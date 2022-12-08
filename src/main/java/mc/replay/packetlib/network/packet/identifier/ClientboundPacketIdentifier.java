package mc.replay.packetlib.network.packet.identifier;

import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static mc.replay.packetlib.utils.ProtocolVersion.MINECRAFT_1_16_5;
import static mc.replay.packetlib.utils.ProtocolVersion.MINECRAFT_1_19_2;

public enum ClientboundPacketIdentifier implements PacketIdentifier {

    CLIENTBOUND_SPAWN_ENTITY((version) -> 0x00),

    CLIENTBOUND_SPAWN_EXPERIENCE_ORB((version) -> 0x01),

    CLIENTBOUND_SPAWN_PLAYER((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x02;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18,
                MINECRAFT_1_17_1, MINECRAFT_1_17, MINECRAFT_1_16_5 -> 0x04;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_ENTITY_ANIMATION((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x03;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x06;
        case MINECRAFT_1_16_5 -> 0x05;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_STATISTIC((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x04;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x07;
        case MINECRAFT_1_16_5 -> 0x06;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_ACKNOWLEDGE_BLOCK_CHANGE((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x05;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x08;
        case MINECRAFT_1_16_5 -> 0x07;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_BLOCK_BREAK_ANIMATION((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x06;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x09;
        case MINECRAFT_1_16_5 -> 0x08;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_BLOCK_ENTITY_DATA((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x07;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x0A;
        case MINECRAFT_1_16_5 -> 0x09;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_BLOCK_ACTION((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x08;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x0B;
        case MINECRAFT_1_16_5 -> 0x0A;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_BLOCK_CHANGE((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x09;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x0C;
        case MINECRAFT_1_16_5 -> 0x0B;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_BOSS_BAR((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x0A;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x0D;
        case MINECRAFT_1_16_5 -> 0x0C;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_SERVER_DIFFICULTY((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x0B;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x0E;
        case MINECRAFT_1_16_5 -> 0x0D;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_CHAT_PREVIEW((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x0C;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_CLEAR_TITLES((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x0D;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x10;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_TAB_COMPLETE((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x0E;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x11;
        case MINECRAFT_1_16_5 -> 0x0F;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_DECLARE_COMMANDS((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x0F;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x12;
        case MINECRAFT_1_16_5 -> 0x10;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_WINDOW_CONFIRMATION((version) -> {
        if (version.isEqual(MINECRAFT_1_16_5)) return 0x11;
        throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_CLOSE_WINDOW((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x10;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x13;
        case MINECRAFT_1_16_5 -> 0x12;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_WINDOW_ITEMS((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x11;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x14;
        case MINECRAFT_1_16_5 -> 0x13;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_WINDOW_PROPERTY((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x12;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x15;
        case MINECRAFT_1_16_5 -> 0x14;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_SET_SLOT((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x13;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x16;
        case MINECRAFT_1_16_5 -> 0x15;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_SET_COOLDOWN((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x14;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x17;
        case MINECRAFT_1_16_5 -> 0x16;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_CUSTOM_CHAT_COMPLETIONS((version) -> {
        if (version.isEqual(MINECRAFT_1_19_2)) return 0x15;
        throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_PLUGIN_MESSAGE((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x16;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x15;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x18;
        case MINECRAFT_1_16_5 -> 0x17;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_NAMED_SOUND_EFFECT((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x17;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x16;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x19;
        case MINECRAFT_1_16_5 -> 0x18;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_DELETE_CHAT_MESSAGE((version) -> {
        if (version.isEqual(MINECRAFT_1_19_2)) return 0x18;
        throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_DISCONNECT((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_16_5 -> 0x19;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x17;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x1A;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_ENTITY_STATUS((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_16_5 -> 0x1A;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x18;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x1B;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_EXPLOSION((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_16_5 -> 0x1B;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x19;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x1C;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_UNLOAD_CHUNK((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_16_5 -> 0x1C;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x1A;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x1D;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_CHANGE_GAME_STATE((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_16_5 -> 0x1D;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x1B;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x1E;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_OPEN_HORSE_WINDOW((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_16_5 -> 0x1E;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x1C;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x1F;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_INITIALIZE_WORLD_BORDER((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x1F;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x1D;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x20;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_WORLD_BORDER((version) -> {
        if (version.isEqual(MINECRAFT_1_16_5)) return 0x3D;
        throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_KEEP_ALIVE((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x20;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x1E;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x21;
        case MINECRAFT_1_16_5 -> 0x1F;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_CHUNK_DATA((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x21;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x1F;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x22;
        case MINECRAFT_1_16_5 -> 0x20;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_EFFECT((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x22;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x20;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x23;
        case MINECRAFT_1_16_5 -> 0x21;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_PARTICLE((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x23;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x21;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x24;
        case MINECRAFT_1_16_5 -> 0x22;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_UPDATE_LIGHT((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x24;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x22;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x25;
        case MINECRAFT_1_16_5 -> 0x23;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_JOIN_GAME((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x25;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x23;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x26;
        case MINECRAFT_1_16_5 -> 0x24;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_MAP_DATA((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x26;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x24;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x27;
        case MINECRAFT_1_16_5 -> 0x25;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_TRADE_LIST((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x27;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x25;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x28;
        case MINECRAFT_1_16_5 -> 0x26;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_ENTITY_POSITION((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x28;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x26;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x29;
        case MINECRAFT_1_16_5 -> 0x27;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_ENTITY_POSITION_AND_ROTATION((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x29;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x27;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x2A;
        case MINECRAFT_1_16_5 -> 0x28;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_ENTITY_ROTATION((version) -> switch (version) {
        case MINECRAFT_1_19_2 -> 0x2A;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x28;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x2B;
        case MINECRAFT_1_16_5 -> 0x29;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    }),

    CLIENTBOUND_VEHICLE_MOVE((version) -> switch (version) {
        case MINECRAFT_1_19_2, MINECRAFT_1_16_5 -> 0x2B;
        case MINECRAFT_1_19_1, MINECRAFT_1_19 -> 0x29;
        case MINECRAFT_1_18_2, MINECRAFT_1_18_1, MINECRAFT_1_18, MINECRAFT_1_17_1, MINECRAFT_1_17 -> 0x2C;
        default -> throw new IllegalStateException("Unexpected value: " + version);
    });

    //    ENTITY_TELEPORT((version) -> switch (version) {
    //        case MINECRAFT_1_16_5 -> 0x55;
    //        case MINECRAFT_1_19_2, MINECRAFT_1_19_1 -> 0x66;
    //        default -> throw new IllegalStateException("Unexpected value: " + version);
    //    });

    private final Function<ProtocolVersion, Integer> identifierFunction;

    ClientboundPacketIdentifier(Function<ProtocolVersion, Integer> identifierFunction) {
        this.identifierFunction = identifierFunction;
    }

    @Override
    public int getIdentifier(@NotNull ProtocolVersion protocolVersion) {
        return this.identifierFunction.apply(protocolVersion);
    }

    private static final Map<Integer, ClientboundPacketIdentifier> PACKET_IDENTIFIER_MAP = new HashMap<>();

    static {
        for (ClientboundPacketIdentifier packetIdentifier : values()) {
            try {
                PACKET_IDENTIFIER_MAP.put(packetIdentifier.getIdentifier(), packetIdentifier);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    public static @Nullable ClientboundPacketIdentifier getPacketIdentifier(int identifier) {
        return PACKET_IDENTIFIER_MAP.get(identifier);
    }
}