package mc.replay.packetlib.network.packet.clientbound.play.legacy;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo(until = ProtocolVersion.MINECRAFT_1_19_2)
public record ClientboundCustomSoundEffect_754_760Packet(@NotNull String soundName, int sourceId, int x, int y,
                                                         int z,
                                                         float volume, float pitch,
                                                         long seed) implements ClientboundPacket {

    public ClientboundCustomSoundEffect_754_760Packet(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(STRING),
                reader.read(VAR_INT),
                reader.read(INT) / 8,
                reader.read(INT) / 8,
                reader.read(INT) / 8,
                reader.read(FLOAT),
                reader.read(FLOAT),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19))
                        ? 0
                        : reader.read(LONG)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(STRING, this.soundName);
        writer.write(VAR_INT, this.sourceId);
        writer.write(INT, this.x * 8);
        writer.write(INT, this.y * 8);
        writer.write(INT, this.z * 8);
        writer.write(FLOAT, this.volume);
        writer.write(FLOAT, this.pitch);

        if (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19)) {
            writer.write(LONG, this.seed);
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.CUSTOM_SOUND_EFFECT_754_760;
    }
}