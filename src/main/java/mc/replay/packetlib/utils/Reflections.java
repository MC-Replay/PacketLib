package mc.replay.packetlib.utils;

import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

@ApiStatus.Internal
public final class Reflections {

    private Reflections() {
    }

    // NMS
    private static Class<?> PACKET;
    private static Class<?> ENTITY_PLAYER;
    private static Class<?> PLAYER_CONNECTION;
    private static Class<?> PACKET_DATA_SERIALIZER;

    // OBC
    private static Class<?> CRAFT_PLAYER;

    private static MethodHandle GET_PLAYER_HANDLE_METHOD;
    private static MethodHandle GET_PLAYER_CONNECTION_METHOD;
    private static MethodHandle SEND_PACKET_METHOD;

    private static Method PACKET_FROM_ID_METHOD_754;
    private static Method PACKET_FROM_ID_METHOD_760;
    private static Method ID_FROM_PACKET_METHOD;
    private static Method SERIALIZE_PACKET_METHOD;

    private static Constructor<?> PACKET_DATA_SERIALIZER_CONSTRUCTOR;

    private static Object SERVERBOUND_PROTOCOL_DIRECTION;
    private static Object CLIENTBOUND_PROTOCOL_DIRECTION;
    private static Object PLAY_ENUM_PROTOCOL;

    private static ProtocolVersion VERSION;

    static {
        try {
            VERSION = ProtocolVersion.getServerVersion();

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            PACKET = ReflectionUtils.nmsClass("network.protocol", "Packet");
            ENTITY_PLAYER = ReflectionUtils.nmsClass("server.level", "EntityPlayer");
            PLAYER_CONNECTION = ReflectionUtils.nmsClass("server.network", "PlayerConnection");
            PACKET_DATA_SERIALIZER = ReflectionUtils.nmsClass("network", "PacketDataSerializer");

            CRAFT_PLAYER = ReflectionUtils.obcClass("entity.CraftPlayer");

            Field playerConnectionField = Arrays.stream(ENTITY_PLAYER.getFields())
                    .filter(field -> field.getType().isAssignableFrom(PLAYER_CONNECTION))
                    .findFirst().orElseThrow(NoSuchFieldException::new);

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

            Class<?> enumProtocol = ReflectionUtils.nmsClass("network.protocol", "EnumProtocol");
            PLAY_ENUM_PROTOCOL = enumProtocol.getMethod("a", int.class).invoke(null, 0);

            if (VERSION.isEqual(ProtocolVersion.MINECRAFT_1_16_5)) {
                PACKET_FROM_ID_METHOD_754 = PLAY_ENUM_PROTOCOL.getClass().getMethod("a", enumProtocolDirection, int.class);
            } else {
                PACKET_FROM_ID_METHOD_760 = PLAY_ENUM_PROTOCOL.getClass().getMethod("a", enumProtocolDirection, int.class, PACKET_DATA_SERIALIZER);
            }

            ID_FROM_PACKET_METHOD = PLAY_ENUM_PROTOCOL.getClass().getMethod("a", enumProtocolDirection, PACKET);
            SERIALIZE_PACKET_METHOD = PACKET.getMethod("b", PACKET_DATA_SERIALIZER);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object entityPlayer = GET_PLAYER_HANDLE_METHOD.invoke(player);
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
}