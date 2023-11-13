package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundMultiBlockChangePacket(long chunkSectionPosition, boolean suppressLightUpdates,
                                                long[] blocks) implements ClientboundPacket {

    public ClientboundMultiBlockChangePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(LONG),
                (ProtocolVersion.getServerVersion().isLowerOrEqual(ProtocolVersion.MINECRAFT_1_19_4))
                        ? reader.read(BOOLEAN)
                        : false,
                reader.read(VAR_LONG_ARRAY)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(LONG, this.chunkSectionPosition);

        if (ProtocolVersion.getServerVersion().isLowerOrEqual(ProtocolVersion.MINECRAFT_1_19_4)) {
            writer.write(BOOLEAN, this.suppressLightUpdates);
        }

        writer.write(VAR_LONG_ARRAY, this.blocks);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.MULTI_BLOCK_CHANGE;
    }
}