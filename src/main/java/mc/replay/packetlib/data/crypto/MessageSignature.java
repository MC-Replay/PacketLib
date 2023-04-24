package mc.replay.packetlib.data.crypto;

import mc.replay.packetlib.network.ReplayByteBuffer;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.BYTE_ARRAY;
import static mc.replay.packetlib.network.ReplayByteBuffer.Writer;

public record MessageSignature(byte @NotNull [] signature) implements Writer {

    public MessageSignature(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(BYTE_ARRAY)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(BYTE_ARRAY, signature);
    }
}