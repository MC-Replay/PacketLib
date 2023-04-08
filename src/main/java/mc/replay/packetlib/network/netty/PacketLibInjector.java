package mc.replay.packetlib.network.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.List;

public final class PacketLibInjector implements Listener {

    private final PacketLib packetLib;

    public PacketLibInjector(PacketLib packetLib) {
        this.packetLib = packetLib;
    }

    @SuppressWarnings("unchecked")
    public void inject() throws Exception {
        Object serverConnection = Reflections.GET_SERVER_CONNECTION_METHOD.invoke(Reflections.MINECRAFT_SERVER_INSTANCE);
        if (serverConnection == null) {
            throw new RuntimeException("Failed to get server connection");
        }

        ChannelFuture listeningChannel = null;

        for (Field field : Reflections.SERVER_CONNECTION.getDeclaredFields()) {
            if (!List.class.isAssignableFrom(field.getType()) || !field.getGenericType().getTypeName().contains(ChannelFuture.class.getName())) {
                continue;
            }

            field.setAccessible(true);

            List<ChannelFuture> allServerChannels = (List<ChannelFuture>) field.get(serverConnection);

            for (ChannelFuture serverChannel : allServerChannels) {
                listeningChannel = serverChannel;
                break;
            }

            break;
        }

        if (listeningChannel == null) {
            throw new RuntimeException("Failed to find listening channel!");
        }

        this.injectChannel(listeningChannel);
    }

    @SuppressWarnings("unchecked")
    private void injectChannel(ChannelFuture listeningChannel) {
        List<String> names = listeningChannel.channel().pipeline().names();

        ChannelInitializer<Channel> childHandler = null;
        ChannelHandler channelHandler = null;
        Field childHandlerField = null;
        for (String name : names) {
            channelHandler = listeningChannel.channel().pipeline().get(name);

            try {
                childHandlerField = channelHandler.getClass().getDeclaredField("childHandler");
                childHandlerField.setAccessible(true);
                childHandler = (ChannelInitializer<Channel>) childHandlerField.get(channelHandler);
                break;
            } catch (Exception ignored) {
            }
        }

        if (childHandler == null) {
            throw new RuntimeException("Failed to find child handler!");
        }

        try {
            ChannelInitializer<Channel> newChildHandler = new PacketLibChannelInitializer(this.packetLib, childHandler);
            childHandlerField.set(channelHandler, newChildHandler);
            System.out.println("Injected PacketLib into the server!");
        } catch (Exception exception) {
            throw new RuntimeException("Failed to inject PacketLib into the server!", exception);
        }
    }
}