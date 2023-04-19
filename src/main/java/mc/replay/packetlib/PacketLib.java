package mc.replay.packetlib;

import mc.replay.packetlib.network.PacketListener;
import mc.replay.packetlib.network.PacketRegistry;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifierLoader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface PacketLib {

    static @NotNull PacketLibBuilder builder() {
        return new PacketLibBuilderImpl();
    }

    static @NotNull PacketLib getInstance(@NotNull JavaPlugin javaPlugin) {
        return PacketLibImpl.getInstance(javaPlugin);
    }

    static boolean hasInstance(@NotNull JavaPlugin javaPlugin) {
        return PacketLibImpl.hasInstance(javaPlugin);
    }

    static @NotNull PacketIdentifierLoader getPacketIdentifierLoader() {
        return PacketLibImpl.PACKET_IDENTIFIER_LOADER;
    }

    static @NotNull PacketRegistry getPacketRegistry() {
        return PacketLibImpl.PACKET_REGISTRY;
    }

    @NotNull JavaPlugin javaPlugin();

    @NotNull PacketLibSettings settings();

    @NotNull PacketListener packetListener();

    void inject(@NotNull Player player);

    void uninject(@NotNull Player player);

    void sendPacket(@NotNull Player player, @NotNull ClientboundPacket packet);
}