package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundEntityTeleportPacket(int entityId, double x, double y, double z, float yaw, float pitch,
                                              boolean onGround) implements ClientboundPacket {

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(DOUBLE, this.x);
        writer.write(DOUBLE, this.y);
        writer.write(DOUBLE, this.z);
        writer.write(BYTE, (byte) (this.yaw * 256f / 360f));
        writer.write(BYTE, (byte) (this.pitch * 256f / 360f));
        writer.write(BOOLEAN, this.onGround);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.CLIENTBOUND_ENTITY_ROTATION;
    }
}