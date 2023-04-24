package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.AdventurePacketConverter;
import mc.replay.packetlib.utils.ProtocolVersion;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static mc.replay.packetlib.network.ReplayByteBuffer.COMPONENT;
import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

@PacketInfo(since = ProtocolVersion.MINECRAFT_1_19_3)
public record ClientboundDisguisedChatPacket(@NotNull Component message, int chatType, @NotNull Component chatTypeName,
                                             @Nullable Component targetName) implements ClientboundPacket {

    public ClientboundDisguisedChatPacket(@NotNull Component message, @NotNull ChatType chatType, @NotNull Component chatTypeName, @Nullable Component targetName) {
        this(
                message,
                chatType.ordinal(),
                chatTypeName,
                targetName
        );
    }

    public ClientboundDisguisedChatPacket(@NotNull String plainMessage, int chatType, @NotNull String plainChatTypeName, @Nullable String plainTargetName) {
        this(
                AdventurePacketConverter.asComponent(plainMessage),
                chatType,
                AdventurePacketConverter.asComponent(plainChatTypeName),
                (plainTargetName == null) ? null : AdventurePacketConverter.asComponent(plainTargetName)
        );
    }

    public ClientboundDisguisedChatPacket(@NotNull String plainMessage, @NotNull ChatType chatType, @NotNull String plainChatTypeName, @Nullable String plainTargetName) {
        this(
                plainMessage,
                chatType.ordinal(),
                plainChatTypeName,
                plainTargetName
        );
    }

    public ClientboundDisguisedChatPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(COMPONENT),
                reader.read(VAR_INT),
                reader.read(COMPONENT),
                reader.readOptional(COMPONENT)
        );
    }

    public @NotNull String messageAsPlain() {
        return AdventurePacketConverter.asPlain(this.message);
    }

    public @NotNull String chatTypeNameAsPlain() {
        return AdventurePacketConverter.asPlain(this.chatTypeName);
    }

    public @Nullable String targetNameAsPlain() {
        return (this.targetName == null) ? null : AdventurePacketConverter.asPlain(this.targetName);
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(COMPONENT, this.message);
        writer.write(VAR_INT, this.chatType);
        writer.write(COMPONENT, this.chatTypeName);
        writer.writeOptional(COMPONENT, this.targetName);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.DISGUISED_CHAT;
    }

    public enum ChatType {

        CHAT,
        SAY_COMMAND,
        MSG_COMMAND_INCOMING,
        MSG_COMMAND_OUTGOING,
        TEAM_MSG_COMMAND_INCOMING,
        TEAM_MSG_COMMAND_OUTGOING,
        EMOTE_COMMAND,
        RAW
    }
}