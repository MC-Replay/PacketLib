package mc.replay.packetlib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.netty.exception.SkipEncodeException;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.user.ConnectionPlayerProvider;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

@SuppressWarnings("rawtypes")
public final class PacketLibEncoder extends MessageToByteEncoder {

    private final Collection<PacketLib> instances = new HashSet<>();

    private final ConnectionPlayerProvider playerProvider;
    private final MessageToByteEncoder original;

    public PacketLibEncoder(PacketLib instance, ConnectionPlayerProvider playerProvider, MessageToByteEncoder original) {
        this.instances.add(instance);

        this.playerProvider = playerProvider;
        this.original = original;
    }

    public @NotNull ConnectionPlayerProvider connection() {
        return this.playerProvider;
    }

    public @NotNull MessageToByteEncoder original() {
        return this.original;
    }

    @NotNull Collection<PacketLib> getInstances() {
        return this.instances;
    }

    void addInstance(@NotNull PacketLib instance) {
        if (!this.instances.contains(instance)) {
            this.instances.add(instance);
        }
    }

    void removeInstance(@NotNull PacketLib instance) {
        this.instances.remove(instance);
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, Object object, final ByteBuf byteBuf) throws Exception {
        if (object instanceof ByteBuf) {
            byteBuf.writeBytes((ByteBuf) object);
            return;
        }

        if (object instanceof ClientboundPacket packet) {
            if (this.instances.isEmpty()) return;

            ReplayByteBuffer buffer = new ReplayByteBuffer(byteBuf.nioBuffer());
            buffer.write(VAR_INT, packet.identifier().getIdentifier());
            packet.write(buffer);

            byteBuf.writeBytes(buffer.readBytes(buffer.writeIndex()));
            return;
        }

        try {
            Reflections.callEncode(this.original, ctx, object, byteBuf);
            if (this.instances.isEmpty()) return;

            ReplayByteBuffer buffer = new ReplayByteBuffer(byteBuf.nioBuffer());
            int packetId = buffer.read(VAR_INT);

            Collection<PacketLib> listeningInstances = this.findListeningMinecraftInstances(packetId);
            if (listeningInstances.isEmpty()) return;

            Player player = this.playerProvider.player();
            if (player == null) return;

            ClientboundPacket clientboundPacket = PacketLib.getPacketRegistry().getClientboundPacket(packetId, buffer);
            if (clientboundPacket == null) return;

            boolean shouldCancel = false;
            for (PacketLib instance : listeningInstances) {
                boolean cancel = instance.packetListener().publishClientbound(player, clientboundPacket);
                if (cancel) shouldCancel = true;
            }

            if (shouldCancel) {
                byteBuf.clear();
                throw new SkipEncodeException();
            }
        } catch (InvocationTargetException exception) {
            if (exception.getCause() instanceof Exception) {
                throw (Exception) exception.getCause();
            } else if (exception.getCause() instanceof Error) {
                throw (Error) exception.getCause();
            }
        }
    }

    private Collection<PacketLib> findListeningMinecraftInstances(int packetId) {
        Collection<PacketLib> instances = new HashSet<>();
        for (PacketLib instance : this.instances) {
            if (instance.settings().listenMinecraftClientbound() && instance.packetListener().isListeningClientbound(packetId)) {
                instances.add(instance);
            }
        }
        return instances;
    }
}