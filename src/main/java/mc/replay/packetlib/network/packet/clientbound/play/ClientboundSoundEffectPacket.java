package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundSoundEffectPacket(int soundId, int sourceId, int x, int y,
                                           int z, float volume, float pitch, long seed) implements ClientboundPacket {

    public ClientboundSoundEffectPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
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
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.soundId);
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
        return ClientboundPacketIdentifier.SOUND_EFFECT;
    }
}