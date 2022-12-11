package mc.replay.packetlib.network;

import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.ServerboundPacket;
import mc.replay.packetlib.network.packet.clientbound.*;
import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifier;
import mc.replay.packetlib.network.packet.identifier.ServerboundPacketIdentifier;
import mc.replay.packetlib.network.packet.serverbound.ServerboundAnimationPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class PacketRegistry {

    private final Map<ServerboundPacketIdentifier, PacketDefinition<ServerboundPacketIdentifier, ? extends ServerboundPacket>> serverboundPacketRegistry = new HashMap<>();
    private final Map<ClientboundPacketIdentifier, PacketDefinition<ClientboundPacketIdentifier, ? extends ClientboundPacket>> clientboundPacketRegistry = new HashMap<>();

    public PacketRegistry() {
        this.registerClientboundPacket(ClientboundPacketIdentifier.BLOCK_ACTION, ClientboundBlockActionPacket.class, ClientboundBlockActionPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.BLOCK_CHANGE, ClientboundBlockChangePacket.class, ClientboundBlockChangePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.BLOCK_ENTITY_DATA, ClientboundBlockEntityDataPacket.class, ClientboundBlockEntityDataPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_ANIMATION, ClientboundEntityAnimationPacket.class, ClientboundEntityAnimationPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_TELEPORT, ClientboundEntityTeleportPacket.class, ClientboundEntityTeleportPacket::new);

        this.registerServerboundPacket(ServerboundPacketIdentifier.ANIMATION, ServerboundAnimationPacket.class, ServerboundAnimationPacket::new);
    }

    public boolean isClientboundRegistered(@Nullable ClientboundPacketIdentifier identifier) {
        if (identifier == null) return false;
        return this.clientboundPacketRegistry.containsKey(identifier);
    }

    public boolean isClientboundRegistered(int packetId) {
        return this.isClientboundRegistered(PacketIdentifier.getPacketIdentifier(ClientboundPacketIdentifier.class, packetId));
    }

    public boolean isServerboundRegistered(@Nullable ServerboundPacketIdentifier identifier) {
        if (identifier == null) return false;
        return this.serverboundPacketRegistry.containsKey(identifier);
    }

    public boolean isServerboundRegistered(int packetId) {
        return this.isServerboundRegistered(PacketIdentifier.getPacketIdentifier(ServerboundPacketIdentifier.class, packetId));
    }

    @SuppressWarnings("unchecked")
    public <P extends ClientboundPacket> @Nullable P getClientboundPacket(@NotNull ClientboundPacketIdentifier identifier, @NotNull PacketBuffer reader) {
        PacketDefinition<ClientboundPacketIdentifier, ?> packetDefinition = this.clientboundPacketRegistry.get(identifier);
        if (packetDefinition == null) return null;

        return (P) packetDefinition.construct(reader);
    }

    public <P extends ClientboundPacket> @Nullable P getClientboundPacket(int packetId, @NotNull PacketBuffer reader) {
        ClientboundPacketIdentifier packetIdentifier = PacketIdentifier.getPacketIdentifier(ClientboundPacketIdentifier.class, packetId);
        if (packetIdentifier == null) return null;

        return this.getClientboundPacket(packetIdentifier, reader);
    }

    @SuppressWarnings("unchecked")
    public <P extends ServerboundPacket> @Nullable P getServerboundPacket(@NotNull ServerboundPacketIdentifier identifier, @NotNull PacketBuffer reader) {
        PacketDefinition<ServerboundPacketIdentifier, ?> packetDefinition = this.serverboundPacketRegistry.get(identifier);
        if (packetDefinition == null) return null;

        return (P) packetDefinition.construct(reader);
    }

    public <P extends ServerboundPacket> @Nullable P getServerboundPacket(int packetId, @NotNull PacketBuffer reader) {
        ServerboundPacketIdentifier packetIdentifier = PacketIdentifier.getPacketIdentifier(ServerboundPacketIdentifier.class, packetId);
        if (packetIdentifier == null) return null;

        return this.getServerboundPacket(packetIdentifier, reader);
    }

    private <P extends ClientboundPacket> void registerClientboundPacket(@NotNull ClientboundPacketIdentifier identifier,
                                                                         @NotNull Class<P> packetClass,
                                                                         @NotNull Function<@NotNull PacketBuffer, @NotNull P> packetConstructor) {
        this.clientboundPacketRegistry.put(identifier, new PacketDefinition<>(identifier, packetClass, packetConstructor));
    }

    private <P extends ServerboundPacket> void registerServerboundPacket(@NotNull ServerboundPacketIdentifier identifier,
                                                                         @NotNull Class<P> packetClass,
                                                                         @NotNull Function<@NotNull PacketBuffer, @NotNull P> packetConstructor) {
        this.serverboundPacketRegistry.put(identifier, new PacketDefinition<>(identifier, packetClass, packetConstructor));
    }
}