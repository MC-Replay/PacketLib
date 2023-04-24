package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Item;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundSetSlotPacket(byte windowId, int stateId, short slot,
                                       @NotNull Item item) implements ClientboundPacket {

    public ClientboundSetSlotPacket(byte windowId, short slot, @NotNull Item item) {
        this(
                windowId,
                0,
                slot,
                item
        );
    }

    public ClientboundSetSlotPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(BYTE),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1))
                        ? reader.read(VAR_INT)
                        : 0,
                reader.read(SHORT),
                reader.read(ITEM)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(BYTE, this.windowId);

        if (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1)) {
            writer.write(VAR_INT, this.stateId);
        }

        writer.write(SHORT, this.slot);
        writer.write(ITEM, this.item);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.SET_SLOT;
    }
}