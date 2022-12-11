package mc.replay.packetlib.network.netty;

import io.netty.channel.Channel;
import mc.replay.packetlib.PacketLib;
import org.jetbrains.annotations.NotNull;

public final class PacketLibInjector {

    private final PacketLib packetLib;

    public PacketLibInjector(PacketLib packetLib) {
        this.packetLib = packetLib;
    }

    public void inject(@NotNull Channel channel) {
        channel.pipeline().addBefore("packet_handler", "packetlib_handler", new PacketLibHandler(this.packetLib));
    }
}