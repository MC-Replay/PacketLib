package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundWorldEventPacket(int effectId, @NotNull Vector position, int data,
                                          boolean disableRelativeVolume) implements ClientboundPacket {

    public ClientboundWorldEventPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(INT),
                reader.read(BLOCK_POSITION),
                reader.read(INT),
                reader.read(BOOLEAN)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(INT, this.effectId);
        writer.write(BLOCK_POSITION, this.position);
        writer.write(INT, this.data);
        writer.write(BOOLEAN, this.disableRelativeVolume);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.WORLD_EVENT;
    }
}