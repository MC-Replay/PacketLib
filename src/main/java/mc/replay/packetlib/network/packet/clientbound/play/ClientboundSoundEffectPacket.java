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
public record ClientboundSoundEffectPacket(Integer soundId, @Nullable String soundName,
                                           @Nullable Float range, int sourceId, int x, int y,
                                           int z, float volume, float pitch,
                                           @Nullable Long seed) implements ClientboundPacket {

    public ClientboundSoundEffectPacket(int soundId, int sourceId, int x, int y, int z, float volume, float pitch, @Nullable Long seed) {
        this(
                soundId,
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
    public ClientboundSoundEffectPacket(@NotNull String soundName, float range, int sourceId, int x, int y, int z, float volume, float pitch, long seed) {
        this(
                null,
                soundName,
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
        this(read(reader));
    }

    private ClientboundSoundEffectPacket(ClientboundSoundEffectPacket packet) {
        this(
                packet.soundId(),
                packet.soundName(),
                packet.range(),
                packet.sourceId(),
                packet.x(),
                packet.y(),
                packet.z(),
                packet.volume(),
                packet.pitch(),
                packet.seed()
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        boolean is1194 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4);
        if (is1194) {
            if (this.soundId != null) {
                writer.write(VAR_INT, this.soundId + 1);
            } else {
                writer.write(VAR_INT, 0);
                writer.write(STRING, this.soundName);
                writer.writeOptional(FLOAT, this.range);
            }
        } else {
            writer.write(VAR_INT, this.soundId);
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

    private static ClientboundSoundEffectPacket read(ReplayByteBuffer reader) {
        Integer soundId = reader.read(VAR_INT);
        String soundName = null;
        Float range = null;

        boolean is1194 = ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4);
        if (is1194) {
            if (soundId == 0) {
                soundId = null;
                soundName = reader.read(STRING);
                range = reader.readOptional(FLOAT);
            }
        }

        int sourceId = reader.read(VAR_INT);
        int x = reader.read(INT) / 8;
        int y = reader.read(INT) / 8;
        int z = reader.read(INT) / 8;
        float volume = reader.read(FLOAT);
        float pitch = reader.read(FLOAT);
        Long seed = (is1194) ? reader.read(LONG) : null;

        return new ClientboundSoundEffectPacket(soundId, soundName, range, sourceId, x, y, z, volume, pitch, seed);
    }
}