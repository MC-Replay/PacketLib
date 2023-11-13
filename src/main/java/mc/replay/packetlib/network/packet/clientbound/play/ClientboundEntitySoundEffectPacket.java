package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundEntitySoundEffectPacket(Integer soundId, String soundName, Float range, int sourceId,
                                                 int entityId, float volume, float pitch,
                                                 long seed) implements ClientboundPacket {

    public ClientboundEntitySoundEffectPacket(int soundId, int sourceId, int entityId, float volume, float pitch, long seed) {
        this(soundId, null, null, sourceId, entityId, volume, pitch, seed);
    }

    public ClientboundEntitySoundEffectPacket(String soundName, float range, int sourceId, int entityId, float volume, float pitch, long seed) {
        this(null, soundName, range, sourceId, entityId, volume, pitch, seed);
    }

    // <= 1.18.2
    public ClientboundEntitySoundEffectPacket(int soundId, int sourceId, int entityId, float volume, float pitch) {
        this(soundId, sourceId, entityId, volume, pitch, 0);
    }

    public ClientboundEntitySoundEffectPacket(@NotNull ReplayByteBuffer reader) {
        this(read(reader));
    }

    private ClientboundEntitySoundEffectPacket(ClientboundEntitySoundEffectPacket packet) {
        this(
                packet.soundId(),
                packet.soundName(),
                packet.range(),
                packet.sourceId(),
                packet.entityId(),
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
        writer.write(VAR_INT, this.entityId);
        writer.write(FLOAT, this.volume);
        writer.write(FLOAT, this.pitch);

        if (is1194) {
            writer.write(LONG, this.seed);
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_SOUND_EFFECT;
    }

    private static ClientboundEntitySoundEffectPacket read(ReplayByteBuffer reader) {
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
        int entityId = reader.read(VAR_INT);
        float volume = reader.read(FLOAT);
        float pitch = reader.read(FLOAT);
        long seed = (is1194) ? reader.read(LONG) : 0;

        return new ClientboundEntitySoundEffectPacket(soundId, soundName, range, sourceId, entityId, volume, pitch, seed);
    }
}