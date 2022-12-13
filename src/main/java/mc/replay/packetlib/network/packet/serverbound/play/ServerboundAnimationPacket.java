package mc.replay.packetlib.network.packet.serverbound.play;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.VAR_INT;

public record ServerboundAnimationPacket(int handId) implements ServerboundPacket {

    public ServerboundAnimationPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(VAR_INT)
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.ANIMATION;
    }
}