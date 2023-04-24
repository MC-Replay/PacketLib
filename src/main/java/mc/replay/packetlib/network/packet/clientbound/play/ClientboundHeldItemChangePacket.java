package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import static mc.replay.packetlib.network.ReplayByteBuffer.BYTE;

public record ClientboundHeldItemChangePacket(@Range(from = 0, to = 8) byte slot) implements ClientboundPacket {

    public ClientboundHeldItemChangePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(BYTE)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(BYTE, this.slot);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.HELD_ITEM_CHANGE;
    }
}