package mc.replay.packetlib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.events.AsyncPacketReceivedEvent;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ServerboundPacket;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.Bukkit;

import java.nio.ByteBuffer;

public final class PacketLibListener extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packetObject) throws Exception {
        Integer packetId = Reflections.getServerboundPacketId(packetObject);
        if (packetId != null && PacketLib.getRegistry().isServerboundRegistered(packetId)) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(2_097_152);

            Object packetDataSerializer = Reflections.createPacketDataSerializer(Unpooled.wrappedBuffer(buffer));
            ((ByteBuf) packetDataSerializer).writerIndex(0);
            ((ByteBuf) packetDataSerializer).readerIndex(0);
            Reflections.serializePacket(packetObject, packetDataSerializer);

            PacketBuffer packetBuffer = new PacketBuffer(buffer);
            ServerboundPacket serverboundPacket = PacketLib.getRegistry().getServerboundPacket(packetId, packetBuffer);

            if (serverboundPacket != null) {
                AsyncPacketReceivedEvent event = new AsyncPacketReceivedEvent(serverboundPacket);
                Bukkit.getPluginManager().callEvent(event);
            }
        }

        super.channelRead(ctx, packetObject);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}