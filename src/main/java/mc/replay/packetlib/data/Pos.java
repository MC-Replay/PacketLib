package mc.replay.packetlib.data;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public record Pos(double x, double y, double z, float yaw, float pitch) {

    public static @NotNull Pos from(@NotNull Location location) {
        return new Pos(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static @NotNull Pos of(double x, double y, double z, float yaw, float pitch) {
        return new Pos(x, y, z, yaw, pitch);
    }

    public static @NotNull Pos of(double x, double y, double z) {
        return new Pos(x, y, z, 0, 0);
    }
}