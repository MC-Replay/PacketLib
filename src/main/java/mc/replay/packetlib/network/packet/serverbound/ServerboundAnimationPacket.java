package mc.replay.packetlib.network.packet.serverbound;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ServerboundPacket;
import mc.replay.packetlib.network.packet.identifier.ServerboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

public record ServerboundAnimationPacket(@NotNull Hand hand) implements ServerboundPacket {

    public ServerboundAnimationPacket(@NotNull PacketBuffer reader) {
        this(
                reader.readEnum(Hand.class)
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.SERVERBOUND_ANIMATION;
    }

    public enum Hand {
        MAIN_HAND,
        OFF_HAND
    }
}