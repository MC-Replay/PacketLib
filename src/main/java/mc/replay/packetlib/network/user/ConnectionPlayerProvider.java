package mc.replay.packetlib.network.user;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public final class ConnectionPlayerProvider {

    private Player player;

    public ConnectionPlayerProvider(@Nullable Player player) {
        this.player = player;
    }

    public @Nullable Player player() {
        return (this.player != null && !this.player.isOnline()) ? null : this.player;
    }

    public void player(Player player) {
        if (this.player != null) {
            throw new IllegalStateException("Player already set");
        }

        this.player = player;
    }
}