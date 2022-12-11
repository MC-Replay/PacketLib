package mc.replay.packetlib.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.ServerboundPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public final class PacketUtils {

    private PacketUtils() {
    }

    public static @Nullable ClientboundPacket readClientboundPacket(@NotNull Object packetObject) {
        try {
            Integer packetId = Reflections.getClientboundPacketId(packetObject);
            if (packetId != null && PacketLib.getInstance().getPacketRegistry().isClientboundRegistered(packetId)) {
                ByteBuffer buffer = serializePacket(packetObject);

                PacketBuffer packetBuffer = new PacketBuffer(buffer);
                return PacketLib.getInstance().getPacketRegistry().getClientboundPacket(packetId, packetBuffer);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    public static @Nullable ServerboundPacket readServerboundPacket(@NotNull Object packetObject) {
        try {
            Integer packetId = Reflections.getServerboundPacketId(packetObject);
            if (packetId != null && PacketLib.getInstance().getPacketRegistry().isServerboundRegistered(packetId)) {
                ByteBuffer buffer = serializePacket(packetObject);

                PacketBuffer packetBuffer = new PacketBuffer(buffer);
                return PacketLib.getInstance().getPacketRegistry().getServerboundPacket(packetId, packetBuffer);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

    private static @NotNull ByteBuffer serializePacket(Object packetObject) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(2_097_152);

        Object packetDataSerializer = Reflections.createPacketDataSerializer(Unpooled.wrappedBuffer(buffer));
        ((ByteBuf) packetDataSerializer).writerIndex(0);
        ((ByteBuf) packetDataSerializer).readerIndex(0);
        Reflections.serializePacket(packetObject, packetDataSerializer);

        return buffer;
    }
}