package mc.replay.packetlib.network.packet.identifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PacketIdentifier {

    int getIdentifier();

    static <I extends PacketIdentifier> @Nullable I getPacketIdentifier(@NotNull Class<I> clazz, int packetId) {
        for (I enumConstant : clazz.getEnumConstants()) {
            if (enumConstant.getIdentifier() == packetId) {
                return enumConstant;
            }
        }

        return null;
    }
}