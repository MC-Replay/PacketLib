package mc.replay.packetlib.data.entity;

import mc.replay.packetlib.network.ReplayByteBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public final class Metadata {

    private static Function<Integer, ReplayByteBuffer.Type<?>> SERIALIZER_PROVIDER;

    public static void registerSerializerProvider(Function<Integer, ReplayByteBuffer.Type<?>> serializerProvider) {
        SERIALIZER_PROVIDER = serializerProvider;
    }

    public static @NotNull Metadata fromEntries(@NotNull Map<Integer, Entry<?>> entries) {
        Metadata metadata = new Metadata();
        for (Map.Entry<Integer, Entry<?>> entry : entries.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;

            metadata.setIndex(entry.getKey(), entry.getValue());
        }
        return metadata;
    }

    private Entry<?>[] entries = new Entry<?>[0];
    private Map<Integer, Entry<?>> entryMap = null;

    private boolean detectChanges = false;
    private Map<Integer, Entry<?>> changes = null;

    public void detectChanges(boolean detectChanges) {
        this.detectChanges = detectChanges;

        if (!detectChanges) {
            this.changes = null;
        }
    }

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

        if (this.detectChanges) {
            if (this.changes == null) {
                this.changes = new HashMap<>();
            }

            this.changes.put(index, entry);
        }
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

    public @Nullable Map<Integer, Entry<?>> getChanges() {
        return this.changes;
    }

    public record Entry<T>(int type, @UnknownNullability T value,
                           @NotNull ReplayByteBuffer.Type<T> serializer) implements ReplayByteBuffer.Writer {

        @SuppressWarnings("unchecked")
        public static <T> Entry<T> read(int type, @NotNull ReplayByteBuffer reader) {
            if (SERIALIZER_PROVIDER == null) throw new IllegalStateException("No serializer provider registered");

            final ReplayByteBuffer.Type<T> serializer = (ReplayByteBuffer.Type<T>) SERIALIZER_PROVIDER.apply(type);
            if (serializer == null) throw new UnsupportedOperationException("Unknown value type: " + type);
            return new Entry<>(type, reader.read(serializer), serializer);
        }

        @Override
        public void write(@NotNull ReplayByteBuffer writer) {
            writer.write(VAR_INT, this.type);
            writer.write(this.serializer, this.value);
        }
    }
}