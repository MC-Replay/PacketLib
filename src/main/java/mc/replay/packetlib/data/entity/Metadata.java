package mc.replay.packetlib.data.entity;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import mc.replay.packetlib.data.Item;
import mc.replay.packetlib.network.PacketBuffer;
import net.kyori.adventure.text.Component;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Pose;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static mc.replay.packetlib.network.PacketBuffer.*;

public final class Metadata {

    public static Entry<Byte> Byte(byte value) {
        return new Entry<>(TYPE_BYTE, value, BYTE);
    }

    public static Entry<Integer> VarInt(int value) {
        return new Entry<>(TYPE_VAR_INT, value, VAR_INT);
    }

    public static Entry<Float> Float(float value) {
        return new Entry<>(TYPE_FLOAT, value, FLOAT);
    }

    public static Entry<String> String(@NotNull String value) {
        return new Entry<>(TYPE_STRING, value, STRING);
    }

    public static Entry<Component> Chat(@NotNull Component value) {
        return new Entry<>(TYPE_CHAT, value, COMPONENT);
    }

    public static Entry<Component> OptChat(@Nullable Component value) {
        return new Entry<>(TYPE_OPT_CHAT, value, OPT_CHAT);
    }

    public static Entry<Item> Slot(@NotNull Item value) {
        return new Entry<>(TYPE_SLOT, value, ITEM);
    }

    public static Entry<Boolean> Boolean(boolean value) {
        return new Entry<>(TYPE_BOOLEAN, value, BOOLEAN);
    }

    public static Entry<Vector> Rotation(@NotNull Vector value) {
        return new Entry<>(TYPE_ROTATION, value, ROTATION);
    }

    public static Entry<Vector> Position(@NotNull Vector value) {
        return new Entry<>(TYPE_POSITION, value, BLOCK_POSITION);
    }

    public static Entry<Vector> OptPosition(@Nullable Vector value) {
        return new Entry<>(TYPE_OPT_POSITION, value, OPT_BLOCK_POSITION);
    }

    public static Entry<BlockFace> BlockFace(@NotNull BlockFace value) {
        return new Entry<>(TYPE_DIRECTION, value, BLOCK_FACE);
    }

    public static Entry<java.util.UUID> OptUUID(@Nullable UUID value) {
        return new Entry<>(TYPE_OPT_UUID, value, OPT_UUID);
    }

    public static Entry<Integer> OptBlockID(@Nullable Integer value) {
        return new Entry<>(TYPE_OPT_BLOCK_ID, value, OPT_BLOCK_ID);
    }

    public static Entry<Tag> NBT(@Nullable Tag nbt) {
        return new Entry<>(TYPE_NBT, nbt, NBT);
    }

    public static Entry<int[]> VillagerData(int villagerType,
                                            int villagerProfession,
                                            int level) {
        return new Entry<>(TYPE_VILLAGER_DATA, new int[]{villagerType, villagerProfession, level},
                VILLAGER_DATA);
    }

    public static Entry<Integer> OptVarInt(@Nullable Integer value) {
        return new Entry<>(TYPE_OPT_VAR_INT, value, OPT_VAR_INT);
    }

    public static Entry<Pose> Pose(@NotNull Pose value) {
        return new Entry<>(TYPE_POSE, value, POSE);
    }

    public static final byte TYPE_BYTE = 0;
    public static final byte TYPE_VAR_INT = 1;
    public static final byte TYPE_FLOAT = 2;
    public static final byte TYPE_STRING = 3;
    public static final byte TYPE_CHAT = 4;
    public static final byte TYPE_OPT_CHAT = 5;
    public static final byte TYPE_SLOT = 6;
    public static final byte TYPE_BOOLEAN = 7;
    public static final byte TYPE_ROTATION = 8;
    public static final byte TYPE_POSITION = 9;
    public static final byte TYPE_OPT_POSITION = 10;
    public static final byte TYPE_DIRECTION = 11;
    public static final byte TYPE_OPT_UUID = 12;
    public static final byte TYPE_OPT_BLOCK_ID = 13;
    public static final byte TYPE_NBT = 14;
    public static final byte TYPE_VILLAGER_DATA = 16;
    public static final byte TYPE_OPT_VAR_INT = 17;
    public static final byte TYPE_POSE = 18;

