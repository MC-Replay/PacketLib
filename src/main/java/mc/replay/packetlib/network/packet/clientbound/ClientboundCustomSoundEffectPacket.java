package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundCustomSoundEffectPacket(@NotNull String soundName, int sourceId, int x, int y,
                                                 int z,
                                                 float volume, float pitch, long seed) implements ClientboundPacket {

    public ClientboundCustomSoundEffectPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(STRING),
                reader.read(VAR_INT),
                reader.read(INT) / 8,
                reader.read(INT) / 8,
                reader.read(INT) / 8,
                reader.read(FLOAT),
                reader.read(FLOAT),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19))
                        ? 0
                        : reader.read(LONG)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(STRING, this.soundName);
        writer.write(VAR_INT, this.sourceId);
        writer.write(INT, this.x * 8);
        writer.write(INT, this.y * 8);
        writer.write(INT, this.z * 8);
        writer.write(FLOAT, this.volume);
        writer.write(FLOAT, this.pitch);

        if (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19)) {
            writer.write(LONG, this.seed);
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.CUSTOM_SOUND_EFFECT;
    }
}