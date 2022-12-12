package mc.replay.packetlib.data;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.ShortTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import mc.replay.packetlib.utils.NBTUtils;
import mc.replay.packetlib.utils.Reflections;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record ItemStackWrapper(int materialId, byte amount, @Nullable CompoundTag meta) {

    public static @NotNull ItemStackWrapper of(@NotNull ItemStack itemStack) {
        int materialId = itemStack.getType().ordinal();
        byte amount = (byte) itemStack.getAmount();

        CompoundTag meta = new CompoundTag("");
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (itemMeta.hasEnchants() && !(itemMeta instanceof SkullMeta)) {
                ListTag enchantments = new ListTag("Enchantments");

                CompoundTag enchantmentTag = new CompoundTag("");
                enchantmentTag.put(new StringTag("id", "unbreaking"));
                enchantmentTag.put(new ShortTag("lvl", (short) 1));

                enchantments.add(enchantmentTag);
                meta.put(enchantments);
            }

            if (itemMeta instanceof SkullMeta skullMeta) {
                PlayerProfile playerProfile = Reflections.getGameProfile(skullMeta);
                if (playerProfile != null) {
                    CompoundTag skullOwnerTag = new CompoundTag("SkullOwner");
                    skullOwnerTag.put(new StringTag("Name", playerProfile.name()));
                    skullOwnerTag.put(NBTUtils.uuidToTag("Id", playerProfile.uuid()));

                    if (!playerProfile.properties().isEmpty()) {
                        CompoundTag propertiesTag = new CompoundTag("Properties");

                        for (Map.Entry<String, PlayerProfile.Property> entry : playerProfile.properties().entrySet()) {
                            ListTag listTag = new ListTag(entry.getKey());

                            CompoundTag propertyTag = new CompoundTag("");
                            propertyTag.put(new StringTag("Value", entry.getValue().value()));
                            if (entry.getValue().signature() != null) {
                                propertyTag.put(new StringTag("Signature", entry.getValue().signature()));
                            }

                            listTag.add(propertyTag);
                            propertiesTag.put(listTag);
                        }

                        skullOwnerTag.put(propertiesTag);
                    }

                    meta.put(skullOwnerTag);
                }
            }
        }

        return new ItemStackWrapper(materialId, amount, (meta.isEmpty()) ? null : meta);
    }
}