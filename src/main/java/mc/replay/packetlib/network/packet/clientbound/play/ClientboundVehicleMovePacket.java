package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.DOUBLE;
import static mc.replay.packetlib.network.ReplayByteBuffer.FLOAT;

public record ClientboundVehicleMovePacket(double x, double y, double z, float yaw,
                                           float pitch) implements ClientboundPacket {

    public ClientboundVehicleMovePacket(@NotNull Pos position) {
        this(
                position.x(),
                position.y(),
                position.z(),
                position.yaw(),
                position.pitch()
        );
    }

    public ClientboundVehicleMovePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(FLOAT),
                reader.read(FLOAT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(DOUBLE, this.x);
        writer.write(DOUBLE, this.y);
        writer.write(DOUBLE, this.z);
        writer.write(FLOAT, this.yaw);
        writer.write(FLOAT, this.pitch);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.VEHICLE_MOVE;
    }
}