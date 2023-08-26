package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundSoundEffectPacket(int soundId, @Nullable String soundName, @Nullable Boolean hasFixedRange,
                                           @Nullable Float range, int sourceId, int x, int y,
                                           int z, float volume, float pitch,
                                           @Nullable Long seed) implements ClientboundPacket {

    public ClientboundSoundEffectPacket(int soundId, int sourceId, int x, int y, int z, float volume, float pitch, @Nullable Long seed) {
        this(
                soundId,
                null,
                null,
                null,
                sourceId,
                x,
                y,
                z,
                volume,
                pitch,
                seed
        );
    }

    public ClientboundSoundEffectPacket(int soundId, int sourceId, int x, int y, int z, float volume, float pitch) {
        this(
                soundId,
                sourceId,
                x,
                y,
                z,
                volume,
                pitch,
                null
        );
    }

    // Custom sound effect equivalent for versions 1.19.4 and higher
    public ClientboundSoundEffectPacket(@NotNull String soundName, boolean hasFixedRange, float range, int sourceId, int x, int y, int z, float volume, float pitch, long seed) {
        this(
                0,
                soundName,
                hasFixedRange,
                range,
                sourceId,
                x,
                y,
                z,
                volume,
                pitch,
                seed
        );
    }

    public ClientboundSoundEffectPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4))
                        ? reader.readOptional(STRING)
                        : null,
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4))
                        ? reader.readOptional(BOOLEAN)
                        : null,
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4))
                        ? reader.readOptional(FLOAT)
                        : null,
                reader.read(VAR_INT),
                reader.read(INT) / 8,
                reader.read(INT) / 8,
                reader.read(INT) / 8,
                reader.read(FLOAT),
                reader.read(FLOAT),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4))
                        ? reader.read(LONG)
                        : null
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.soundId);

        boolean is1194 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4);
        if (is1194) {
            writer.writeOptional(STRING, this.soundName);
            writer.writeOptional(BOOLEAN, this.hasFixedRange);
            writer.writeOptional(FLOAT, this.range);
        }

        writer.write(VAR_INT, this.sourceId);
        writer.write(INT, this.x * 8);
        writer.write(INT, this.y * 8);
        writer.write(INT, this.z * 8);
        writer.write(FLOAT, this.volume);
        writer.write(FLOAT, this.pitch);

        if (is1194) {
            writer.write(LONG, this.seed);
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.SOUND_EFFECT;
    }
}