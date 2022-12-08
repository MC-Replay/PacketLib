package mc.replay.packetlib.network;

import mc.replay.packetlib.network.packet.identifier.ClientboundPacketIdentifier;
import mc.replay.packetlib.network.packet.identifier.ServerboundPacketIdentifier;
import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundAnimationPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public final class PacketRegistry {

    private final Map<ServerboundPacketIdentifier, PacketDefinition<ServerboundPacketIdentifier, ? extends ServerboundPacket>> serverboundPacketRegistry = new HashMap<>();
    private final Collection<ClientboundPacketIdentifier> clientboundPacketRegistry = new HashSet<>();

    public PacketRegistry() {
        this.registerClientboundPacket(ClientboundPacketIdentifier.CLIENTBOUND_BLOCK_CHANGE);
        this.registerClientboundPacket(ClientboundPacketIdentifier.CLIENTBOUND_ENTITY_ANIMATION);

        this.registerServerboundPacket(ServerboundPacketIdentifier.SERVERBOUND_ANIMATION, ServerboundAnimationPacket.class, ServerboundAnimationPacket::new);
    }

    public boolean isClientboundRegistered(@Nullable ClientboundPacketIdentifier identifier) {
        if (identifier == null) return false;
        return this.clientboundPacketRegistry.contains(identifier);
    }

    public boolean isClientboundRegistered(int packetId) {
        return this.isClientboundRegistered(ClientboundPacketIdentifier.getPacketIdentifier(packetId));
    }

    public boolean isServerboundRegistered(@Nullable ServerboundPacketIdentifier identifier) {
        if (identifier == null) return false;
        return this.serverboundPacketRegistry.containsKey(identifier);
    }

    public boolean isServerboundRegistered(int packetId) {
        return this.isServerboundRegistered(ServerboundPacketIdentifier.getPacketIdentifier(packetId));
    }

    @SuppressWarnings("unchecked")
    public <P extends ServerboundPacket> @Nullable P getServerboundPacket(@NotNull ServerboundPacketIdentifier identifier, @NotNull PacketBuffer reader) {
        PacketDefinition<ServerboundPacketIdentifier, ?> packetDefinition = this.serverboundPacketRegistry.get(identifier);
        if (packetDefinition == null) return null;

        return (P) packetDefinition.construct(reader);
    }

    public <P extends ServerboundPacket> @Nullable P getServerboundPacket(int packetId, @NotNull PacketBuffer reader) {
        ServerboundPacketIdentifier packetIdentifier = ServerboundPacketIdentifier.getPacketIdentifier(packetId);
        if (packetIdentifier == null) return null;

        return this.getServerboundPacket(packetIdentifier, reader);
    }

    private <P extends ClientboundPacket> void registerClientboundPacket(@NotNull ClientboundPacketIdentifier identifier) {
        this.clientboundPacketRegistry.add(identifier);
    }

    private <P extends ServerboundPacket> void registerServerboundPacket(@NotNull ServerboundPacketIdentifier identifier,
                                                                         @NotNull Class<P> packetClass,
                                                                         @NotNull Function<@NotNull PacketBuffer, @NotNull P> packetConstructor) {
        this.serverboundPacketRegistry.put(identifier, new PacketDefinition<>(identifier, packetClass, packetConstructor));
    }
}