package mc.replay.packetlib.data.entity;

import mc.replay.packetlib.network.ReplayByteBuffer;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.FLOAT;

public sealed interface InteractEntityType permits InteractEntityType.Interact, InteractEntityType.Attack, InteractEntityType.InteractAt {

    record Interact(@NotNull PlayerHand hand) implements InteractEntityType {

        public Interact(@NotNull ReplayByteBuffer reader) {
            this(
                    reader.readEnum(PlayerHand.class)
            );
        }
    }

    record Attack() implements InteractEntityType {
    }

    record InteractAt(float targetX, float targetY, float targetZ,
                      @NotNull PlayerHand hand) implements InteractEntityType {

        public InteractAt(@NotNull ReplayByteBuffer reader) {
            this(
                    reader.read(FLOAT),
                    reader.read(FLOAT),
                    reader.read(FLOAT),
                    reader.readEnum(PlayerHand.class)
            );
        }
    }
}