package mc.replay.packetlib.network.packet.clientbound.play.legacy;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo(until = ProtocolVersion.MINECRAFT_1_18_2)
public record ClientboundPaintingSpawn754_758Packet(int entityId, @NotNull UUID uuid, int motive,
                                                    @NotNull Pos position,
                                                    @NotNull Direction direction) implements ClientboundPacket {

    public ClientboundPaintingSpawn754_758Packet(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(UUID),
                reader.read(VAR_INT),
                Pos.of(
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(DOUBLE)
                ),
                reader.readEnum(Direction.class)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(UUID, this.uuid);
        writer.write(VAR_INT, this.motive);

        writer.write(DOUBLE, this.position.x());
        writer.write(DOUBLE, this.position.y());
        writer.write(DOUBLE, this.position.z());

        writer.writeEnum(Direction.class, this.direction);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.SPAWN_PAINTING_754_758;
    }

    public enum Direction {

        SOUTH,
        WEST,
        NORTH,
        EAST
    }
}