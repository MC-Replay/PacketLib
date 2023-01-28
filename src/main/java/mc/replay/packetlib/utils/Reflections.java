package mc.replay.packetlib.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public final class Reflections {

    private Reflections() {
    }

    // NMS
    public static Class<?> PACKET;
    public static Class<?> ENTITY_PLAYER;
    public static Class<?> PLAYER_CONNECTION;
    public static Class<?> PACKET_DATA_SERIALIZER;
    public static Class<?> NETWORK_MANAGER;

    public static MethodHandle GET_PLAYER_HANDLE_METHOD;
    public static MethodHandle GET_PLAYER_CONNECTION_METHOD;
    public static MethodHandle SEND_PACKET_METHOD;

    public static Field NETWORK_MANAGER_FIELD;
    public static Field NETWORK_CHANNEL_FIELD;

    public static Method PACKET_FROM_ID_METHOD_754;
    public static Method PACKET_FROM_ID_METHOD_760;
    public static Method ID_FROM_PACKET_METHOD;
    public static Method SERIALIZE_PACKET_METHOD;

    public static Constructor<?> PACKET_DATA_SERIALIZER_CONSTRUCTOR;

    public static Object SERVERBOUND_PROTOCOL_DIRECTION;
    public static Object CLIENTBOUND_PROTOCOL_DIRECTION;
    public static Object PLAY_ENUM_PROTOCOL;

    public static ProtocolVersion VERSION;

    static {
        try {
            VERSION = ProtocolVersion.getServerVersion();

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            PACKET = ReflectionUtils.nmsClass("network.protocol", "Packet");
            ENTITY_PLAYER = ReflectionUtils.nmsClass("server.level", "EntityPlayer");
            PLAYER_CONNECTION = ReflectionUtils.nmsClass("server.network", "PlayerConnection");
            PACKET_DATA_SERIALIZER = ReflectionUtils.nmsClass("network", "PacketDataSerializer");
            NETWORK_MANAGER = ReflectionUtils.nmsClass("network", "NetworkManager");

            Class<?> CRAFT_PLAYER = ReflectionUtils.obcClass("entity.CraftPlayer");

            Field playerConnectionField = ReflectionUtils.findFieldAssignable(ENTITY_PLAYER, PLAYER_CONNECTION);

            Method sendPacketMethod = Arrays.stream(PLAYER_CONNECTION.getMethods())
                    .filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0] == PACKET)
                    .findFirst().orElseThrow(NoSuchMethodException::new);

            GET_PLAYER_HANDLE_METHOD = lookup.findVirtual(CRAFT_PLAYER, "getHandle", MethodType.methodType(ENTITY_PLAYER));
            GET_PLAYER_CONNECTION_METHOD = lookup.unreflectGetter(playerConnectionField);
            SEND_PACKET_METHOD = lookup.unreflect(sendPacketMethod);

            PACKET_DATA_SERIALIZER_CONSTRUCTOR = PACKET_DATA_SERIALIZER.getConstructor(ByteBuf.class);

            Class<?> enumProtocolDirection = ReflectionUtils.nmsClass("network.protocol", "EnumProtocolDirection");
            SERVERBOUND_PROTOCOL_DIRECTION = Enum.valueOf(enumProtocolDirection.asSubclass(Enum.class), "SERVERBOUND");
            CLIENTBOUND_PROTOCOL_DIRECTION = Enum.valueOf(enumProtocolDirection.asSubclass(Enum.class), "CLIENTBOUND");

            Class<?> enumProtocol = ReflectionUtils.nmsClass("network", "EnumProtocol");
            PLAY_ENUM_PROTOCOL = enumProtocol.getMethod("a", int.class).invoke(null, 0);

            NETWORK_MANAGER_FIELD = ReflectionUtils.findFieldEquals(ENTITY_PLAYER, NETWORK_MANAGER);
            NETWORK_CHANNEL_FIELD = ReflectionUtils.findFieldEquals(NETWORK_MANAGER, Channel.class);

            if (VERSION.isEqual(ProtocolVersion.MINECRAFT_1_16_5)) {
                PACKET_FROM_ID_METHOD_754 = PLAY_ENUM_PROTOCOL.getClass().getMethod("a", enumProtocolDirection, int.class);
            } else {
                PACKET_FROM_ID_METHOD_760 = PLAY_ENUM_PROTOCOL.getClass().getMethod("a", enumProtocolDirection, int.class, PACKET_DATA_SERIALIZER);
            }

            ID_FROM_PACKET_METHOD = PLAY_ENUM_PROTOCOL.getClass().getMethod("a", enumProtocolDirection, PACKET);
            if (VERSION.isLower(ProtocolVersion.MINECRAFT_1_18)) {
                SERIALIZE_PACKET_METHOD = PACKET.getMethod("b", PACKET_DATA_SERIALIZER);
            } else {
                SERIALIZE_PACKET_METHOD = PACKET.getMethod("a", PACKET_DATA_SERIALIZER);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object entityPlayer = getEntityPlayer(player);
            Object playerConnection = GET_PLAYER_CONNECTION_METHOD.invoke(entityPlayer);
            SEND_PACKET_METHOD.invoke(playerConnection, packet);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static @Nullable Object getClientboundPacket(@NotNull ByteBuf buffer, int packetId) {
        try {
            Object dataSerializer = createPacketDataSerializer(buffer);

            if (VERSION.isEqual(ProtocolVersion.MINECRAFT_1_16_5)) {
                Object packet = PACKET_FROM_ID_METHOD_754.invoke(PLAY_ENUM_PROTOCOL, CLIENTBOUND_PROTOCOL_DIRECTION, packetId);
                PACKET.getMethod("a", PACKET_DATA_SERIALIZER).invoke(packet, dataSerializer);
                return packet;
            } else {
                return PACKET_FROM_ID_METHOD_760.invoke(PLAY_ENUM_PROTOCOL, CLIENTBOUND_PROTOCOL_DIRECTION, packetId, dataSerializer);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static @Nullable Integer getClientboundPacketId(@NotNull Object minecraftPacket) {
        if (!PACKET.isAssignableFrom(minecraftPacket.getClass())) return null;

        try {
            return (Integer) ID_FROM_PACKET_METHOD.invoke(PLAY_ENUM_PROTOCOL, CLIENTBOUND_PROTOCOL_DIRECTION, minecraftPacket);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static @Nullable Integer getServerboundPacketId(@NotNull Object minecraftPacket) {
        if (!PACKET.isAssignableFrom(minecraftPacket.getClass())) return null;

        try {
            return (Integer) ID_FROM_PACKET_METHOD.invoke(PLAY_ENUM_PROTOCOL, SERVERBOUND_PROTOCOL_DIRECTION, minecraftPacket);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static Object createPacketDataSerializer(ByteBuf buffer) throws Exception {
        return PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(buffer);
    }

    public static void serializePacket(Object minecraftPacket, Object packetDataSerializer) {
        try {
            SERIALIZE_PACKET_METHOD.invoke(minecraftPacket, packetDataSerializer);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static Channel getPacketChannel(@NotNull Player player) {
        try {
            Object entityPlayer = getEntityPlayer(player);
            Object networkManager = NETWORK_MANAGER_FIELD.get(entityPlayer);
            return (Channel) NETWORK_CHANNEL_FIELD.get(networkManager);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static Object getEntityPlayer(@NotNull Player player) throws Throwable {
        return GET_PLAYER_HANDLE_METHOD.invoke(player);
    }
}