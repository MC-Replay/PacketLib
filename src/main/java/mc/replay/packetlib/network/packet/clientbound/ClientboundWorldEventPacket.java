package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundWorldEventPacket(int effectId, @NotNull Vector position, int data,
                                          boolean disableRelativeVolume) implements ClientboundPacket {

    public ClientboundWorldEventPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(INT),
                reader.read(BLOCK_POSITION),
                reader.read(INT),
                reader.read(BOOLEAN)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
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