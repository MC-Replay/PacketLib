package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.BLOCK_POSITION;
import static mc.replay.packetlib.network.PacketBuffer.VAR_INT;

public record ClientboundBlockChangePacket(@NotNull Vector blockPosition,
                                           int blockStateId) implements ClientboundPacket {

    public ClientboundBlockChangePacket(@NotNull Block block) {
        this(block.getLocation().clone().toVector(), block.getState().getRawData());
    }

    public ClientboundBlockChangePacket(@NotNull PacketBuffer reader) {
        this(reader.read(BLOCK_POSITION), reader.read(VAR_INT));
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(BLOCK_POSITION, this.blockPosition);
        writer.write(VAR_INT, this.blockStateId);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.BLOCK_CHANGE;
    }
}