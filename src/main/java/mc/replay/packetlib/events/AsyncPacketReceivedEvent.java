package mc.replay.packetlib.events;

import mc.replay.packetlib.network.packet.ServerboundPacket;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class AsyncPacketReceivedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ServerboundPacket packet;

    public AsyncPacketReceivedEvent(@NotNull ServerboundPacket packet) {
        super(true);
        this.packet = packet;
    }

    public @NotNull ServerboundPacket getPacket() {
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