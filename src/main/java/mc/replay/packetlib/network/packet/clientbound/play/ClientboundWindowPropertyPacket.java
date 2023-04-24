package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.BYTE;
import static mc.replay.packetlib.network.ReplayByteBuffer.SHORT;

public record ClientboundWindowPropertyPacket(byte windowId, short property, short value) implements ClientboundPacket {

    public ClientboundWindowPropertyPacket(byte windowId, @NotNull InventoryView.Property property, short value) {
        this(
                windowId,
                (short) property.getId(),
                value
        );
    }

    public ClientboundWindowPropertyPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(BYTE),
                reader.read(SHORT),
                reader.read(SHORT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(BYTE, this.windowId);
        writer.write(SHORT, this.property);
        writer.write(SHORT, this.value);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.WINDOW_PROPERTY;
    }
}