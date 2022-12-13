package mc.replay.packetlib.network.packet.clientbound.play;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static mc.replay.packetlib.network.PacketBuffer.*;

public record ClientboundBlockEntityDataPacket(@NotNull Vector blockPosition, int action,
                                               @Nullable CompoundTag data) implements ClientboundPacket {

    public ClientboundBlockEntityDataPacket(@NotNull PacketBuffer reader) {
        this(
                reader.read(BLOCK_POSITION),
                reader.read(VAR_INT),
                (CompoundTag) reader.read(NBT)
        );
    }

    @Override
    public void write(@NotNull PacketBuffer writer) {
        writer.write(BLOCK_POSITION, this.blockPosition);
        writer.write(VAR_INT, this.action);

        if (this.data != null) {
            writer.write(NBT, this.data);
        } else {
            writer.write(BYTE, (byte) 0x00);
        }
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.BLOCK_ENTITY_DATA;
    }
}