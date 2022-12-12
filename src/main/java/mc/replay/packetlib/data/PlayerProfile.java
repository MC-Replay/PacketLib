package mc.replay.packetlib.data;

import java.util.Map;
import java.util.UUID;

public record PlayerProfile(UUID uuid, String name, Map<String, Property> properties) {

    public record Property(String name, String value, String signature) {
    }
}