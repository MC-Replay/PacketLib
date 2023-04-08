package mc.replay.packetlib.network.user;

import io.netty.channel.Channel;
import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.netty.PacketLibEncoder;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PacketLibConnectionHandler implements Listener {

    public PacketLibConnectionHandler(PacketLib packetLib) {
        Bukkit.getServer().getPluginManager().registerEvents(this, packetLib.getJavaPlugin());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Channel channel = Reflections.getPacketChannel(player);
        if (channel == null || !channel.isOpen()) return;

        ConnectionPlayerProvider userConnection = this.getUserConnection(channel);
        if (userConnection == null) return;

        userConnection.player(player);
    }

    private ConnectionPlayerProvider getUserConnection(Channel channel) {
        PacketLibEncoder encoder = channel.pipeline().get(PacketLibEncoder.class);
        return encoder != null ? encoder.connection() : null;
    }
}