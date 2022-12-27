package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundPlayerSpawnPacket(int entityId, @NotNull UUID playerUuid,
                                           @NotNull Pos position) implements ClientboundPacket {

    public ClientboundPlayerSpawnPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(UUID),
                new Pos(
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(BYTE) * 360f / 256f,
                        reader.read(BYTE) * 360f / 256f
                )
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(UUID, this.playerUuid);
        writer.write(DOUBLE, this.position.x());
        writer.write(DOUBLE, this.position.y());
        writer.write(DOUBLE, this.position.z());
        writer.write(BYTE, (byte) (this.position.yaw() * 256f / 360f));
        writer.write(BYTE, (byte) (this.position.pitch() * 256f / 360f));
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.PLAYER_SPAWN;
    }
}