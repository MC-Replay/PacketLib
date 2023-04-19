package mc.replay.packetlib;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface PacketLibBuilder {

    @NotNull PacketLibBuilder global();

    @NotNull PacketLibBuilder player();

    @NotNull PacketLibBuilder listenServerbound(boolean listenServerbound);

    @NotNull PacketLibBuilder listenMinecraftClientbound(boolean listenMinecraftClientbound);

    @NotNull PacketLibBuilder listenPacketLibClientbound(boolean listenPacketLibClientbound);

    @NotNull PacketLibImpl inject(@NotNull JavaPlugin javaPlugin);
}