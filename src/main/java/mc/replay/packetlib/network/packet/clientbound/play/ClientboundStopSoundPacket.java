package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundStopSoundPacket(byte flags, Integer sourceId, String sound) implements ClientboundPacket {

    public ClientboundStopSoundPacket(@NotNull ReplayByteBuffer reader) {
        this(read(reader));
    }

    private ClientboundStopSoundPacket(ClientboundStopSoundPacket packet) {
        this(packet.flags, packet.sourceId, packet.sound);
    }

    private static ClientboundStopSoundPacket read(@NotNull ReplayByteBuffer reader) {
        byte flags = reader.read(BYTE);
        Integer sourceId = (flags == 1 || flags == 3) ? reader.read(VAR_INT) : null;
        String sound = (flags == 2 || flags == 3) ? reader.read(STRING) : null;
        return new ClientboundStopSoundPacket(flags, sourceId, sound);
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(BYTE, this.flags);

        if (this.flags == 1 || this.flags == 3) {
            assert this.sourceId != null;
            writer.write(VAR_INT, this.sourceId);
        }

        if (this.flags == 2 || this.flags == 3) {
            assert this.sound != null;
            writer.write(STRING, this.sound);
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.STOP_SOUND;
    }
}