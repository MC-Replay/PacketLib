package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundExperienceOrbSpawnPacket(int entityID, Pos position,
                                                  short experienceAmount) implements ClientboundPacket {

    public ClientboundExperienceOrbSpawnPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                Pos.of(
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(DOUBLE)
                ),
                reader.read(SHORT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityID);
        writer.write(DOUBLE, this.position.x());
        writer.write(DOUBLE, this.position.y());
        writer.write(DOUBLE, this.position.z());
        writer.write(SHORT, this.experienceAmount);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.EXPERIENCE_ORB_SPAWN;
    }
}