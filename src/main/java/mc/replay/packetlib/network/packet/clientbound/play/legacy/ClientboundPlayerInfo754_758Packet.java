package mc.replay.packetlib.network.packet.clientbound.play.legacy;

import mc.replay.packetlib.data.PlayerProfileProperty;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.AdventurePacketConverter;
import mc.replay.packetlib.utils.ProtocolVersion;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo(until = ProtocolVersion.MINECRAFT_1_18_2)
public record ClientboundPlayerInfo754_758Packet(@NotNull PlayerInfoAction action,
                                                 @NotNull List<PlayerInfoEntry> entries) implements ClientboundPacket {

    public ClientboundPlayerInfo754_758Packet {
        entries = List.copyOf(entries);
        for (PlayerInfoEntry entry : entries) {
            if (!entry.getClass().equals(action.getClazz())) {
                throw new IllegalArgumentException("Invalid entry class for action " + action);
            }
        }
    }

    public ClientboundPlayerInfo754_758Packet(@NotNull PlayerInfoAction action, @NotNull PlayerInfoEntry entry) {
        this(action, List.of(entry));
    }

    public ClientboundPlayerInfo754_758Packet(@NotNull ReplayByteBuffer reader) {
        this(read(reader));
    }

    private ClientboundPlayerInfo754_758Packet(@NotNull ClientboundPlayerInfo754_758Packet packet) {
        this(packet.action, packet.entries);
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.writeEnum(PlayerInfoAction.class, this.action);
        writer.writeCollection(this.entries, (buffer, entry) -> {
            buffer.write(UUID, entry.uuid());
            entry.write(buffer);
        });
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.PLAYER_INFO_754_758;
    }

    private static @NotNull ClientboundPlayerInfo754_758Packet read(@NotNull ReplayByteBuffer reader) {
        final PlayerInfoAction action = reader.readEnum(PlayerInfoAction.class);
        final List<PlayerInfoEntry> entries = reader.readCollection((buffer) -> {
            final UUID uuid = buffer.read(UUID);
            return switch (action) {
                case ADD_PLAYER -> new PlayerInfoEntry.AddPlayer(uuid, buffer);
                case UPDATE_GAMEMODE -> new PlayerInfoEntry.UpdateGameMode(uuid, buffer);
                case UPDATE_LATENCY -> new PlayerInfoEntry.UpdateLatency(uuid, buffer);
                case UPDATE_DISPLAY_NAME -> new PlayerInfoEntry.UpdateDisplayName(uuid, buffer);
                case REMOVE_PLAYER -> new PlayerInfoEntry.RemovePlayer(uuid);
            };
        });

        return new ClientboundPlayerInfo754_758Packet(action, entries);
    }

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

    public interface PlayerInfoEntry extends ReplayByteBuffer.Writer {

        @NotNull UUID uuid();

        record AddPlayer(@NotNull UUID uuid, String name, List<PlayerProfileProperty> properties, GameMode gameMode,
                         int ping, @Nullable Component displayName) implements PlayerInfoEntry {

            public AddPlayer {
                properties = List.copyOf(properties);
            }

            public AddPlayer(@NotNull UUID uuid, String name, List<PlayerProfileProperty> properties, GameMode gameMode,
                             int ping, @Nullable String plainDisplayName) {
                this(
                        uuid,
                        name,
                        properties,
                        gameMode,
                        ping,
                        (plainDisplayName == null) ? null : AdventurePacketConverter.asComponent(plainDisplayName)
                );
            }

            public AddPlayer(@NotNull UUID uuid, @NotNull ReplayByteBuffer reader) {
                this(
                        uuid,
                        reader.read(STRING),
                        reader.readCollection(PlayerProfileProperty::new),
                        reader.readEnum(GameMode.class),
                        reader.read(VAR_INT),
                        reader.readOptional(COMPONENT)
                );
            }

            public @Nullable String displayNameAsPlain() {
                return (this.displayName == null) ? null : AdventurePacketConverter.asPlain(this.displayName);
            }

            @Override
            public void write(@NotNull ReplayByteBuffer writer) {
                writer.write(STRING, this.name);
                writer.writeCollection(this.properties);
                writer.write(VAR_INT, this.gameMode.ordinal());
                writer.write(VAR_INT, this.ping);
                writer.writeOptional(COMPONENT, this.displayName);
                // TODO 1.19 public key
            }
        }

        record UpdateGameMode(@NotNull UUID uuid, GameMode gameMode) implements PlayerInfoEntry {

            public UpdateGameMode(@NotNull UUID uuid, @NotNull ReplayByteBuffer reader) {
                this(
                        uuid,
                        reader.readEnum(GameMode.class)
                );
            }

            @Override
            public void write(@NotNull ReplayByteBuffer writer) {
                writer.write(VAR_INT, this.gameMode.ordinal());
            }
        }

        record UpdateLatency(@NotNull UUID uuid, int ping) implements PlayerInfoEntry {

            public UpdateLatency(@NotNull UUID uuid, @NotNull ReplayByteBuffer reader) {
                this(
                        uuid,
                        reader.read(VAR_INT)
                );
            }

            @Override
            public void write(@NotNull ReplayByteBuffer writer) {
                writer.write(VAR_INT, this.ping);
            }
        }

        record UpdateDisplayName(@NotNull UUID uuid, @Nullable Component displayName) implements PlayerInfoEntry {

            public UpdateDisplayName(@NotNull UUID uuid, @Nullable String plainDisplayName) {
                this(
                        uuid,
                        (plainDisplayName) == null ? null : AdventurePacketConverter.asComponent(plainDisplayName)
                );
            }

            public UpdateDisplayName(@NotNull UUID uuid, @NotNull ReplayByteBuffer reader) {
                this(
                        uuid,
                        reader.readOptional(COMPONENT)
                );
            }

            public @Nullable String displayNameAsPlain() {
                return (this.displayName == null) ? null : AdventurePacketConverter.asPlain(this.displayName);
            }

            @Override
            public void write(@NotNull ReplayByteBuffer writer) {
                writer.writeOptional(COMPONENT, this.displayName);
            }
        }

        record RemovePlayer(@NotNull UUID uuid) implements PlayerInfoEntry {

            @Override
            public void write(@NotNull ReplayByteBuffer writer) {
            }
        }
    }
}