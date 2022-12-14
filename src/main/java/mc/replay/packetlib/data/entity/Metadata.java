package mc.replay.packetlib.data.entity;

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

    private static final Map<Byte, PacketBuffer.Type<?>> SERIALIZERS = new HashMap<>();

    static {
        SERIALIZERS.put(TYPE_BYTE, BYTE);
        SERIALIZERS.put(TYPE_VAR_INT, VAR_INT);
        SERIALIZERS.put(TYPE_FLOAT, FLOAT);
        SERIALIZERS.put(TYPE_STRING, STRING);
        SERIALIZERS.put(TYPE_CHAT, COMPONENT);
        SERIALIZERS.put(TYPE_OPT_CHAT, OPT_CHAT);
        SERIALIZERS.put(TYPE_SLOT, ITEM);
        SERIALIZERS.put(TYPE_BOOLEAN, BOOLEAN);
        SERIALIZERS.put(TYPE_ROTATION, ROTATION);
        SERIALIZERS.put(TYPE_POSITION, BLOCK_POSITION);
        SERIALIZERS.put(TYPE_OPT_POSITION, OPT_BLOCK_POSITION);
        SERIALIZERS.put(TYPE_DIRECTION, BLOCK_FACE);
        SERIALIZERS.put(TYPE_OPT_UUID, OPT_UUID);
        SERIALIZERS.put(TYPE_OPT_BLOCK_ID, OPT_BLOCK_ID);
        SERIALIZERS.put(TYPE_NBT, NBT);
        SERIALIZERS.put(TYPE_VILLAGER_DATA, VILLAGER_DATA);
        SERIALIZERS.put(TYPE_OPT_VAR_INT, OPT_VAR_INT);
        SERIALIZERS.put(TYPE_POSE, POSE);
    }

    @SuppressWarnings("unchecked")
    public static <T> PacketBuffer.Type<T> getSerializer(int type) {
        return (Type<T>) SERIALIZERS.get((byte) type);
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

        public static <T> Entry<T> read(int type, @NotNull PacketBuffer reader) {
            final PacketBuffer.Type<T> serializer = getSerializer(type);
            if (serializer == null) throw new UnsupportedOperationException("Unknown value type: " + type);
            return new Entry<>(type, reader.read(serializer), serializer);
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.write(VAR_INT, this.type);
            writer.write(this.serializer, this.value);
        }
    }
}