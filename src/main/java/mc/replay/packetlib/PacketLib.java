package mc.replay.packetlib;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import mc.replay.packetlib.network.netty.PacketLibInjector;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.PacketRegistry;
import mc.replay.packetlib.utils.ProtocolVersion;
import net.minecraft.server.v1_16_R3.EnumProtocol;
import net.minecraft.server.v1_16_R3.EnumProtocolDirection;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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

            net.minecraft.server.v1_16_R3.Packet<?> mcPacket = EnumProtocol.a(0).a(EnumProtocolDirection.CLIENTBOUND, packetId);
            mcPacket.a(new PacketDataSerializer(Unpooled.copiedBuffer(buffer)));

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(mcPacket);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static @NotNull PacketRegistry getRegistry() {
        return REGISTRY;
    }
}