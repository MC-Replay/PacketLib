package mc.replay.packetlib.network;

import mc.replay.packetlib.network.packet.ClientboundPacket;
import mc.replay.packetlib.network.packet.ServerboundPacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class PacketListener {

    private final Collection<Consumer<ClientboundPacket>> clientboundPacketListeners = new HashSet<>();
    private final Collection<BiConsumer<Player, ServerboundPacket>> serverboundPacketListeners = new HashSet<>();

    public void listenClientbound(@NotNull Consumer<@NotNull ClientboundPacket> consumer) {
        this.clientboundPacketListeners.add(consumer);
    }

    public void listenServerbound(@NotNull BiConsumer<@NotNull Player, @NotNull ServerboundPacket> consumer) {
        this.serverboundPacketListeners.add(consumer);
    }

    public void publishClientbound(@NotNull ClientboundPacket packet) {
        for (Consumer<ClientboundPacket> listener : this.clientboundPacketListeners) {
            listener.accept(packet);
        }
    }

    public void publishServerbound(@NotNull Player player, @NotNull ServerboundPacket packet) {
        for (BiConsumer<Player, ServerboundPacket> listener : this.serverboundPacketListeners) {
            listener.accept(player, packet);
        }
    }
}