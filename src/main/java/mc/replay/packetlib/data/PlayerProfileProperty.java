package mc.replay.packetlib.data;

import mc.replay.packetlib.network.PacketBuffer;
import org.jetbrains.annotations.NotNull;

public record PlayerProfileProperty(String name, String value, String signature) implements PacketBuffer.Writer {

    public PlayerProfileProperty(@NotNull PacketBuffer reader) {
        this(
                reader.read(PacketBuffer.STRING),
                reader.read(PacketBuffer.STRING),
                reader.readOptional(PacketBuffer.STRING)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(PacketBuffer.STRING, this.name);
        writer.write(PacketBuffer.STRING, this.value);
        writer.writeOptional(PacketBuffer.STRING, this.signature);
    }
}