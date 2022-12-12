package mc.replay.packetlib.utils;

import com.github.steveice10.opennbt.tag.builtin.IntArrayTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class NBTUtils {

    private NBTUtils() {
    }

    public static @NotNull Tag uuidToTag(@NotNull String tagName, @NotNull UUID uuid) {
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();

        int[] intArray = new int[]{
                (int) (mostSignificantBits >> 32),
                (int) mostSignificantBits,
                (int) (leastSignificantBits >> 32),
                (int) leastSignificantBits
        };

        return new IntArrayTag(tagName, intArray);
    }

    public static @NotNull UUID uuidFromTag(@NotNull Tag tag) {
        if (!(tag instanceof IntArrayTag intArrayTag)) {
            throw new IllegalArgumentException("Expected UUID-Tag to be of type IntArrayTag, but found " + tag.getClass().getSimpleName() + ".");
        }

        int[] intArray = intArrayTag.getValue();
        if (intArray.length != 4) {
            throw new IllegalArgumentException("Expected UUID-Array to be of length 4, but found " + intArray.length + ".");
        }

        return new UUID(
                (long) intArray[0] << 32 | (long) intArray[1] & 4294967295L,
                (long) intArray[2] << 32 | (long) intArray[3] & 4294967295L
        );
    }
}