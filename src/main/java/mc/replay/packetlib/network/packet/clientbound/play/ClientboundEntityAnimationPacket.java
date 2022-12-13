package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.VAR_INT;

public record ClientboundEntityAnimationPacket(int entityId, int animationId) implements ClientboundPacket {

    public ClientboundEntityAnimationPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(VAR_INT)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(VAR_INT, this.animationId);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_ANIMATION;
    }
}