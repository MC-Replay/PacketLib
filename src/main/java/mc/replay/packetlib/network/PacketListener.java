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
import java.util.function.BiFunction;

public final class PacketListener {

    private final Map<Integer, Collection<BiFunction<Player, ClientboundPacket, Boolean>>> clientboundPacketListeners = new HashMap<>();
    private final Map<Integer, Collection<BiFunction<Player, ServerboundPacket, Boolean>>> serverboundPacketListeners = new HashMap<>();
    private final Collection<BiFunction<Player, ClientboundPacket, Boolean>> globalClientboundPacketListeners = new HashSet<>();
    private final Collection<BiFunction<Player, ServerboundPacket, Boolean>> globalServerboundPacketListeners = new HashSet<>();

    public void listenClientbound(@NotNull ClientboundPacketIdentifier identifier, @NotNull BiConsumer<Player, ClientboundPacket> consumer) {
        this.clientboundPacketListeners.putIfAbsent(identifier.getIdentifier(), new HashSet<>());
        this.clientboundPacketListeners.get(identifier.getIdentifier()).add((player, packet) -> {
            consumer.accept(player, packet);
            return false;
        });
    }

    public void interceptClientbound(@NotNull ClientboundPacketIdentifier identifier, @NotNull BiFunction<Player, ClientboundPacket, Boolean> function) {
        this.clientboundPacketListeners.putIfAbsent(identifier.getIdentifier(), new HashSet<>());
        this.clientboundPacketListeners.get(identifier.getIdentifier()).add(function);
    }

    public void listenServerbound(@NotNull ServerboundPacketIdentifier identifier, @NotNull BiConsumer<@NotNull Player, @NotNull ServerboundPacket> consumer) {
        this.serverboundPacketListeners.putIfAbsent(identifier.getIdentifier(), new HashSet<>());
        this.serverboundPacketListeners.get(identifier.getIdentifier()).add((player, packet) -> {
            consumer.accept(player, packet);
            return false;
        });
    }

    public void interceptServerbound(@NotNull ServerboundPacketIdentifier identifier, @NotNull BiFunction<@NotNull Player, @NotNull ServerboundPacket, Boolean> function) {
        this.serverboundPacketListeners.putIfAbsent(identifier.getIdentifier(), new HashSet<>());
        this.serverboundPacketListeners.get(identifier.getIdentifier()).add(function);
    }

    public void listenClientbound(@NotNull BiConsumer<Player, ClientboundPacket> consumer) {
        this.globalClientboundPacketListeners.add((player, packet) -> {
            consumer.accept(player, packet);
            return false;
        });
    }

    public void interceptClientbound(@NotNull BiFunction<Player, ClientboundPacket, Boolean> function) {
        this.globalClientboundPacketListeners.add(function);
    }

    public void listenServerbound(@NotNull BiConsumer<@NotNull Player, @NotNull ServerboundPacket> consumer) {
        this.globalServerboundPacketListeners.add((player, packet) -> {
            consumer.accept(player, packet);
            return false;
        });
    }

    public void interceptServerbound(@NotNull BiFunction<@NotNull Player, @NotNull ServerboundPacket, Boolean> function) {
        this.globalServerboundPacketListeners.add(function);
    }

    public boolean isListeningClientbound(int packetId) {
        if (!this.globalClientboundPacketListeners.isEmpty()) return true;

        Collection<BiFunction<Player, ClientboundPacket, Boolean>> listeners = this.clientboundPacketListeners.get(packetId);
        return listeners != null && !listeners.isEmpty();
    }

    public boolean isListeningClientbound(@NotNull ClientboundPacketIdentifier identifier) {
        return this.isListeningClientbound(identifier.getIdentifier());
    }

    public boolean isListeningServerbound(int packetId) {
        if (!this.globalServerboundPacketListeners.isEmpty()) return true;

        Collection<BiFunction<Player, ServerboundPacket, Boolean>> listeners = this.serverboundPacketListeners.get(packetId);
        return listeners != null && !listeners.isEmpty();
    }

    public boolean isListeningServerbound(@NotNull ServerboundPacketIdentifier identifier) {
        return this.isListeningServerbound(identifier.getIdentifier());
    }

    public boolean publishClientbound(@NotNull Player player, @NotNull ClientboundPacket packet) {
        boolean shouldCancel = false;
        for (BiFunction<Player, ClientboundPacket, Boolean> globalClientboundPacketListener : this.globalClientboundPacketListeners) {
            boolean cancel = globalClientboundPacketListener.apply(player, packet);
            if (cancel) shouldCancel = true;
        }

        Collection<BiFunction<Player, ClientboundPacket, Boolean>> listeners = this.clientboundPacketListeners.get(packet.identifier().getIdentifier());
        if (listeners == null) return shouldCancel;

        for (BiFunction<Player, ClientboundPacket, Boolean> listener : listeners) {
            boolean cancel = listener.apply(player, packet);
            if (cancel) shouldCancel = true;
        }

        return shouldCancel;
    }

    public boolean publishServerbound(@NotNull Player player, @NotNull ServerboundPacket packet) {
        boolean shouldCancel = false;
        for (BiFunction<Player, ServerboundPacket, Boolean> globalClientboundPacketListener : this.globalServerboundPacketListeners) {
            boolean cancel = globalClientboundPacketListener.apply(player, packet);
            if (cancel) shouldCancel = true;
        }

        Collection<BiFunction<Player, ServerboundPacket, Boolean>> listeners = this.serverboundPacketListeners.get(packet.identifier().getIdentifier());
        if (listeners == null) return shouldCancel;

        for (BiFunction<Player, ServerboundPacket, Boolean> listener : listeners) {
            boolean cancel = listener.apply(player, packet);
            if (cancel) shouldCancel = true;
        }

        return shouldCancel;
    }
}