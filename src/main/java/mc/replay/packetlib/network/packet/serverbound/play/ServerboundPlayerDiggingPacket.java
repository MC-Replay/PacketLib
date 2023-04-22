package mc.replay.packetlib.network.packet.serverbound.play;

import mc.replay.packetlib.data.entity.player.DiggingStatus;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ServerboundPlayerDiggingPacket(@NotNull DiggingStatus status, @NotNull Vector blockPosition,
                                             @NotNull BlockFace blockFace,
                                             int sequence) implements ServerboundPacket {

    public ServerboundPlayerDiggingPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.readEnum(DiggingStatus.class),
                reader.read(BLOCK_POSITION),
                reader.read(BLOCK_FACE),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19))
                        ? reader.read(VAR_INT)
                        : -1
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.PLAYER_DIGGING;
    }
}