package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.BYTE;
import static mc.replay.packetlib.network.ReplayByteBuffer.INT;

public record ClientboundEntityStatusPacket(int entityId, byte status) implements ClientboundPacket {

    public ClientboundEntityStatusPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(INT),
                reader.read(BYTE)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(INT, this.entityId);
        writer.write(BYTE, this.status);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_STATUS;
    }
}