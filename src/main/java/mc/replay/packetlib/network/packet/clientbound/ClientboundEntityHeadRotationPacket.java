package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundEntityHeadRotationPacket(int entityId, float yaw) implements ClientboundPacket {

    public ClientboundEntityHeadRotationPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(BYTE) * 360f / 256f
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(BYTE, (byte) (this.yaw * 256 / 360));
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_HEAD_ROTATION;
    }
}