package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.entity.attribute.AttributeValue;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundEntityPropertiesPacket(int entityId,
                                                @NotNull List<AttributeValue> properties) implements ClientboundPacket {

    public ClientboundEntityPropertiesPacket {
        properties = List.copyOf(properties);
    }

    public ClientboundEntityPropertiesPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.readCollection(r -> {
                    Attribute attribute = getAttribute(r.read(STRING));
                    double value = r.read(DOUBLE);
                    int modifierCount = r.read(VAR_INT);

                    AttributeValue attributeValue = new AttributeValue(attribute, value, new HashSet<>());
                    for (int i = 0; i < modifierCount; i++) {
                        AttributeModifier modifier = new AttributeModifier(
                                r.read(UUID),
                                "",
                                r.read(DOUBLE),
                                r.readEnum(AttributeModifier.Operation.class)
                        );
                        attributeValue.modifiers().add(modifier);
                    }

                    return attributeValue;
                })
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.writeCollection(this.properties, (w, property) -> {
            w.write(STRING, property.attribute().getKey().getKey());
            w.write(DOUBLE, property.value());

            {
                w.writeCollection(property.modifiers(), (w1, modifier) -> {
                    w1.write(UUID, modifier.getUniqueId());
                    w1.write(DOUBLE, modifier.getAmount());
                    w1.writeEnum(AttributeModifier.Operation.class, modifier.getOperation());
                });
            }
        });
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_PROPERTIES;
    }

    private static @Nullable Attribute getAttribute(@NotNull String key) {
        for (Attribute attribute : Attribute.values()) {
            if (attribute.getKey().getKey().equals(key)) {
                return attribute;
            }
        }

        return null;
    }
}