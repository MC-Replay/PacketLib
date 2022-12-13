package mc.replay.packetlib.data.team;

import org.jetbrains.annotations.NotNull;

public enum CollisionRule {

    ALWAYS("always"),
    PUSH_OTHER_TEAMS("pushOtherTeams"),
    PUSH_OWN_TEAM("pushOwnTeam"),
    NEVER("never");

    private final String identifier;

    CollisionRule(String identifier) {
        this.identifier = identifier;
    }

    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    public static @NotNull CollisionRule fromIdentifier(String identifier) {
        for (CollisionRule collisionRule : values()) {
            if (collisionRule.getIdentifier().equalsIgnoreCase(identifier)) {
                return collisionRule;
            }
        }

        throw new IllegalArgumentException("Identifier for CollisionRule is invalid: " + identifier);
    }
}