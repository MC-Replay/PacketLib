package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.Pos;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundBlockBreakAnimationPacket(int entityID, Pos position,
                                                   byte stage) implements ClientboundPacket {

    public ClientboundBlockBreakAnimationPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                Pos.of(
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(DOUBLE),
                        reader.read(BYTE) * 360f / 256f,
                        reader.read(BYTE) * 360f / 256f
                ),
                reader.read(BYTE)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityID);

        writer.write(DOUBLE, this.position.x());
        writer.write(DOUBLE, this.position.y());
        writer.write(DOUBLE, this.position.z());

        writer.write(BYTE, (byte) (this.position.pitch() * 256 / 360));
        writer.write(BYTE, (byte) (this.position.yaw() * 256 / 360));

        writer.write(BYTE, this.stage);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.BLOCK_BREAK_ANIMATION;
    }
}