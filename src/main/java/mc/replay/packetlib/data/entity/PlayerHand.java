package mc.replay.packetlib.data.entity;

import org.jetbrains.annotations.NotNull;

public enum PlayerHand {

    MAIN_HAND,
    OFF_HAND;

    public @NotNull EntityAnimation toAnimation() {
        return (this == MAIN_HAND) ? EntityAnimation.SWING_MAIN_ARM : EntityAnimation.SWING_OFF_HAND;
    }
}