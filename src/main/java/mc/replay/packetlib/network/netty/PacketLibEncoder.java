package mc.replay.packetlib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.utils.Reflections;

import java.lang.reflect.InvocationTargetException;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

@SuppressWarnings("rawtypes")
final class PacketLibEncoder extends MessageToByteEncoder {

    private final PacketLib packetLib;
    private final MessageToByteEncoder minecraftEncoder;

    public PacketLibEncoder(PacketLib packetLib, MessageToByteEncoder minecraftEncoder) {
        this.packetLib = packetLib;
        this.minecraftEncoder = minecraftEncoder;
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, Object object, final ByteBuf byteBuf) throws Exception {
        if (object instanceof ByteBuf) {
            byteBuf.writeBytes((ByteBuf) object);
            return;
        }

        if (object instanceof ClientboundPacket packet) {
            ReplayByteBuffer buffer = new ReplayByteBuffer(byteBuf.nioBuffer());
            buffer.write(VAR_INT, packet.identifier().getIdentifier());
            packet.write(buffer);

            byteBuf.writeBytes(buffer.readBytes(buffer.writeIndex()));

            this.packetLib.getPacketListener().publishClientbound(packet);
            return;
        }

        try {
            Reflections.callEncode(this.minecraftEncoder, ctx, object, byteBuf);

            // If we want to listen to packets that are not sent by the PacketLib, we can do it here by reading the bytebuf.
        } catch (InvocationTargetException exception) {
            if (exception.getCause() instanceof Exception) {
                throw (Exception) exception.getCause();
            } else if (exception.getCause() instanceof Error) {
                throw (Error) exception.getCause();
            }
        }
    }
}