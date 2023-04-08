package mc.replay.packetlib;

import io.netty.channel.Channel;
import mc.replay.packetlib.network.PacketListener;
import mc.replay.packetlib.network.PacketRegistry;
import mc.replay.packetlib.network.netty.PacketLibInjector;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifierLoader;
import mc.replay.packetlib.network.user.PacketLibConnectionHandler;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class PacketLib {

    private static PacketLib instance;

    @ApiStatus.Internal
    public static PacketLib getInstance() {
        return instance;
    }

    private final JavaPlugin javaPlugin;
    private final PacketRegistry packetRegistry;
    private final PacketListener packetListener;
    private final PacketIdentifierLoader packetIdentifierLoader;
    private final PacketLibInjector injector;

    public PacketLib(@NotNull JavaPlugin plugin) {
        instance = this;

        this.javaPlugin = plugin;
        this.packetRegistry = new PacketRegistry(this);
        this.packetListener = new PacketListener();
        this.packetIdentifierLoader = new PacketIdentifierLoader();
        this.injector = new PacketLibInjector(this);

        new PacketLibConnectionHandler(this);
    }

    public void inject() {
        try {
            this.injector.inject();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendPacket(@NotNull Player player, @NotNull ClientboundPacket packet) {
        Channel channel = Reflections.getPacketChannel(player);
        if (channel == null) return;

        if (channel.eventLoop().inEventLoop()) {
            if (!channel.isOpen() || !channel.isActive()) return;

            channel.writeAndFlush(packet).addListener(future -> {
                if (!future.isSuccess()) {
                    future.cause().printStackTrace();
                }
            });
        } else {
            channel.eventLoop().execute(() -> {
                if (!channel.isOpen() || !channel.isActive()) return;

                channel.writeAndFlush(packet).addListener(future -> {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    }
                });
            });
        }
    }

    public @NotNull JavaPlugin getJavaPlugin() {
        return this.javaPlugin;
    }

    public @NotNull PacketRegistry getPacketRegistry() {
        return this.packetRegistry;
    }

    public @NotNull PacketListener getPacketListener() {
        return this.packetListener;
    }

    public @NotNull PacketIdentifierLoader getPacketIdentifierLoader() {
        return this.packetIdentifierLoader;
    }

    public @NotNull PacketLibInjector getInjector() {
        return this.injector;
    }
}