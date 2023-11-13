package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.PlayerProfileProperty;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo(since = ProtocolVersion.MINECRAFT_1_19_4)
public final class ClientboundPlayerInfoPacket implements ClientboundPacket {

    private final EnumSet<Action> actions;
    private final List<Entry> entries;

    public ClientboundPlayerInfoPacket(@NotNull EnumSet<@NotNull Action> actions, @NotNull List<@NotNull Entry> entries) {
        this.actions = actions;
        this.entries = entries;
    }

    public ClientboundPlayerInfoPacket(@NotNull Action action, @NotNull Entry entry) {
        this(EnumSet.of(action), List.of(entry));
    }

    public ClientboundPlayerInfoPacket(@NotNull ReplayByteBuffer reader) {
        this.actions = reader.readEnumSet(Action.class);
        this.entries = reader.readCollection(buffer -> {
            UUID uuid = buffer.read(ReplayByteBuffer.UUID);
            String username = "";
            List<PlayerProfileProperty> properties = List.of();
            boolean listed = false;
            int latency = 0;
            GameMode gameMode = GameMode.SURVIVAL;
            Component displayName = null;

            for (Action action : actions) {
                switch (action) {
                    case ADD_PLAYER -> {
                        username = buffer.read(STRING);
                        properties = buffer.readCollection(PlayerProfileProperty::new);
                    }
                    case UPDATE_GAME_MODE -> gameMode = GameMode.values()[buffer.read(VAR_INT)];
                    case UPDATE_LISTED -> listed = buffer.read(BOOLEAN);
                    case UPDATE_LATENCY -> latency = buffer.read(VAR_INT);
                    case UPDATE_DISPLAY_NAME -> displayName = buffer.readOptional(COMPONENT);
                }
            }

            return new Entry(uuid, username, properties, listed, latency, gameMode, displayName);
        });
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.writeEnumSet(this.actions, Action.class);
        writer.writeCollection(this.entries, (buffer, entry) -> {
            buffer.write(UUID, entry.uuid);
            for (Action action : this.actions) {
                action.writer.write(buffer, entry);
            }
        });
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.PLAYER_INFO;
    }

    public @NotNull EnumSet<Action> actions() {
        return this.actions;
    }

    public @NotNull List<Entry> entries() {
        return this.entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientboundPlayerInfoPacket that = (ClientboundPlayerInfoPacket) o;
        return this.actions.equals(that.actions) && this.entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.actions, this.entries);
    }

    @Override
    public String toString() {
        return "ClientboundPlayerInfoPacket{" +
                "actions=" + this.actions +
                ", entries=" + this.entries +
                '}';
    }

    public record Entry(UUID uuid, String username, List<PlayerProfileProperty> properties,
                        boolean listed, int latency, GameMode gameMode,
                        @Nullable Component displayName) {
        public Entry {
            properties = List.copyOf(properties);
        }
    }

    public enum Action {
        ADD_PLAYER((writer, entry) -> {
            writer.write(STRING, entry.username);
            writer.writeCollection(entry.properties);
        }),
        INITIALIZE_CHAT((writer, entry) -> writer.writeOptional(null)),
        UPDATE_GAME_MODE((writer, entry) -> writer.write(VAR_INT, entry.gameMode.ordinal())),
        UPDATE_LISTED((writer, entry) -> writer.write(BOOLEAN, entry.listed)),
        UPDATE_LATENCY((writer, entry) -> writer.write(VAR_INT, entry.latency)),
        UPDATE_DISPLAY_NAME((writer, entry) -> writer.writeOptional(COMPONENT, entry.displayName));

        final Writer writer;

        Action(Writer writer) {
            this.writer = writer;
        }

        interface Writer {
            void write(ReplayByteBuffer writer, Entry entry);
        }
    }
}