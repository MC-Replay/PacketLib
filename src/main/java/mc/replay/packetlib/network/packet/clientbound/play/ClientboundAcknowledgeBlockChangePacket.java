package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.VAR_INT;

public record ClientboundAcknowledgeBlockChangePacket(int sequence) implements ClientboundPacket {

    public ClientboundAcknowledgeBlockChangePacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(VAR_INT)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(VAR_INT, this.sequence);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ACKNOWLEDGE_BLOCK_CHANGE;
    }
}