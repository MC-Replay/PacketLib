package mc.replay.packetlib.network.packet.serverbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ServerboundPlayerPositionAndRotationPacket(double x, double y, double z, float yaw, float pitch,
                                                         boolean onGround) implements ServerboundPacket {

    public ServerboundPlayerPositionAndRotationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(BOOLEAN)
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.PLAYER_POSITION_AND_ROTATION;
    }
}