package mc.replay.packetlib.network.netty;

import io.netty.channel.Channel;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PacketLibInjector {

    private final PacketLib packetLib;

    public PacketLibInjector(PacketLib packetLib) {
        this.packetLib = packetLib;
    }

    public void inject(@NotNull Player player, boolean listenForClientbound) {
        Channel channel = Reflections.getPacketChannel(player);
        if (channel == null) {
            throw new IllegalStateException("Couldn't get channel from player '" + player.getUniqueId() + "'");
        }

        channel.pipeline().addBefore("packet_handler", "packetlib_handler", new PacketLibHandler(this.packetLib, player, listenForClientbound));
    }
}