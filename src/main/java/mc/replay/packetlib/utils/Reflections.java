package mc.replay.packetlib.utils;

import com.google.common.collect.ForwardingMultimap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import mc.replay.packetlib.data.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Reflections {

    private Reflections() {
    }

    // NMS
    private static Class<?> PACKET;
    private static Class<?> ENTITY_PLAYER;
    private static Class<?> PLAYER_CONNECTION;
    private static Class<?> PACKET_DATA_SERIALIZER;
    private static Class<?> NETWORK_MANAGER;

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

    private static Field GAME_PROFILE_SKULL_META_FIELD;
    private static Field GAME_PROFILE_UUID_FIELD;
    private static Field GAME_PROFILE_NAME_FIELD;
    private static Field GAME_PROFILE_PROPERTIES_FIELD;
    private static Field PROPERTY_NAME_FIELD;
    private static Field PROPERTY_VALUE_FIELD;
    private static Field PROPERTY_SIGNATURE_FIELD;

    private static ProtocolVersion VERSION;

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

            Class<?> CRAFT_META_SKULL = ReflectionUtils.obcClass("inventory.CraftMetaSkull");
            Class<?> GAME_PROFILE = ReflectionUtils.getClass("com.mojang.authlib.GameProfile");
            Class<?> PROPERTY = ReflectionUtils.getClass("com.mojang.authlib.properties.Property");

            GAME_PROFILE_SKULL_META_FIELD = ReflectionUtils.getField(CRAFT_META_SKULL, "profile");
            GAME_PROFILE_UUID_FIELD = ReflectionUtils.getField(GAME_PROFILE, "id");
            GAME_PROFILE_NAME_FIELD = ReflectionUtils.getField(GAME_PROFILE, "name");
            GAME_PROFILE_PROPERTIES_FIELD = ReflectionUtils.getField(GAME_PROFILE, "properties");
            PROPERTY_NAME_FIELD = ReflectionUtils.getField(PROPERTY, "name");
            PROPERTY_VALUE_FIELD = ReflectionUtils.getField(PROPERTY, "value");
            PROPERTY_SIGNATURE_FIELD = ReflectionUtils.getField(PROPERTY, "signature");
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
            Object networkManager = ENTITY_PLAYER.getField("networkManager").get(entityPlayer);

            Object channel;
            if (VERSION.isLowerOrEqual(ProtocolVersion.MINECRAFT_1_17_1)) {
                channel = NETWORK_MANAGER.getField("channel").get(networkManager);
            } else if (VERSION.isEqual(ProtocolVersion.MINECRAFT_1_18_1)) {
                channel = NETWORK_MANAGER.getField("k").get(networkManager);
            } else {
                channel = NETWORK_MANAGER.getField("m").get(networkManager);
            }

            return (Channel) channel;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static PlayerProfile getGameProfile(@NotNull Object gameProfile) {
        try {
            UUID uuid = (UUID) GAME_PROFILE_UUID_FIELD.get(gameProfile);
            String name = (String) GAME_PROFILE_NAME_FIELD.get(gameProfile);
            ForwardingMultimap<String, Object> properties = (ForwardingMultimap<String, Object>) GAME_PROFILE_PROPERTIES_FIELD.get(gameProfile);
            Map<String, PlayerProfile.Property> propertyMap = new HashMap<>();

            for (Map.Entry<String, Object> entry : properties.entries()) {
                propertyMap.put(entry.getKey(), getPropertyFromPropertyObject(entry.getValue()));
            }

            return new PlayerProfile(uuid, name, propertyMap);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static PlayerProfile getGameProfile(@NotNull SkullMeta skullMeta) {
        try {
            return getGameProfile(GAME_PROFILE_SKULL_META_FIELD.get(skullMeta));
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private static PlayerProfile.Property getPropertyFromPropertyObject(@NotNull Object propertyObject) throws IllegalAccessException {
        String name = (String) PROPERTY_NAME_FIELD.get(propertyObject);
        String value = (String) PROPERTY_VALUE_FIELD.get(propertyObject);
        String signature = (String) PROPERTY_SIGNATURE_FIELD.get(propertyObject);
        return new PlayerProfile.Property(name, value, signature);
    }

    private static Object getEntityPlayer(@NotNull Player player) throws Throwable {
        return GET_PLAYER_HANDLE_METHOD.invoke(player);
    }
}