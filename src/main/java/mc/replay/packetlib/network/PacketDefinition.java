package mc.replay.packetlib.network;

import mc.replay.packetlib.network.packet.identifier.PacketIdentifier;
import mc.replay.packetlib.network.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

final class PacketDefinition<I extends PacketIdentifier, P extends Packet<I>> {

    private final I identifier;
    private final Class<P> packetClass;
    private final Function<ReplayByteBuffer, P> packetConstructor;

    PacketDefinition(I identifier, Class<P> packetClass, Function<ReplayByteBuffer, P> packetConstructor) {
        this.identifier = identifier;
        this.packetClass = packetClass;
        this.packetConstructor = packetConstructor;
    }

    @NotNull I identifier() {
        return this.identifier;
    }

    @NotNull Class<P> packetClass() {
        return this.packetClass;
    }

    P construct(@NotNull ReplayByteBuffer reader) {
        return this.packetConstructor.apply(reader);
    }
}