package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.FLOAT;
import static mc.replay.packetlib.network.PacketBuffer.VAR_INT;

public record ClientboundEntitySoundEffectPacket(int soundId, int sourceId, int entityId,
                                                 float volume, float pitch) implements ClientboundPacket {

    public ClientboundEntitySoundEffectPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(VAR_INT),
                reader.read(VAR_INT),
                reader.read(FLOAT),
                reader.read(FLOAT)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(VAR_INT, this.soundId);
        writer.write(VAR_INT, this.sourceId);
        writer.write(VAR_INT, this.entityId);
        writer.write(FLOAT, this.volume);
        writer.write(FLOAT, this.pitch);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_SOUND_EFFECT;
    }
}