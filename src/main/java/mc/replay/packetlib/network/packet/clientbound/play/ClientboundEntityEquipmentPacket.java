package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Item;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundEntityEquipmentPacket(int entityId,
                                               Map<Byte, Item> equipment) implements ClientboundPacket {

    public ClientboundEntityEquipmentPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                readEquipment(reader)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);

        int index = 0;
        for (Map.Entry<Byte, Item> entry : this.equipment.entrySet()) {
            boolean last = index++ == this.equipment.size() - 1;
            byte slot = entry.getKey();
            if (!last) slot |= 0x80;
            writer.write(BYTE, slot);
            writer.write(ITEM, entry.getValue());
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_EQUIPMENT;
    }

    private static Map<Byte, Item> readEquipment(@NotNull ReplayByteBuffer reader) {
        Map<Byte, Item> equipment = new HashMap<>();

        byte slot;
        do {
            slot = reader.read(BYTE);
            equipment.put((byte) (slot & 0x7F), reader.read(ITEM));
        } while ((slot & 0x80) == 0x80);

        return equipment;
    }
}