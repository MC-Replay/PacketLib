package mc.replay.packetlib.data.entity.player;

import org.jetbrains.annotations.NotNull;

public enum PlayerInfoAction {

    ADD_PLAYER(PlayerInfoEntry.AddPlayer.class),
    UPDATE_GAMEMODE(PlayerInfoEntry.UpdateGameMode.class),
    UPDATE_LATENCY(PlayerInfoEntry.UpdateLatency.class),
    UPDATE_DISPLAY_NAME(PlayerInfoEntry.UpdateDisplayName.class),
    REMOVE_PLAYER(PlayerInfoEntry.RemovePlayer.class);

    private final Class<? extends PlayerInfoEntry> clazz;

    PlayerInfoAction(Class<? extends PlayerInfoEntry> clazz) {
        this.clazz = clazz;
    }

    public @NotNull Class<? extends PlayerInfoEntry> getClazz() {
        return this.clazz;
    }
}