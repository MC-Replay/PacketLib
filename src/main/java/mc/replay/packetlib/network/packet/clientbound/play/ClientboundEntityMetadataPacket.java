package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.entity.Metadata;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static mc.replay.packetlib.network.ReplayByteBuffer.BYTE;
import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public record ClientboundEntityMetadataPacket(int entityId,
                                              @NotNull Map<Integer, Metadata.Entry<?>> entries) implements ClientboundPacket {

    public ClientboundEntityMetadataPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                readEntries(reader)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        for (Map.Entry<Integer, Metadata.Entry<?>> entry : this.entries.entrySet()) {
            writer.write(BYTE, entry.getKey().byteValue());
            writer.write(entry.getValue());
        }
        writer.write(BYTE, (byte) 0xFF); // End
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_METADATA;
    }

    private static Map<Integer, Metadata.Entry<?>> readEntries(@NotNull ReplayByteBuffer reader) {
        Map<Integer, Metadata.Entry<?>> entries = new HashMap<>();
        while (true) {
            final byte index = reader.read(BYTE);
            if (index == (byte) 0xFF) break; // End
            final int type = reader.read(VAR_INT);
            entries.put((int) index, Metadata.Entry.read(type, reader));
        }
        return entries;
    }
}