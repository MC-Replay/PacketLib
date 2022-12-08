package mc.replay.packetlib.network.netty;

import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

public final class PacketLibInjector {

    public void inject(@NotNull Channel channel) {
        channel.pipeline().addBefore("packet_handler", "packetlib_listener", new PacketLibListener());
    }
}