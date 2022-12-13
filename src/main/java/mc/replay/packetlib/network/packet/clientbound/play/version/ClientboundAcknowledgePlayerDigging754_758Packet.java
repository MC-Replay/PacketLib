package mc.replay.packetlib.network.packet.clientbound.play.version;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundAcknowledgePlayerDigging754_758Packet(@NotNull Vector blockPosition, int blockStateId,
                                                               int stateId,
                                                               boolean successful) implements ClientboundPacket {

    public ClientboundAcknowledgePlayerDigging754_758Packet(@NotNull PacketBuffer reader) {
        this(
                reader.read(BLOCK_POSITION),
                reader.read(VAR_INT),
                reader.read(VAR_INT),
                reader.read(BOOLEAN)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(BLOCK_POSITION, this.blockPosition);
        writer.write(VAR_INT, this.blockStateId);
        writer.write(VAR_INT, this.stateId);
        writer.write(BOOLEAN, this.successful);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ACKNOWLEDGE_PLAYER_DIGGING_754_758;
    }
}