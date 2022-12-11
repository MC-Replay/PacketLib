package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.data.block.BlockAction;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundBlockActionPacket(@NotNull Vector blockPosition, byte actionId, byte actionParam,
                                           int blockId) implements ClientboundPacket {

    public ClientboundBlockActionPacket(@NotNull Vector blockPosition, byte actionId, byte actionParam, @NotNull Block block) {
        this(blockPosition, actionId, actionParam, block.getState().getData().getData());
    }

    public ClientboundBlockActionPacket(@NotNull Vector blockPosition, @NotNull BlockAction blockAction, int blockId) {
        this(blockPosition, blockAction.actionId(), blockAction.actionParam(), blockId);
    }

    public ClientboundBlockActionPacket(@NotNull Vector blockPosition, @NotNull BlockAction blockAction, @NotNull Block block) {
        this(blockPosition, blockAction.actionId(), blockAction.actionParam(), block);
    }

    public ClientboundBlockActionPacket(@NotNull PacketBuffer reader) {
        this(reader.read(BLOCK_POSITION), reader.read(BYTE), reader.read(BYTE), reader.read(VAR_INT));
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(BLOCK_POSITION, this.blockPosition);
        writer.write(BYTE, this.actionId);
        writer.write(BYTE, this.actionParam);
        writer.write(VAR_INT, this.blockId);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.CLIENTBOUND_BLOCK_ACTION;
    }
}