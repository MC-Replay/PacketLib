package mc.replay.packetlib.network.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.events.AsyncPacketReceivedEvent;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ServerboundPacket;
import net.minecraft.server.v1_16_R3.EnumProtocol;
import net.minecraft.server.v1_16_R3.EnumProtocolDirection;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import org.bukkit.Bukkit;

import java.nio.ByteBuffer;

public final class PacketLibListener extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet<?> mcPacket) {
            Integer packetId = EnumProtocol.a(0).a(EnumProtocolDirection.SERVERBOUND, mcPacket);
            if (packetId != null && PacketLib.getRegistry().isServerboundRegistered(packetId)) {
                ByteBuffer buffer = ByteBuffer.allocateDirect(2_097_152);

                PacketDataSerializer dataSerializer = new PacketDataSerializer(Unpooled.wrappedBuffer(buffer));
                dataSerializer.writerIndex(0);
                dataSerializer.readerIndex(0);
                mcPacket.b(dataSerializer);

                PacketBuffer packetBuffer = new PacketBuffer(buffer);

                ServerboundPacket serverboundPacket = PacketLib.getRegistry().getServerboundPacket(packetId, packetBuffer);

                if (serverboundPacket != null) {
                    AsyncPacketReceivedEvent event = new AsyncPacketReceivedEvent(serverboundPacket);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}