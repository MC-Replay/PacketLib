package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.INT;

@PacketInfo
public record ClientboundEntityAttachPacket(int attachedEntityId, int holdingEntityId) implements ClientboundPacket {

    public ClientboundEntityAttachPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(INT),
                reader.read(INT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(INT, this.attachedEntityId);
        writer.write(INT, this.holdingEntityId);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_ATTACH;
    }
}