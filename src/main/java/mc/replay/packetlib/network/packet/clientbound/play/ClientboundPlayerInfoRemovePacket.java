package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.UUID;

@PacketInfo(since = ProtocolVersion.MINECRAFT_1_19_4)
public record ClientboundPlayerInfoRemovePacket(List<java.util.UUID> playerUuids) implements ClientboundPacket {

    public ClientboundPlayerInfoRemovePacket {
        playerUuids = List.copyOf(playerUuids);
    }

    public ClientboundPlayerInfoRemovePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.readCollection(UUID)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.writeCollection(UUID, this.playerUuids);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.PLAYER_INFO_REMOVE;
    }
}