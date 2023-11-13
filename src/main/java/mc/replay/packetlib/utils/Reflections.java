package mc.replay.packetlib.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class Reflections {

    private Reflections() {
    }

    // NMS
    public static Class<?> MINECRAFT_SERVER;
    public static Class<?> SERVER_CONNECTION;
    public static Class<?> ENTITY_PLAYER;
    public static Class<?> PLAYER_CONNECTION;
    public static Class<?> NETWORK_MANAGER;

    public static MethodHandle GET_PLAYER_HANDLE_METHOD;
    public static Method GET_SERVER_CONNECTION_METHOD;

    public static Field PLAYER_CONNECTION_FIELD;
    public static Field NETWORK_MANAGER_FIELD;
    public static Field NETWORK_CHANNEL_FIELD;

    public static Object MINECRAFT_SERVER_INSTANCE;

    private static Method DECODE_METHOD;
    private static Method ENCODE_METHOD;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            MINECRAFT_SERVER = ReflectionUtils.nmsClass("server", "MinecraftServer");
            SERVER_CONNECTION = ReflectionUtils.nmsClass("server.network", "ServerConnection");

            MINECRAFT_SERVER_INSTANCE = MINECRAFT_SERVER.getMethod("getServer").invoke(null);
            GET_SERVER_CONNECTION_METHOD = ReflectionUtils.getMethod(MINECRAFT_SERVER, SERVER_CONNECTION);

            ENTITY_PLAYER = ReflectionUtils.nmsClass("server.level", "EntityPlayer");
            PLAYER_CONNECTION = ReflectionUtils.nmsClass("server.network", "PlayerConnection");
            NETWORK_MANAGER = ReflectionUtils.nmsClass("network", "NetworkManager");

            Class<?> craftPlayerClass = ReflectionUtils.obcClass("entity.CraftPlayer");
            GET_PLAYER_HANDLE_METHOD = lookup.findVirtual(craftPlayerClass, "getHandle", MethodType.methodType(ENTITY_PLAYER));

            PLAYER_CONNECTION_FIELD = ReflectionUtils.findFieldEquals(ENTITY_PLAYER, PLAYER_CONNECTION);
            NETWORK_MANAGER_FIELD = ReflectionUtils.findFieldEquals(PLAYER_CONNECTION, NETWORK_MANAGER);
            NETWORK_CHANNEL_FIELD = ReflectionUtils.findFieldEquals(NETWORK_MANAGER, Channel.class);

            DECODE_METHOD = ReflectionUtils.getMethod(ByteToMessageDecoder.class, "decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
            ENCODE_METHOD = ReflectionUtils.getMethod(MessageToByteEncoder.class, "encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static Channel getPacketChannel(@NotNull Player player) {
        try {
            Object entityPlayer = getEntityPlayer(player);
            Object playerConnection = PLAYER_CONNECTION_FIELD.get(entityPlayer);
            Object networkManager = NETWORK_MANAGER_FIELD.get(playerConnection);
            return (Channel) NETWORK_CHANNEL_FIELD.get(networkManager);
        } catch (Throwable throwable) {
            return null;
        }
    }

    public static Object getEntityPlayer(@NotNull Player player) throws Throwable {
        return GET_PLAYER_HANDLE_METHOD.invoke(player);
    }

    public static List<Object> callDecode(ByteToMessageDecoder decoder, ChannelHandlerContext ctx, Object input) throws InvocationTargetException {
        List<Object> output = new ArrayList<>();

        try {
            DECODE_METHOD.invoke(decoder, ctx, input, output);
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return output;
    }

    @SuppressWarnings("rawtypes")
    public static void callEncode(MessageToByteEncoder encoder, ChannelHandlerContext ctx, Object msg, ByteBuf output) throws InvocationTargetException {
        try {
            ENCODE_METHOD.invoke(encoder, ctx, msg, output);
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }
}