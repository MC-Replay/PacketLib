package mc.replay.packetlib.network.packet.serverbound.play;

import mc.replay.packetlib.data.entity.PlayerHand;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

public record ServerboundAnimationPacket(@NotNull PlayerHand hand) implements ServerboundPacket {

    public ServerboundAnimationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.readEnum(PlayerHand.class)
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.ANIMATION;
    }
}