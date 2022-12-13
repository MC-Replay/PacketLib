package mc.replay.packetlib.data.team;

import org.jetbrains.annotations.NotNull;

public enum NameTagVisibility {

    ALWAYS("always"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
    NEVER("never");

    private final String identifier;

    NameTagVisibility(String identifier) {
        this.identifier = identifier;
    }

    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    public static @NotNull NameTagVisibility fromIdentifier(String identifier) {
        for (NameTagVisibility nameTagVisibility : values()) {
            if (nameTagVisibility.getIdentifier().equalsIgnoreCase(identifier)) {
                return nameTagVisibility;
            }
        }

        throw new IllegalArgumentException("Identifier for NameTagVisibility is invalid: " + identifier);
    }
}