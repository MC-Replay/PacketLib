package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.ItemStackWrapper;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundEntityEquipmentPacket(int entityId,
                                               Map<Byte, ItemStackWrapper> equipment) implements ClientboundPacket {

    public ClientboundEntityEquipmentPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(VAR_INT),
                readEquipment(reader)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(VAR_INT, this.entityId);

        int index = 0;
        for (Map.Entry<Byte, ItemStackWrapper> entry : this.equipment.entrySet()) {
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

    private static Map<Byte, ItemStackWrapper> readEquipment(@NotNull PacketBuffer reader) {
        Map<Byte, ItemStackWrapper> equipment = new HashMap<>();

        byte slot;
        do {
            slot = reader.read(BYTE);
            equipment.put((byte) (slot & 0x7F), reader.read(ITEM));
        } while ((slot & 0x80) == 0x80);

        return equipment;
    }
}