package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.FLOAT;
import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public record ClientboundUpdateHealthPacket(float health, int food, float foodSaturation) implements ClientboundPacket {

    public ClientboundUpdateHealthPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(FLOAT),
                reader.read(VAR_INT),
                reader.read(FLOAT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(FLOAT, this.health);
        writer.write(VAR_INT, this.food);
        writer.write(FLOAT, this.foodSaturation);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.UPDATE_HEALTH;
    }
}