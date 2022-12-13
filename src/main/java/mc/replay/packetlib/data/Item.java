package mc.replay.packetlib.data;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Item {

    public static @NotNull Item AIR = new Item(0, (byte) 0, null);

    private final int materialId;
    private final byte amount;
    private final CompoundTag meta;

    public Item(int materialId, byte amount, @Nullable CompoundTag meta) {
        this.materialId = materialId;
        this.amount = amount;
        this.meta = meta;
    }

    public int materialId() {
        return this.materialId;
    }

    public byte amount() {
        return this.amount;
    }

    public @Nullable CompoundTag meta() {
        return this.meta;
    }
}