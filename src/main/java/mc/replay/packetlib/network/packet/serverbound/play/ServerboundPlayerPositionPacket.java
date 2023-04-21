package mc.replay.packetlib.network.packet.serverbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.BOOLEAN;
import static mc.replay.packetlib.network.ReplayByteBuffer.DOUBLE;

public record ServerboundPlayerPositionPacket(double x, double y, double z,
                                              boolean onGround) implements ServerboundPacket {

    public ServerboundPlayerPositionPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(BOOLEAN)
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.PLAYER_POSITION;
    }
}