package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public record ClientboundSetPassengersPacket(int vehicleEntityId,
                                             @NotNull List<Integer> passengerIds) implements ClientboundPacket {

    public ClientboundSetPassengersPacket {
        passengerIds = List.copyOf(passengerIds);
    }

    public ClientboundSetPassengersPacket(@NotNull ReplayByteBuffer buffer) {
        this(
                buffer.read(VAR_INT),
                buffer.readCollection(VAR_INT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.vehicleEntityId);
        writer.writeCollection(VAR_INT, this.passengerIds);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.SET_PASSENGERS;
    }
}