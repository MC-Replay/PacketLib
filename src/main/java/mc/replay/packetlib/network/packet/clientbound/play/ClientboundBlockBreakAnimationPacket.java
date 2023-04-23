package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundBlockBreakAnimationPacket(int entityId, @NotNull Vector blockPosition,
                                                   byte stage) implements ClientboundPacket {

    public ClientboundBlockBreakAnimationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(BLOCK_POSITION),
                reader.read(BYTE)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(BLOCK_POSITION, this.blockPosition);
        writer.write(BYTE, this.stage);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.BLOCK_BREAK_ANIMATION;
    }
}