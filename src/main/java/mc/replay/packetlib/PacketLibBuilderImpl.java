package mc.replay.packetlib;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

final class PacketLibBuilderImpl implements PacketLibBuilder {

    private boolean global = false;
    private boolean listenServerbound = true;
    private boolean listenMinecraftClientbound = true;
    private boolean listenPacketLibClientbound = true;

    @Override
    public @NotNull PacketLibBuilder global() {
        this.global = true;
        return this;
    }

    @Override
    public @NotNull PacketLibBuilder player() {
        this.global = false;
        return this;
    }

    @Override
    public @NotNull PacketLibBuilder listenServerbound(boolean listenServerbound) {
        this.listenServerbound = listenServerbound;
        return this;
    }

    @Override
    public @NotNull PacketLibBuilder listenMinecraftClientbound(boolean listenMinecraftClientbound) {
        this.listenMinecraftClientbound = listenMinecraftClientbound;
        return this;
    }

    @Override
    public @NotNull PacketLibBuilder listenPacketLibClientbound(boolean listenPacketLibClientbound) {
        this.listenPacketLibClientbound = listenPacketLibClientbound;
        return this;
    }

    @Override
    public @NotNull PacketLibImpl inject(@NotNull JavaPlugin javaPlugin) {
        PacketLibSettings settings = new PacketLibSettings(
                this.global,
                this.listenServerbound,
                this.listenMinecraftClientbound,
                this.listenPacketLibClientbound
        );

        return new PacketLibImpl(javaPlugin, settings);
    }
}