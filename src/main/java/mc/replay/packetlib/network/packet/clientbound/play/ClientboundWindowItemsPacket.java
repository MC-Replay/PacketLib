package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Item;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundWindowItemsPacket(byte windowId, int stateId, @NotNull List<Item> items,
                                           @NotNull Item carriedItem) implements ClientboundPacket {

    public ClientboundWindowItemsPacket {
        items = List.copyOf(items);
    }

    public ClientboundWindowItemsPacket(byte windowId, @NotNull List<Item> items, @NotNull Item carriedItem) {
        this(
                windowId,
                0,
                items,
                carriedItem
        );
    }

    public ClientboundWindowItemsPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(BYTE),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1))
                        ? reader.read(VAR_INT)
                        : 0,
                reader.readCollection(ITEM),
                reader.read(ITEM)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(BYTE, this.windowId);

        if (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_17_1)) {
            writer.write(VAR_INT, this.stateId);
        }

        writer.writeCollection(ITEM, this.items);
        writer.write(ITEM, this.carriedItem);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.WINDOW_ITEMS;
    }
}