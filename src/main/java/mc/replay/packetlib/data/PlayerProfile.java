package mc.replay.packetlib.data;

import mc.replay.packetlib.network.PacketBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public record PlayerProfile(UUID uuid, String name, Map<String, Property> properties) {

    public record Property(String name, String value, String signature) implements PacketBuffer.Writer {

        public Property(@NotNull PacketBuffer reader) {
            this(
                    reader.read(PacketBuffer.STRING),
                    reader.read(PacketBuffer.STRING),
                    reader.read(PacketBuffer.STRING)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.write(PacketBuffer.STRING, this.name);
            writer.write(PacketBuffer.STRING, this.value);
            writer.write(PacketBuffer.STRING, this.signature);
        }
    }
}