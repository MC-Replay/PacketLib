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

import java.util.UUID;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundSystemChatMessagePacket(@NotNull Component message, @NotNull ChatPosition position,
                                                 @Nullable UUID sender) implements ClientboundPacket {

    public ClientboundSystemChatMessagePacket {
        if (sender == null && ProtocolVersion.getServerVersion().isLowerOrEqual(ProtocolVersion.MINECRAFT_1_18_2)) {
            sender = new UUID(0L, 0L); // Always displays chat message
        }
    }

    public ClientboundSystemChatMessagePacket(@NotNull String plainMessage, @NotNull ChatPosition position,
                                              @Nullable UUID sender) {
        this(
                AdventurePacketConverter.asComponent(plainMessage),
                position,
                sender
        );
    }

    public ClientboundSystemChatMessagePacket(@NotNull Component message, @NotNull ChatPosition position) {
        this(
                message,
                position,
                null
        );
    }

    public ClientboundSystemChatMessagePacket(@NotNull String plainMessage, @NotNull ChatPosition position) {
        this(
                AdventurePacketConverter.asComponent(plainMessage),
                position
        );
    }

    public ClientboundSystemChatMessagePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(COMPONENT),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4))
                        ? (reader.read(BOOLEAN)) ? ChatPosition.ACTION_BAR : ChatPosition.SYSTEM
                        : reader.readEnum(ChatPosition.class),
                (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4))
                        ? null
                        : reader.read(UUID)
        );
    }

    public @NotNull String messageAsPlain() {
        return AdventurePacketConverter.asPlain(this.message);
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(COMPONENT, this.message);

        if (ProtocolVersion.getServerVersion().isHigherOrEqual(ProtocolVersion.MINECRAFT_1_19_4)) {
            writer.write(BOOLEAN, this.position == ChatPosition.ACTION_BAR);
        } else {
            writer.writeEnum(ChatPosition.class, this.position);
            writer.write(UUID, this.sender);
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.SYSTEM_CHAT;
    }

    public enum ChatPosition {

        CHAT,
        SYSTEM,
        ACTION_BAR
    }
}