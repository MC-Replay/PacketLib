package mc.replay.packetlib.network.packet.serverbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.BOOLEAN;
import static mc.replay.packetlib.network.ReplayByteBuffer.FLOAT;

public record ServerboundPlayerRotationPacket(float yaw, float pitch, boolean onGround) implements ServerboundPacket {

    public ServerboundPlayerRotationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(BOOLEAN)
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.PLAYER_ROTATION;
    }
}