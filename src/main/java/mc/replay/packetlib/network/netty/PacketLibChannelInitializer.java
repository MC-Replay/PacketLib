package mc.replay.packetlib.network.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.user.ConnectionPlayerProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

final class PacketLibChannelInitializer extends ChannelInitializer<Channel> {

    private static final Method INIT_CHANNEL_METHOD;

    static {
        try {
            INIT_CHANNEL_METHOD = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
            INIT_CHANNEL_METHOD.setAccessible(true);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    private final PacketLib instance;
    private final ChannelInitializer<Channel> original;

    public PacketLibChannelInitializer(PacketLib instance, ChannelInitializer<Channel> original) {
        this.instance = instance;
        this.original = original;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        INIT_CHANNEL_METHOD.invoke(this.original, channel);

        afterChannelInitialize(this.instance, channel, null);
    }

    @SuppressWarnings("rawtypes")
    static void afterChannelInitialize(PacketLib instance, Channel channel, @Nullable Player player) {
        ConnectionPlayerProvider connectionPlayerProvider = new ConnectionPlayerProvider(player);

        MessageToByteEncoder currentEncoder = (MessageToByteEncoder) channel.pipeline().get("encoder");
        ByteToMessageDecoder currentDecoder = (ByteToMessageDecoder) channel.pipeline().get("decoder");

        // If another plugin has already initialized PacketLib for this player, we can just add an instance to the existing encoders and decoders.
        if (currentEncoder instanceof PacketLibEncoder packetLibEncoder && currentDecoder instanceof PacketLibDecoder packetLibDecoder) {
            packetLibEncoder.addInstance(instance);
            packetLibDecoder.addInstance(instance);
            return;
        }

        MessageToByteEncoder newEncoder = new PacketLibEncoder(instance, connectionPlayerProvider, currentEncoder);
        ByteToMessageDecoder newDecoder = new PacketLibDecoder(instance, connectionPlayerProvider, currentDecoder);

        channel.pipeline().replace("encoder", "encoder", newEncoder);
        channel.pipeline().replace("decoder", "decoder", newDecoder);
    }
}
