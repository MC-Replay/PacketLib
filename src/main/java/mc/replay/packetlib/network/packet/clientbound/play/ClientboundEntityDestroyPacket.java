package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mc.replay.packetlib.network.PacketBuffer.VAR_INT;

public record ClientboundEntityDestroyPacket(@NotNull List<@NotNull Integer> entityIds) implements ClientboundPacket {

    public ClientboundEntityDestroyPacket {
        entityIds = List.copyOf(entityIds);
    }

    public ClientboundEntityDestroyPacket(int entityId) {
        this(List.of(entityId));
    }

    public ClientboundEntityDestroyPacket(@NotNull PacketBuffer reader) {
        this(
                reader.readCollection(VAR_INT)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.writeCollection(VAR_INT, this.entityIds);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_DESTROY;
    }
}