    private static final Map<Byte, Entry<?>> EMPTY_VALUES = new HashMap<>();

    static {
        EMPTY_VALUES.put(TYPE_BYTE, Byte((byte) 0));
        EMPTY_VALUES.put(TYPE_VAR_INT, VarInt(0));
        EMPTY_VALUES.put(TYPE_FLOAT, Float(0f));
        EMPTY_VALUES.put(TYPE_STRING, String(""));
        EMPTY_VALUES.put(TYPE_CHAT, Chat(Component.empty()));
        EMPTY_VALUES.put(TYPE_OPT_CHAT, OptChat(null));
        EMPTY_VALUES.put(TYPE_SLOT, Slot(Item.AIR));
        EMPTY_VALUES.put(TYPE_BOOLEAN, Boolean(false));
        EMPTY_VALUES.put(TYPE_ROTATION, Rotation(new Vector(0, 0, 0)));
        EMPTY_VALUES.put(TYPE_POSITION, Position(new Vector(0, 0, 0)));
        EMPTY_VALUES.put(TYPE_OPT_POSITION, OptPosition(null));
        EMPTY_VALUES.put(TYPE_DIRECTION, BlockFace(BlockFace.DOWN));
        EMPTY_VALUES.put(TYPE_OPT_UUID, OptUUID(null));
        EMPTY_VALUES.put(TYPE_OPT_BLOCK_ID, OptBlockID(null));
        EMPTY_VALUES.put(TYPE_NBT, NBT(new CompoundTag("")));
        EMPTY_VALUES.put(TYPE_VILLAGER_DATA, VillagerData(0, 0, 0));
        EMPTY_VALUES.put(TYPE_OPT_VAR_INT, OptVarInt(null));
        EMPTY_VALUES.put(TYPE_POSE, Pose(Pose.STANDING));
    }

    private Entry<?>[] entries = new Entry<?>[0];
    private Map<Integer, Entry<?>> entryMap = null;

    @SuppressWarnings("unchecked")
    public <T> T getIndex(int index, @Nullable T defaultValue) {
        final Entry<?>[] entries = this.entries;
        if (index < 0 || index >= entries.length) return defaultValue;
        final Entry<?> entry = entries[index];
        return (entry != null) ? (T) entry.value() : defaultValue;
    }

    public void setIndex(int index, @NotNull Entry<?> entry) {
        Entry<?>[] entries = this.entries;
        if (index >= entries.length) {
            final int newLength = Math.max(entries.length * 2, index + 1);
            this.entries = entries = Arrays.copyOf(entries, newLength);
        }

        entries[index] = entry;
        this.entryMap = null;
    }

    public Map<Integer, Entry<?>> getEntries() {
        Map<Integer, Entry<?>> map = this.entryMap;
        if (map == null) {
            map = new HashMap<>();
            final Entry<?>[] entries = this.entries;
            for (int i = 0; i < entries.length; i++) {
                final Entry<?> entry = entries[i];
                if (entry != null) map.put(i, entry);
            }
            this.entryMap = Map.copyOf(map);
        }
        return map;
    }

    public record Entry<T>(int type, @UnknownNullability T value,
                           @NotNull PacketBuffer.Type<T> serializer) implements PacketBuffer.Writer {

        public static Entry<?> read(int type, @NotNull PacketBuffer reader) {
            final Entry<?> value = EMPTY_VALUES.get((byte) type);
            if (value == null) throw new UnsupportedOperationException("Unknown value type: " + type);
            return value.withValue(reader);
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.write(VAR_INT, this.type);
            writer.write(this.serializer, this.value);
        }

        private Entry<T> withValue(@NotNull PacketBuffer reader) {
            return new Entry<>(this.type, reader.read(this.serializer), this.serializer);
        }
    }
}