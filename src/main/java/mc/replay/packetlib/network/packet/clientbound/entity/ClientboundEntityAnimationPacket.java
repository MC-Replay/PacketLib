package mc.replay.packetlib.network.packet.clientbound.entity;

import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.BYTE;
import static mc.replay.packetlib.network.PacketBuffer.VAR_INT;

public record ClientboundEntityAnimationPacket(int entityId,
                                               @NotNull Animation animation) implements ClientboundPacket {

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(BYTE, (byte) this.animation.ordinal());
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.CLIENTBOUND_ENTITY_ANIMATION;
    }

    public enum Animation {
        SWING_MAIN_ARM,
        TAKE_DAMAGE,
        LEAVE_BED,
        SWING_OFFHAND,
        CRITICAL_EFFECT,
        MAGIC_CRITICAL_EFFECT
    }
}