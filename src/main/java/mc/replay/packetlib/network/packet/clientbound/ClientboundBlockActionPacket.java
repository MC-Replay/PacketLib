package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundBlockActionPacket(@NotNull Vector blockPosition, byte actionId, byte actionParam,
                                           int blockId) implements ClientboundPacket {

    public ClientboundBlockActionPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(BLOCK_POSITION),
                reader.read(BYTE),
                reader.read(BYTE),
                reader.read(VAR_INT)
        );
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
        return ClientboundPacketIdentifier.BLOCK_ACTION;
    }
}