package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

@PacketInfo
public record ClientboundEntityDestroyPacket(@NotNull List<@NotNull Integer> entityIds) implements ClientboundPacket {

    public ClientboundEntityDestroyPacket {
        entityIds = List.copyOf(entityIds);
    }

    public ClientboundEntityDestroyPacket(int entityId) {
        this(List.of(entityId));
    }

    public ClientboundEntityDestroyPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.readCollection(VAR_INT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.writeCollection(VAR_INT, this.entityIds);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_DESTROY;
    }
}