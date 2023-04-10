package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.SHORT;
import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public record ClientboundEntityVelocityPacket(int entityId, short velocityX, short velocityY,
                                              short velocityZ) implements ClientboundPacket {

    public ClientboundEntityVelocityPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(SHORT),
                reader.read(SHORT),
                reader.read(SHORT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(SHORT, this.velocityX);
        writer.write(SHORT, this.velocityY);
        writer.write(SHORT, this.velocityZ);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_VELOCITY;
    }
}
