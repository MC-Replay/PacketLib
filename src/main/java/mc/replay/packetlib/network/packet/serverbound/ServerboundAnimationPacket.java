package mc.replay.packetlib.network.packet.serverbound;

import mc.replay.packetlib.data.entity.PlayerHand;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ServerboundPacket;
import mc.replay.packetlib.network.packet.identifier.ServerboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

public record ServerboundAnimationPacket(@NotNull PlayerHand hand) implements ServerboundPacket {

    public ServerboundAnimationPacket(@NotNull PacketBuffer reader) {
        this(
                reader.readEnum(PlayerHand.class)
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.ANIMATION;
    }
}