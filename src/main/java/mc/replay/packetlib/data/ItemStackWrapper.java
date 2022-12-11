package mc.replay.packetlib.data;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.ShortTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public record ItemStackWrapper(int materialId, byte amount, CompoundTag meta) {

    public static ItemStackWrapper of(@NotNull ItemStack itemStack) {
        int materialId = itemStack.getType().ordinal();
        byte amount = (byte) itemStack.getAmount();

        CompoundTag meta = null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null && itemMeta.hasEnchants()) {
            meta = new CompoundTag("");
            ListTag enchantments = new ListTag("Enchantments");

            CompoundTag enchantmentTag = new CompoundTag("");
            enchantmentTag.put(new StringTag("id", "unbreaking"));
            enchantmentTag.put(new ShortTag("lvl", (short) 1));

            enchantments.add(enchantmentTag);
            meta.put(enchantments);
        }

        return new ItemStackWrapper(materialId, amount, meta);
    }
}