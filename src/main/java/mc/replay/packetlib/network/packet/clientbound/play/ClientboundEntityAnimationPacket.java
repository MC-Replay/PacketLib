package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.entity.EntityAnimation;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.BYTE;
import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

@PacketInfo
public record ClientboundEntityAnimationPacket(int entityId,
                                               @NotNull EntityAnimation animation) implements ClientboundPacket {

    public ClientboundEntityAnimationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                EntityAnimation.values()[reader.read(BYTE)]
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(BYTE, (byte) this.animation.ordinal());
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_ANIMATION;
    }
}