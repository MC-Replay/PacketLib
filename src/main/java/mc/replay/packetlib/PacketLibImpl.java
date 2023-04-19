package mc.replay.packetlib;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import mc.replay.packetlib.network.PacketListener;
import mc.replay.packetlib.network.PacketRegistry;
import mc.replay.packetlib.network.netty.PacketLibInjector;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifierLoader;
import mc.replay.packetlib.network.user.PacketLibConnectionHandler;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

final class PacketLibImpl implements PacketLib {

    private static final Map<JavaPlugin, PacketLib> INSTANCES = new HashMap<>();

    static @NotNull PacketLib getInstance(@NotNull JavaPlugin javaPlugin) {
        PacketLib instance = INSTANCES.get(javaPlugin);
        if (instance == null) {
            throw new IllegalStateException("PacketLib is not initialized for this plugin! (JavaPlugin: " + javaPlugin.getName() + ")");
        }

        return instance;
    }

    static boolean hasInstance(@NotNull JavaPlugin javaPlugin) {
        return INSTANCES.containsKey(javaPlugin);
    }

    static final PacketIdentifierLoader PACKET_IDENTIFIER_LOADER = new PacketIdentifierLoader();
    static final PacketRegistry PACKET_REGISTRY = new PacketRegistry();

    private final JavaPlugin javaPlugin;
    private final PacketLibSettings settings;

    private final PacketListener packetListener;
    private final PacketLibInjector injector;

    PacketLibImpl(@NotNull JavaPlugin javaPlugin, @NotNull PacketLibSettings settings) {
        if (INSTANCES.containsKey(javaPlugin)) {
            throw new IllegalStateException("PacketLib is already initialized for this plugin! (JavaPlugin: " + javaPlugin.getName() + ")");
        }

        this.javaPlugin = javaPlugin;
        this.settings = settings;

        this.packetListener = new PacketListener();
        this.injector = new PacketLibInjector(this);

        new PacketLibConnectionHandler(this);

        if (settings.global()) {
            try {
                this.injector.inject();
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }

        INSTANCES.put(javaPlugin, this);
    }

    @Override
    public @NotNull JavaPlugin javaPlugin() {
        return this.javaPlugin;
    }

    @Override
    public @NotNull PacketLibSettings settings() {
        return this.settings;
    }

    @Override
    public @NotNull PacketListener packetListener() {
        return this.packetListener;
    }

    @Override
    public void inject(@NotNull Player player) {
        if (this.settings.global()) {
            throw new IllegalStateException("Global injection is enabled");
        }

        this.injector.inject(player);
    }

    @Override
    public void uninject(@NotNull Player player) {
        if (this.settings.global()) {
            throw new IllegalStateException("Global injection is enabled");
        }

        this.injector.uninject(player);
    }

    @Override
    public void sendPacket(@NotNull Player player, @NotNull ClientboundPacket packet) {
        Channel channel = Reflections.getPacketChannel(player);
        if (channel == null) return;

        if (channel.eventLoop().inEventLoop()) {
            this.writeAndFlushPacket(player, channel, packet);
        } else {
            channel.eventLoop().execute(() -> {
                this.writeAndFlushPacket(player, channel, packet);
            });
        }
    }

    private void writeAndFlushPacket(Player player, Channel channel, ClientboundPacket packet) {
        if (!channel.isOpen() || !channel.isActive()) return;

        channel.writeAndFlush(packet).addListener(future -> {
            if (!future.isSuccess()) return;

            Collection<PacketLib> listeningPacketLibInstances = this.findListeningPacketLibInstances(packet.identifier());
            if (listeningPacketLibInstances.isEmpty()) return;

            for (PacketLib instance : listeningPacketLibInstances) {
                instance.packetListener().publishClientbound(player, packet);
            }
        }).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    private Collection<PacketLib> findListeningPacketLibInstances(ClientboundPacketIdentifier packetIdentifier) {
        return INSTANCES.values().stream()
                .filter(packetLib -> packetLib.settings().listenPacketLibClientbound() && packetLib.packetListener().isListeningClientbound(packetIdentifier))
                .toList();
    }
}