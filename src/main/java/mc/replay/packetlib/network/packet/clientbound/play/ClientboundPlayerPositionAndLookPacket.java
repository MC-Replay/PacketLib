package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundPlayerPositionAndLookPacket(double x, double y, double z, float yaw, float pitch,
                                                     byte flags, int teleportId,
                                                     boolean dismountVehicle) implements ClientboundPacket {

    public ClientboundPlayerPositionAndLookPacket(double x, double y, double z, float yaw, float pitch, byte flags, int teleportId) {
        this(
                x,
                y,
                z,
                yaw,
                pitch,
                flags,
                teleportId,
                false
        );
    }

    public ClientboundPlayerPositionAndLookPacket(Pos pos, byte flags, int teleportId, boolean dismountVehicle) {
        this(
                pos.x(),
                pos.y(),
                pos.z(),
                pos.yaw(),
                pos.pitch(),
                flags,
                teleportId,
                dismountVehicle
        );
    }

    public ClientboundPlayerPositionAndLookPacket(Pos pos, byte flags, int teleportId) {
        this(
                pos,
                flags,
                teleportId,
                false
        );
    }

    public ClientboundPlayerPositionAndLookPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(BYTE),
                reader.read(VAR_INT),
                (ProtocolVersion.getServerVersion().isEqual(ProtocolVersion.MINECRAFT_1_16_5))
                        ? reader.read(BOOLEAN)
                        : false
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(DOUBLE, this.x);
        writer.write(DOUBLE, this.y);
        writer.write(DOUBLE, this.z);
        writer.write(FLOAT, this.yaw);
        writer.write(FLOAT, this.pitch);
        writer.write(BYTE, this.flags);
        writer.write(VAR_INT, this.teleportId);

        if (ProtocolVersion.getServerVersion().isEqual(ProtocolVersion.MINECRAFT_1_16_5)) {
            writer.write(BOOLEAN, this.dismountVehicle);
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.PLAYER_POSITION_AND_LOOK;
    }
}