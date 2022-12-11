package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundEntityPositionAndRotationPacket(int entityId, short deltaX, short deltaY,
                                                         short deltaZ, float yaw, float pitch,
                                                         boolean onGround) implements ClientboundPacket {

    public ClientboundEntityPositionAndRotationPacket(int entityId, @NotNull Pos newPosition, @NotNull Pos oldPosition, boolean onGround) {
        this(
                entityId,
                (short) ((newPosition.x() * 32 - oldPosition.x() * 32) * 128),
                (short) ((newPosition.y() * 32 - oldPosition.y() * 32) * 128),
                (short) ((newPosition.z() * 32 - oldPosition.z() * 32) * 128),
                newPosition.yaw(),
                newPosition.pitch(),
                onGround
        );
    }

    public ClientboundEntityPositionAndRotationPacket(@NotNull PacketBuffer reader) {
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
    public void write(@NotNull PacketBuffer writer) {
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