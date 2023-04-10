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

    public static @NotNull Pos of(double x, double y, double z, float pitch, float yaw, boolean pitchFirst) {
        return new Pos(x, y, z, yaw, pitch);
    }

    public static @NotNull Pos of(double x, double y, double z) {
        return new Pos(x, y, z, 0, 0);
    }

    public @NotNull Pos withRotation(float yaw, float pitch) {
        return new Pos(
                this.x,
                this.y,
                this.z,
                yaw,
                pitch
        );
    }

    public @NotNull Pos add(@NotNull Pos delta) {
        return new Pos(
                this.x + delta.x(),
                this.y + delta.y(),
                this.z + delta.z(),
                this.yaw,
                this.pitch
        );
    }

    public @NotNull Pos addWithRotation(@NotNull Pos delta) {
        return new Pos(
                this.x + delta.x(),
                this.y + delta.y(),
                this.z + delta.z(),
                this.yaw + delta.yaw(),
                this.pitch + delta.pitch()
        );
    }

    public @NotNull Pos subtract(@NotNull Pos other) {
        return new Pos(
                this.x - other.x(),
                this.y - other.y(),
                this.z - other.z(),
                this.yaw,
                this.pitch
        );
    }

    public @NotNull Pos subtractWithRotation(@NotNull Pos delta) {
        return new Pos(
                this.x - delta.x(),
                this.y - delta.y(),
                this.z - delta.z(),
                this.yaw - delta.yaw(),
                this.pitch - delta.pitch()
        );
    }
}