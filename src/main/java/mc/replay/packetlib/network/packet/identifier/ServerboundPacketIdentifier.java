package mc.replay.packetlib.network.packet.identifier;

import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum ServerboundPacketIdentifier implements PacketIdentifier {

    SERVERBOUND_ANIMATION((version) -> switch (version) {
        case MINECRAFT_1_16_5 -> 0x2C;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    });

    private final Function<ProtocolVersion, Integer> identifierFunction;

    ServerboundPacketIdentifier(Function<ProtocolVersion, Integer> identifierFunction) {
        this.identifierFunction = identifierFunction;
    }

    @Override
    public int getIdentifier(@NotNull ProtocolVersion protocolVersion) {
        return this.identifierFunction.apply(protocolVersion);
    }

    private static final Map<Integer, ServerboundPacketIdentifier> PACKET_IDENTIFIER_MAP = new HashMap<>();

    static {
        for (ServerboundPacketIdentifier packetIdentifier : values()) {
            try {
                PACKET_IDENTIFIER_MAP.put(packetIdentifier.getIdentifier(), packetIdentifier);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    public static @Nullable ServerboundPacketIdentifier getPacketIdentifier(int identifier) {
        return PACKET_IDENTIFIER_MAP.get(identifier);
    }
}