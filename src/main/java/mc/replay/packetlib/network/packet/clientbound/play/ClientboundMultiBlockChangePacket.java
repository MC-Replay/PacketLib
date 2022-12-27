package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundMultiBlockChangePacket(long chunkSectionPosition, boolean suppressLightUpdates,
                                                long[] blocks) implements ClientboundPacket {

    public ClientboundMultiBlockChangePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(LONG),
                reader.read(BOOLEAN),
                reader.read(VAR_LONG_ARRAY)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(LONG, this.chunkSectionPosition);
        writer.write(BOOLEAN, this.suppressLightUpdates);
        writer.write(VAR_LONG_ARRAY, this.blocks);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.MULTI_BLOCK_CHANGE;
    }
}