package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.entity.EntityAnimation;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public record ClientboundEntityAnimationPacket(int entityId,
                                               @NotNull EntityAnimation animation) implements ClientboundPacket {

    public ClientboundEntityAnimationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.readEnum(EntityAnimation.class)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.writeEnum(EntityAnimation.class, this.animation);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_ANIMATION;
    }
}