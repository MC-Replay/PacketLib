package mc.replay.packetlib.network.packet;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

public interface ClientboundPacket extends Packet<ClientboundPacketIdentifier> {

    void write(@NotNull PacketBuffer writer);
}