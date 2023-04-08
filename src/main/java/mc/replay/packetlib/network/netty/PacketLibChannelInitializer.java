package mc.replay.packetlib.network.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.user.ConnectionPlayerProvider;

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

    private final PacketLib packetLib;
    private final ChannelInitializer<Channel> original;

    public PacketLibChannelInitializer(PacketLib packetLib, ChannelInitializer<Channel> original) {
        this.packetLib = packetLib;
        this.original = original;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        INIT_CHANNEL_METHOD.invoke(this.original, channel);

        afterChannelInitialize(this.packetLib, channel);
    }

    @SuppressWarnings("rawtypes")
    static void afterChannelInitialize(PacketLib packetLib, Channel channel) {
        ConnectionPlayerProvider connectionPlayerProvider = new ConnectionPlayerProvider();

        MessageToByteEncoder encoder = new PacketLibEncoder(packetLib, connectionPlayerProvider, (MessageToByteEncoder) channel.pipeline().get("encoder"));
        ByteToMessageDecoder decoder = new PacketLibDecoder(packetLib, connectionPlayerProvider, (ByteToMessageDecoder) channel.pipeline().get("decoder"));

        channel.pipeline().replace("encoder", "encoder", encoder);
        channel.pipeline().replace("decoder", "decoder", decoder);
    }
}
