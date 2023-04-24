package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundExperienceOrbSpawnPacket(int entityId, double x, double y, double z,
                                                  short experienceAmount) implements ClientboundPacket {

    public ClientboundExperienceOrbSpawnPacket(int entityId, @NotNull Pos position, short experienceAmount) {
        this(
                entityId,
                position.x(),
                position.y(),
                position.z(),
                experienceAmount
        );
    }

    public ClientboundExperienceOrbSpawnPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(SHORT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(DOUBLE, this.x);
        writer.write(DOUBLE, this.y);
        writer.write(DOUBLE, this.z);
        writer.write(SHORT, this.experienceAmount);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.EXPERIENCE_ORB_SPAWN;
    }
}