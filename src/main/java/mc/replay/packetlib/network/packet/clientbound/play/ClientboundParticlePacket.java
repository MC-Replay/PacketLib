package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundParticlePacket(int particleId, boolean longDistance, double x, double y, double z,
                                        float offsetX, float offsetY, float offsetZ, float particleData,
                                        int particleCount, byte[] data) implements ClientboundPacket {

    public ClientboundParticlePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(BOOLEAN),
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(DOUBLE),
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(INT),
                reader.read(RAW_BYTES)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.particleId);
        writer.write(BOOLEAN, this.longDistance);
        writer.write(DOUBLE, this.x);
        writer.write(DOUBLE, this.y);
        writer.write(DOUBLE, this.z);
        writer.write(FLOAT, this.offsetX);
        writer.write(FLOAT, this.offsetY);
        writer.write(FLOAT, this.offsetZ);
        writer.write(FLOAT, this.particleData);
        writer.write(INT, this.particleCount);
        writer.write(RAW_BYTES, this.data);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.PARTICLE;
    }
}