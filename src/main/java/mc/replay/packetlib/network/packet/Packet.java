package mc.replay.packetlib.network.packet;

import mc.replay.packetlib.network.packet.identifier.PacketIdentifier;
import org.jetbrains.annotations.NotNull;

public interface Packet<I extends PacketIdentifier> {

    @NotNull I identifier();
}