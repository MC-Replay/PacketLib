package mc.replay.packetlib.data.entity.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public record AttributeValue(@NotNull Attribute attribute, double value,
                             @NotNull Collection<AttributeModifier> modifiers) {
}