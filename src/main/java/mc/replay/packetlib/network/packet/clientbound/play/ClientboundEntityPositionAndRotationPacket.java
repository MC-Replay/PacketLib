package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundEntityPositionAndRotationPacket(int entityId, short deltaX, short deltaY,
                                                         short deltaZ, float yaw, float pitch,
                                                         boolean onGround) implements ClientboundPacket {

    public ClientboundEntityPositionAndRotationPacket(int entityId, @NotNull Pos deltaPosition, boolean onGround) {
        this(
                entityId,
                (short) ((deltaPosition.x() * 32) * 128),
                (short) ((deltaPosition.y() * 32) * 128),
                (short) ((deltaPosition.z() * 32) * 128),
                deltaPosition.yaw(),
                deltaPosition.pitch(),
                onGround
        );
    }

    public ClientboundEntityPositionAndRotationPacket(int entityId, @NotNull Pos newPosition, @NotNull Pos oldPosition, boolean onGround) {
        this(
                entityId,
                newPosition.subtract(oldPosition),
                onGround
        );
    }

    public ClientboundEntityPositionAndRotationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(SHORT),
                reader.read(SHORT),
                reader.read(SHORT),
                reader.read(BYTE) * 360f / 256f,
                reader.read(BYTE) * 360f / 256f,
                reader.read(BOOLEAN)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(SHORT, this.deltaX);
        writer.write(SHORT, this.deltaY);
        writer.write(SHORT, this.deltaZ);
        writer.write(BYTE, (byte) (this.yaw * 256 / 360));
        writer.write(BYTE, (byte) (this.pitch * 256 / 360));
        writer.write(BOOLEAN, this.onGround);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_POSITION_AND_ROTATION;
    }
}