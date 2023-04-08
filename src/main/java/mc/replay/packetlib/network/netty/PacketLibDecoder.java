package mc.replay.packetlib.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.utils.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

final class PacketLibDecoder extends ByteToMessageDecoder {

    private final PacketLib packetLib;
    private final ByteToMessageDecoder minecraftDecoder;

    public PacketLibDecoder(PacketLib packetLib, ByteToMessageDecoder minecraftDecoder) {
        this.packetLib = packetLib;
        this.minecraftDecoder = minecraftDecoder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        ReplayByteBuffer replayByteBuffer = new ReplayByteBuffer(byteBuf.nioBuffer());
        int packetId = replayByteBuffer.read(VAR_INT);

        if (this.packetLib.getPacketListener().isListeningServerbound(packetId)) {
            ServerboundPacket serverboundPacket = this.packetLib.getPacketRegistry().getServerboundPacket(packetId, replayByteBuffer);
            System.out.println(serverboundPacket);
        }

        byteBuf.resetReaderIndex();

        try {
            list.addAll(Reflections.callDecode(this.minecraftDecoder, ctx, byteBuf));
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            } else if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
        }
    }
}