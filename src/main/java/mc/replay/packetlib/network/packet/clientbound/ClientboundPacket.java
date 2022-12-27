package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.Packet;
import org.jetbrains.annotations.NotNull;

public interface ClientboundPacket extends Packet<ClientboundPacketIdentifier> {

    void write(@NotNull ReplayByteBuffer writer);
}