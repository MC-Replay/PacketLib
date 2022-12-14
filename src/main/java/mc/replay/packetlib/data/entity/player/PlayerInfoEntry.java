package mc.replay.packetlib.data.entity.player;

import mc.replay.packetlib.data.PlayerProfileProperty;
import mc.replay.packetlib.network.PacketBuffer;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static mc.replay.packetlib.network.PacketBuffer.*;

public interface PlayerInfoEntry extends PacketBuffer.Writer {

    @NotNull UUID uuid();

    record AddPlayer(@NotNull UUID uuid, String name, List<PlayerProfileProperty> properties, GameMode gameMode,
                     int ping, @Nullable Component displayName) implements PlayerInfoEntry {

        public AddPlayer {
            properties = List.copyOf(properties);
        }

        public AddPlayer(@NotNull UUID uuid, @NotNull PacketBuffer reader) {
            this(
                    uuid,
                    reader.read(STRING),
                    reader.readCollection(PlayerProfileProperty::new),
                    reader.readEnum(GameMode.class),
                    reader.read(VAR_INT),
                    reader.readOptional(COMPONENT)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.write(STRING, this.name);
            writer.writeCollection(this.properties);
            writer.write(VAR_INT, this.gameMode.ordinal());
            writer.write(VAR_INT, this.ping);
            writer.writeOptional(COMPONENT, this.displayName);
        }
    }

    record UpdateGameMode(@NotNull UUID uuid, GameMode gameMode) implements PlayerInfoEntry {

        public UpdateGameMode(@NotNull UUID uuid, @NotNull PacketBuffer reader) {
            this(
                    uuid,
                    reader.readEnum(GameMode.class)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.write(VAR_INT, this.gameMode.ordinal());
        }
    }

    record UpdateLatency(@NotNull UUID uuid, int ping) implements PlayerInfoEntry {

        public UpdateLatency(@NotNull UUID uuid, @NotNull PacketBuffer reader) {
            this(
                    uuid,
                    reader.read(VAR_INT)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.write(VAR_INT, this.ping);
        }
    }

    record UpdateDisplayName(@NotNull UUID uuid, @Nullable Component displayName) implements PlayerInfoEntry {

        public UpdateDisplayName(@NotNull UUID uuid, @NotNull PacketBuffer reader) {
            this(
                    uuid,
                    reader.readOptional(COMPONENT)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.writeOptional(COMPONENT, this.displayName);
        }
    }

    record RemovePlayer(@NotNull UUID uuid) implements PlayerInfoEntry {

        @Override
        public void write(@NotNull PacketBuffer writer) {
        }
    }
}