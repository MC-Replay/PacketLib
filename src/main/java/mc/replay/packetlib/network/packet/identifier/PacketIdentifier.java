package mc.replay.packetlib.network.packet.identifier;

import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

public interface PacketIdentifier {

    int getIdentifier(@NotNull ProtocolVersion protocolVersion);

    default int getIdentifier() {
        return this.getIdentifier(ProtocolVersion.getServerVersion());
    }
}