package mc.replay.packetlib;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.PacketRegistry;
import mc.replay.packetlib.network.netty.PacketLibInjector;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public final class PacketLib {

    private static final PacketRegistry REGISTRY = new PacketRegistry();
    private static final PacketLibInjector INJECTOR = new PacketLibInjector();

    public static void inject(@NotNull Channel channel) {
        INJECTOR.inject(channel);
    }

    public static void sendPacket(@NotNull Player player, @NotNull ClientboundPacket packet) {
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

    public static @NotNull PacketRegistry getRegistry() {
        return REGISTRY;
    }
}