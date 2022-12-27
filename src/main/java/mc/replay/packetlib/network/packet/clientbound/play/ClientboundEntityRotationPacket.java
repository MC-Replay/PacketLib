package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundEntityRotationPacket(int entityId, float yaw, float pitch,
                                              boolean onGround) implements ClientboundPacket {

    public ClientboundEntityRotationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(BYTE) * 360f / 256f,
                reader.read(BYTE) * 360f / 256f,
                reader.read(BOOLEAN)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(BYTE, (byte) (this.yaw * 256 / 360));
        writer.write(BYTE, (byte) (this.pitch * 256 / 360));
        writer.write(BOOLEAN, this.onGround);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_ROTATION;
    }
}