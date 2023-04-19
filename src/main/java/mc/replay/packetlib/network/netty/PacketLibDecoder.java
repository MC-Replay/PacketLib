package mc.replay.packetlib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.user.ConnectionPlayerProvider;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public final class PacketLibDecoder extends ByteToMessageDecoder {

    private final Collection<PacketLib> instances = new HashSet<>();

    private final ConnectionPlayerProvider playerProvider;
    private final ByteToMessageDecoder original;

    public PacketLibDecoder(PacketLib instance, ConnectionPlayerProvider playerProvider, ByteToMessageDecoder original) {
        this.instances.add(instance);

        this.playerProvider = playerProvider;
        this.original = original;
    }

    public @NotNull ByteToMessageDecoder original() {
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
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        ReplayByteBuffer replayByteBuffer = new ReplayByteBuffer(byteBuf.nioBuffer());
        int packetId = replayByteBuffer.read(VAR_INT);

        Collection<PacketLib> listeningInstances = this.findListeningInstances(packetId);
        if (!listeningInstances.isEmpty()) {
            Player player = this.playerProvider.player();

            if (player != null) {
                ServerboundPacket serverboundPacket = PacketLib.getPacketRegistry().getServerboundPacket(packetId, replayByteBuffer);

                if (serverboundPacket != null) {
                    for (PacketLib instance : listeningInstances) {
                        instance.packetListener().publishServerbound(player, serverboundPacket);
                    }
                }
            }
        }

        byteBuf.resetReaderIndex();

        try {
            list.addAll(Reflections.callDecode(this.original, ctx, byteBuf));
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            } else if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
        }
    }

    private Collection<PacketLib> findListeningInstances(int packetId) {
        Collection<PacketLib> instances = new HashSet<>();
        for (PacketLib instance : this.instances) {
            if (instance.settings().listenServerbound() && instance.packetListener().isListeningServerbound(packetId)) {
                instances.add(instance);
            }
        }
        return instances;
    }
}