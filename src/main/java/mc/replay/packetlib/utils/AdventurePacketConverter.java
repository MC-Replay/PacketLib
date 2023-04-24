package mc.replay.packetlib.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AdventurePacketConverter {

    private static final Map<NamedTextColor, Integer> NAMED_TEXT_COLOR_ID_MAP = new HashMap<>(16);
    private static final PlainTextComponentSerializer PLAIN_TEXT_COMPONENT_SERIALIZER = PlainTextComponentSerializer.plainText();

    static {
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.BLACK, 0);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.DARK_BLUE, 1);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.DARK_GREEN, 2);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.DARK_AQUA, 3);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.DARK_RED, 4);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.DARK_PURPLE, 5);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.GOLD, 6);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.GRAY, 7);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.DARK_GRAY, 8);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.BLUE, 9);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.GREEN, 10);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.AQUA, 11);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.RED, 12);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.LIGHT_PURPLE, 13);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.YELLOW, 14);
        NAMED_TEXT_COLOR_ID_MAP.put(NamedTextColor.WHITE, 15);
    }

    public static int getNamedTextColorValue(@NotNull NamedTextColor color) {
        return NAMED_TEXT_COLOR_ID_MAP.get(color);
    }

    public static @NotNull NamedTextColor getNamedTextColorFromId(int id) {
        for (Map.Entry<NamedTextColor, Integer> entry : NAMED_TEXT_COLOR_ID_MAP.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }

        return NamedTextColor.BLACK;
    }

    public static @NotNull String asPlain(@NotNull Component component) {
        return PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(component);
    }

    public static @NotNull Component asComponent(@NotNull String plain) {
        return PLAIN_TEXT_COMPONENT_SERIALIZER.deserialize(plain);
    }
}