package mc.replay.packetlib.network.packet.serverbound.play;

import mc.replay.packetlib.data.entity.InteractEntityType;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.BOOLEAN;
import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public record ServerboundInteractEntityPacket(int targetId, @NotNull InteractEntityType type,
                                              boolean sneaking) implements ServerboundPacket {

    public ServerboundInteractEntityPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                switch (reader.read(VAR_INT)) {
                    case 0 -> new InteractEntityType.Interact(reader);
                    case 1 -> new InteractEntityType.Attack();
                    case 2 -> new InteractEntityType.InteractAt(reader);
                    default -> throw new IllegalArgumentException("Unknown interact entity type");
                },
                reader.read(BOOLEAN)
        );
    }

    @Override
    public @NotNull ServerboundPacketIdentifier identifier() {
        return ServerboundPacketIdentifier.INTERACT_ENTITY;
    }
}