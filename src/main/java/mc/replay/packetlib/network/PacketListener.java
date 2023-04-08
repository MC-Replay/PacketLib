package mc.replay.packetlib.network;

import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class PacketListener {

    private final Map<Integer, Collection<Consumer<ClientboundPacket>>> clientboundPacketListeners = new HashMap<>();
    private final Map<Integer, Collection<BiConsumer<Player, ServerboundPacket>>> serverboundPacketListeners = new HashMap<>();

    public void listenClientbound(@NotNull ClientboundPacketIdentifier identifier, @NotNull Consumer<@NotNull ClientboundPacket> consumer) {
        this.clientboundPacketListeners.putIfAbsent(identifier.getIdentifier(), new HashSet<>());
        this.clientboundPacketListeners.get(identifier.getIdentifier()).add(consumer);
    }

    public void listenServerbound(@NotNull ServerboundPacketIdentifier identifier, @NotNull BiConsumer<@NotNull Player, @NotNull ServerboundPacket> consumer) {
        this.serverboundPacketListeners.putIfAbsent(identifier.getIdentifier(), new HashSet<>());
        this.serverboundPacketListeners.get(identifier.getIdentifier()).add(consumer);
    }

    public boolean isListeningClientbound(int packetId) {
        Collection<Consumer<ClientboundPacket>> listeners = this.clientboundPacketListeners.get(packetId);
        return listeners != null && !listeners.isEmpty();
    }

    public boolean isListeningClientbound(@NotNull ClientboundPacketIdentifier identifier) {
        return this.isListeningClientbound(identifier.getIdentifier());
    }

    public boolean isListeningServerbound(int packetId) {
        Collection<BiConsumer<Player, ServerboundPacket>> listeners = this.serverboundPacketListeners.get(packetId);
        return listeners != null && !listeners.isEmpty();
    }

    public boolean isListeningServerbound(@NotNull ServerboundPacketIdentifier identifier) {
        return this.isListeningServerbound(identifier.getIdentifier());
    }

    public void publishClientbound(@NotNull ClientboundPacket packet) {
        Collection<Consumer<ClientboundPacket>> listeners = this.clientboundPacketListeners.get(packet.identifier().getIdentifier());
        if (listeners == null) return;

        for (Consumer<ClientboundPacket> listener : listeners) {
            listener.accept(packet);
        }
    }

    public void publishServerbound(@NotNull Player player, @NotNull ServerboundPacket packet) {
        Collection<BiConsumer<Player, ServerboundPacket>> listeners = this.serverboundPacketListeners.get(packet.identifier().getIdentifier());
        if (listeners == null) return;

        for (BiConsumer<Player, ServerboundPacket> listener : listeners) {
            listener.accept(player, packet);
        }
    }
}