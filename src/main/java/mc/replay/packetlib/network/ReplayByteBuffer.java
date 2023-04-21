package mc.replay.packetlib.network;

import com.github.steveice10.opennbt.tag.builtin.Tag;
import mc.replay.packetlib.data.Item;
import mc.replay.packetlib.utils.Either;
import mc.replay.packetlib.utils.ProtocolVersion;
import net.kyori.adventure.text.Component;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Pose;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ReplayByteBuffer {

    public static Type<Boolean> BOOLEAN = ReplayByteBufferTypes.BOOLEAN;
    public static Type<Byte> BYTE = ReplayByteBufferTypes.BYTE;
    public static Type<Short> SHORT = ReplayByteBufferTypes.SHORT;
    public static Type<Integer> UNSIGNED_SHORT = ReplayByteBufferTypes.UNSIGNED_SHORT;
    public static Type<Integer> INT = ReplayByteBufferTypes.INT;
    public static Type<Long> LONG = ReplayByteBufferTypes.LONG;
    public static Type<Float> FLOAT = ReplayByteBufferTypes.FLOAT;
    public static Type<Double> DOUBLE = ReplayByteBufferTypes.DOUBLE;
    public static Type<Integer> VAR_INT = ReplayByteBufferTypes.VAR_INT;
    public static Type<Long> VAR_LONG = ReplayByteBufferTypes.VAR_LONG;
    public static Type<byte[]> RAW_BYTES = ReplayByteBufferTypes.RAW_BYTES;
    public static Type<String> STRING = ReplayByteBufferTypes.STRING;
    public static Type<Tag> NBT = ReplayByteBufferTypes.NBT;
    public static Type<Vector> BLOCK_POSITION = ReplayByteBufferTypes.BLOCK_POSITION;
    public static Type<Component> COMPONENT = ReplayByteBufferTypes.COMPONENT;

    public static Type<UUID> UUID = ReplayByteBufferTypes.UUID;
    public static Type<Item> ITEM = ReplayByteBufferTypes.ITEM;
    public static Type<byte[]> BYTE_ARRAY = ReplayByteBufferTypes.BYTE_ARRAY;
    public static Type<long[]> LONG_ARRAY = ReplayByteBufferTypes.LONG_ARRAY;
    public static Type<int[]> VAR_INT_ARRAY = ReplayByteBufferTypes.VAR_INT_ARRAY;
    public static Type<long[]> VAR_LONG_ARRAY = ReplayByteBufferTypes.VAR_LONG_ARRAY;
    public static Type<ProtocolVersion> PROTOCOL_VERSION = ReplayByteBufferTypes.PROTOCOL_VERSION;

    public static Type<Component> OPT_CHAT = ReplayByteBufferTypes.OPT_CHAT;
    public static Type<Vector> ROTATION = ReplayByteBufferTypes.ROTATION;
    public static Type<Vector> OPT_BLOCK_POSITION = ReplayByteBufferTypes.OPT_BLOCK_POSITION;
    public static Type<BlockFace> BLOCK_FACE = ReplayByteBufferTypes.BLOCK_FACE;
    public static Type<UUID> OPT_UUID = ReplayByteBufferTypes.OPT_UUID;
    public static Type<Integer> OPT_BLOCK_ID = ReplayByteBufferTypes.OPT_BLOCK_ID;
    public static Type<int[]> VILLAGER_DATA = ReplayByteBufferTypes.VILLAGER_DATA;
    public static Type<Integer> OPT_VAR_INT = ReplayByteBufferTypes.OPT_VAR_INT;
    public static Type<Pose> POSE = ReplayByteBufferTypes.POSE;

    ByteBuffer nioBuffer;
    int writeIndex;
    int readIndex;

    public ReplayByteBuffer(@NotNull ByteBuffer buffer) {
        this.nioBuffer = buffer.order(ByteOrder.BIG_ENDIAN);

        this.writeIndex = buffer.position();
        this.readIndex = buffer.position();
    }

    public <T> void write(@NotNull Type<T> type, @UnknownNullability T value) {
        final long length = type.writer().write(this, value);
        if (length != -1) {
            this.writeIndex += length;
        }
    }

    public <T> void write(@NotNull Writer writer) {
        writer.write(this);
    }

    public <T> T read(@NotNull Type<T> type) {
        return type.reader().read(this);
    }

    public <T> void writeOptional(@NotNull Type<T> type, @Nullable T value) {
        this.write(BOOLEAN, value != null);
        if (value != null) {
            this.write(type, value);
        }
    }

    public <T> void writeOptional(@Nullable Writer writer) {
        this.write(BOOLEAN, writer != null);
        if (writer != null) {
            writer.write(this);
        }
    }

    public <T> @Nullable T readOptional(@NotNull Type<T> type) {
        return this.read(BOOLEAN) ? this.read(type) : null;
    }

    public <T> @Nullable T readOptional(@NotNull Function<@NotNull ReplayByteBuffer, @NotNull T> function) {
        return this.read(BOOLEAN) ? function.apply(this) : null;
    }

    public <T> void writeCollection(@NotNull Type<T> type, @Nullable Collection<@NotNull T> values) {
        if (values == null) {
            this.write(BYTE, (byte) 0);
            return;
        }

        this.write(BYTE, (byte) values.size());

        for (T value : values) {
            this.write(type, value);
        }
    }

    @SafeVarargs
    public final <T> void writeCollection(@NotNull Type<T> type, @NotNull T @Nullable ... values) {
        this.writeCollection(type, values == null ? null : List.of(values));
    }

    public <T extends Writer> void writeCollection(@Nullable Collection<@NotNull T> values) {
        if (values == null) {
            this.write(BYTE, (byte) 0);
            return;
        }

        this.write(VAR_INT, values.size());

        for (T value : values) {
            this.write(value);
        }
    }

    public <T> void writeCollection(@Nullable Collection<@NotNull T> values, @NotNull BiConsumer<@NotNull ReplayByteBuffer, @NotNull T> consumer) {
        if (values == null) {
            this.write(BYTE, (byte) 0);
            return;
        }

        this.write(VAR_INT, values.size());

        for (T value : values) {
            consumer.accept(this, value);
        }
    }

    public @NotNull <T> List<@NotNull T> readCollection(@NotNull Type<T> type) {
        final int size = this.read(VAR_INT);
        final List<T> values = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            values.add(this.read(type));
        }

        return values;
    }

    public @NotNull <T> List<@NotNull T> readCollection(@NotNull Function<@NotNull ReplayByteBuffer, @NotNull T> function) {
        final int size = this.read(VAR_INT);
        final List<T> values = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            values.add(function.apply(this));
        }

        return values;
    }

    public <L, R> void writeEither(Either<L, R> either, BiConsumer<ReplayByteBuffer, L> leftWriter, BiConsumer<ReplayByteBuffer, R> rightWriter) {
        if (either.isLeft()) {
            this.write(BOOLEAN, true);
            leftWriter.accept(this, either.left());
        } else {
            this.write(BOOLEAN, false);
            rightWriter.accept(this, either.right());
        }
    }

    public <L, R> Either<L, R> readEither(Function<ReplayByteBuffer, L> leftReader, Function<ReplayByteBuffer, R> rightReader) {
        return this.read(BOOLEAN) ? Either.left(leftReader.apply(this)) : Either.right(rightReader.apply(this));
    }

    public <E extends Enum<?>> void writeEnum(@NotNull Class<@NotNull E> enumClass, @NotNull E value) {
        this.write(VAR_INT, value.ordinal());
    }

    public <E extends Enum<?>> @NotNull E readEnum(@NotNull Class<@NotNull E> enumClass) {
        return enumClass.getEnumConstants()[this.read(VAR_INT)];
    }

    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        this.nioBuffer.get(this.readIndex, bytes, 0, length);
        this.readIndex += length;
        return bytes;
    }

    public void copyTo(int srcOffset, byte @NotNull [] dest, int destOffset, int length) {
        this.nioBuffer.get(srcOffset, dest, destOffset, length);
    }

    public byte @NotNull [] extractBytes(@NotNull Consumer<@NotNull ReplayByteBuffer> extractor) {
        final int startingPosition = this.readIndex;
        extractor.accept(this);
        final int endingPosition = this.readIndex;
        byte[] output = new byte[endingPosition - startingPosition];
        this.copyTo(startingPosition, output, 0, output.length);
        return output;
    }

    public void clear() {
        this.writeIndex = 0;
        this.readIndex = 0;
    }

    public ByteBuffer nioBuffer() {
        return this.nioBuffer;
    }

    public int writeIndex() {
        return this.writeIndex;
    }

    public int readIndex() {
        return this.readIndex;
    }

    public void writeIndex(int writeIndex) {
        this.writeIndex = writeIndex;
    }

    public void readIndex(int readIndex) {
        this.readIndex = readIndex;
    }

    public int skipWrite(int length) {
        final int oldWriteIndex = this.writeIndex;
        this.writeIndex += length;
        return oldWriteIndex;
    }

    public int readableBytes() {
        return this.writeIndex - this.readIndex;
    }

    public void ensureSize(int length) {
        if (this.nioBuffer.capacity() < this.writeIndex + length) {
            final int newCapacity = Math.max(this.nioBuffer.capacity() * 2, this.writeIndex + length);
            final ByteBuffer newBuffer = ByteBuffer.allocateDirect(newCapacity);
            this.nioBuffer.position(0);
            newBuffer.put(this.nioBuffer);
            this.nioBuffer = newBuffer.clear();
        }
    }

    public interface Type<T> {

        @NotNull TypeWriter<T> writer();

        @NotNull TypeReader<T> reader();
    }

    @FunctionalInterface
    public interface Writer {

        void write(@NotNull ReplayByteBuffer writer);
    }

    public interface TypeWriter<T> {

        long write(@NotNull ReplayByteBuffer buffer, @UnknownNullability T value);
    }

    public interface TypeReader<T> {

        @UnknownNullability T read(@NotNull ReplayByteBuffer buffer);
    }
}