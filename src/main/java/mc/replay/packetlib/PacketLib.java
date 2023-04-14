package mc.replay.packetlib;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
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

    private boolean globalInjection = false;

    public PacketLib(@NotNull JavaPlugin plugin) {
        instance = this;

        this.javaPlugin = plugin;
        this.packetRegistry = new PacketRegistry();
        this.packetListener = new PacketListener();
        this.packetIdentifierLoader = new PacketIdentifierLoader();
        this.injector = new PacketLibInjector(this);

        new PacketLibConnectionHandler(this);
    }

    public void inject() {
        if (this.globalInjection) {
            throw new IllegalStateException("Global injection is already enabled");
        }

        try {
            this.injector.inject();
            this.globalInjection = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void inject(@NotNull Player player) {
        if (this.globalInjection) {
            throw new IllegalStateException("Global injection is enabled");
        }

        this.injector.inject(player);
    }

    public void uninject(@NotNull Player player) {
        if (this.globalInjection) {
            throw new IllegalStateException("Global injection is enabled");
        }

        this.injector.uninject(player);
    }

    public void sendPacket(@NotNull Player player, @NotNull ClientboundPacket packet) {
        Channel channel = Reflections.getPacketChannel(player);
        if (channel == null) return;

        if (channel.eventLoop().inEventLoop()) {
            this.writeAndFlushPacket(channel, packet);
        } else {
            channel.eventLoop().execute(() -> {
                this.writeAndFlushPacket(channel, packet);
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

    private void writeAndFlushPacket(Channel channel, ClientboundPacket packet) {
        if (!channel.isOpen() || !channel.isActive()) return;

        channel.writeAndFlush(packet).addListener(future -> {
            if (future.isSuccess() && this.packetListener.isListeningClientbound(packet.identifier())) {
                this.packetListener.publishClientbound(packet);
            }
        }).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}