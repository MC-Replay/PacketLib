package mc.replay.packetlib;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.PacketRegistry;
import mc.replay.packetlib.network.netty.PacketLibInjector;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifierLoader;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class PacketLib {

    private static PacketLib instance;

    @ApiStatus.Internal
    public static PacketLib getInstance() {
        return instance;
    }

    private final PacketRegistry packetRegistry;
    private final PacketIdentifierLoader packetIdentifierLoader;
    private final PacketLibInjector injector;

    public PacketLib() {
        instance = this;

        this.packetRegistry = new PacketRegistry();
        this.packetIdentifierLoader = new PacketIdentifierLoader();
        this.injector = new PacketLibInjector(this);
    }

    public void inject(@NotNull Channel channel) {
        this.injector.inject(channel);
    }

    public void sendPacket(@NotNull Player player, @NotNull ClientboundPacket packet) {
        try {
            int packetId = packet.identifier().getIdentifier();

            ByteBuffer buffer = ByteBuffer.allocateDirect(2_097_152);
            PacketBuffer packetBuffer = new PacketBuffer(buffer);

            packet.write(packetBuffer);

            Object clientboundPacket = Reflections.getClientboundPacket(Unpooled.copiedBuffer(buffer), packetId);
            if (clientboundPacket == null) return;

            Reflections.sendPacket(player, clientboundPacket);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public @NotNull PacketRegistry getPacketRegistry() {
        return this.packetRegistry;
    }

    public @NotNull PacketIdentifierLoader getPacketIdentifierLoader() {
        return this.packetIdentifierLoader;
    }

    public @NotNull PacketLibInjector getInjector() {
        return this.injector;
    }
}