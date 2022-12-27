package mc.replay.packetlib.data;

import mc.replay.packetlib.network.ReplayByteBuffer;
import org.jetbrains.annotations.NotNull;

public record PlayerProfileProperty(String name, String value, String signature) implements ReplayByteBuffer.Writer {

    public PlayerProfileProperty(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(ReplayByteBuffer.STRING),
                reader.read(ReplayByteBuffer.STRING),
                reader.readOptional(ReplayByteBuffer.STRING)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(ReplayByteBuffer.STRING, this.name);
        writer.write(ReplayByteBuffer.STRING, this.value);
        writer.writeOptional(ReplayByteBuffer.STRING, this.signature);
    }
}