package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundEntitySpawnPacket(int entityId, @NotNull UUID uuid, int type, @NotNull Pos position,
                                           float headRotation, int data, short velocityX, short velocityY,
                                           short velocityZ) implements ClientboundPacket {

    public ClientboundEntitySpawnPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(UUID),
                reader.read(VAR_INT),
                new Pos(
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(BYTE) * 360f / 256f,
                        reader.read(BYTE) * 360f / 256f
                ),
                reader.read(BYTE) * 360f / 256f,
                reader.read(VAR_INT),
                reader.read(SHORT),
                reader.read(SHORT),
                reader.read(SHORT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(UUID, this.uuid);
        writer.write(VAR_INT, this.type);

        writer.write(DOUBLE, this.position.x());
        writer.write(DOUBLE, this.position.y());
        writer.write(DOUBLE, this.position.z());

        writer.write(BYTE, (byte) (this.position.pitch() * 256 / 360));
        writer.write(BYTE, (byte) (this.position.yaw() * 256 / 360));
        writer.write(BYTE, (byte) (this.headRotation * 256 / 360));

        writer.write(VAR_INT, this.data);

        writer.write(SHORT, this.velocityX);
        writer.write(SHORT, this.velocityY);
        writer.write(SHORT, this.velocityZ);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_SPAWN;
    }
}