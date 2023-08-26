package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.STRING;

@PacketInfo(since = ProtocolVersion.MINECRAFT_1_19_4)
public record ClientboundCustomChatCompletionPacket(@NotNull Action action,
                                                    @NotNull List<@NotNull String> entries) implements ClientboundPacket {

    public ClientboundCustomChatCompletionPacket {
        entries = List.copyOf(entries);
    }

    public ClientboundCustomChatCompletionPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.readEnum(Action.class),
                reader.readCollection(STRING)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.writeEnum(Action.class, this.action);
        writer.writeCollection(STRING, this.entries);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.CUSTOM_CHAT_COMPLETIONS;
    }

    public enum Action {

        ADD, REMOVE, SET
    }
}