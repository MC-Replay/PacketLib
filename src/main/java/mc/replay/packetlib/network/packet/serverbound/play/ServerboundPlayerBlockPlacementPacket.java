package mc.replay.packetlib.network.packet.serverbound.play;

import mc.replay.packetlib.data.entity.PlayerHand;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ServerboundPlayerBlockPlacementPacket(@NotNull PlayerHand hand, @NotNull Vector blockPosition,
                                                    @NotNull BlockFace blockFace, float cursorPositionX,
                                                    float cursorPositionY, float cursorPositionZ, boolean insideBlock,
                                                    int sequence) implements ServerboundPacket {

    public ServerboundPlayerBlockPlacementPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.readEnum(PlayerHand.class),
                reader.read(BLOCK_POSITION),
                reader.read(BLOCK_FACE),
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(BOOLEAN),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19))
                        ? reader.read(VAR_INT)
                        : -1
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.PLAYER_BLOCK_PLACEMENT;
    }
}