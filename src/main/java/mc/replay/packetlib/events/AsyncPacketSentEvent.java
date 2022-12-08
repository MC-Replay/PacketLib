package mc.replay.packetlib.events;

import mc.replay.packetlib.network.packet.Packet;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class AsyncPacketSentEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Packet packet;

    public AsyncPacketSentEvent(@NotNull Packet packet) {
        super(true);
        this.packet = packet;
    }

    public @NotNull Packet getPacket() {
        return this.packet;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}