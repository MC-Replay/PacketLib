package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.crypto.MessageSignature;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

public record ClientboundDeleteChatPacket(@NotNull MessageSignature signature) implements ClientboundPacket {

    public ClientboundDeleteChatPacket(@NotNull ReplayByteBuffer reader) {
        this(
                new MessageSignature(reader)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(this.signature);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.DELETE_CHAT_MESSAGE;
    }
}