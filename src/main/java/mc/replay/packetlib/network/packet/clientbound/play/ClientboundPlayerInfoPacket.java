package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.entity.player.PlayerInfoAction;
import mc.replay.packetlib.data.entity.player.PlayerInfoEntry;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static mc.replay.packetlib.network.PacketBuffer.UUID;
import static mc.replay.packetlib.network.PacketBuffer.VAR_INT;

public record ClientboundPlayerInfoPacket(@NotNull PlayerInfoAction action,
                                          @NotNull List<PlayerInfoEntry> entries) implements ClientboundPacket {

    public ClientboundPlayerInfoPacket {
        entries = List.copyOf(entries);
        for (PlayerInfoEntry entry : entries) {
            if (!entry.getClass().equals(action.getClazz())) {
                throw new IllegalArgumentException("Invalid entry class for action " + action);
            }
        }
    }

    public ClientboundPlayerInfoPacket(@NotNull PlayerInfoAction action, @NotNull PlayerInfoEntry entry) {
        this(action, List.of(entry));
    }

    public ClientboundPlayerInfoPacket(@NotNull PacketBuffer reader) {
        this(read(reader));
    }

    private ClientboundPlayerInfoPacket(@NotNull ClientboundPlayerInfoPacket packet) {
        this(packet.action, packet.entries);
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.writeEnum(PlayerInfoAction.class, this.action);
        writer.writeCollection(this.entries, (buffer, entry) -> {
            buffer.write(UUID, entry.uuid());
            entry.write(buffer);
        });
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.PLAYER_INFO;
    }

    private static @NotNull ClientboundPlayerInfoPacket read(@NotNull PacketBuffer reader) {
        PlayerInfoAction action = reader.readEnum(PlayerInfoAction.class);
        final int playerInfoCount = reader.read(VAR_INT);
        List<PlayerInfoEntry> entries = new ArrayList<>(playerInfoCount);
        for (int i = 0; i < playerInfoCount; i++) {
            final UUID uuid = reader.read(UUID);
            entries.add(switch (action) {
                case ADD_PLAYER -> new PlayerInfoEntry.AddPlayer(uuid, reader);
                case UPDATE_GAMEMODE -> new PlayerInfoEntry.UpdateGameMode(uuid, reader);
                case UPDATE_LATENCY -> new PlayerInfoEntry.UpdateLatency(uuid, reader);
                case UPDATE_DISPLAY_NAME -> new PlayerInfoEntry.UpdateDisplayName(uuid, reader);
                case REMOVE_PLAYER -> new PlayerInfoEntry.RemovePlayer(uuid);
            });
        }
        return new ClientboundPlayerInfoPacket(action, entries);
    }
}