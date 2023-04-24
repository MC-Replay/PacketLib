package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.LONG;

public record ClientboundTimeUpdatePacket(long worldAge, long timeOfDay) implements ClientboundPacket {

    public ClientboundTimeUpdatePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(LONG),
                reader.read(LONG)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(LONG, this.worldAge);
        writer.write(LONG, this.timeOfDay);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.TIME_UPDATE;
    }
}