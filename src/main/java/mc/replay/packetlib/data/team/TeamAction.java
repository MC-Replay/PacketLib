package mc.replay.packetlib.data.team;

import mc.replay.packetlib.network.PacketBuffer;
import mc.replay.packetlib.utils.AdventurePacketConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static mc.replay.packetlib.network.PacketBuffer.*;

public interface TeamAction extends PacketBuffer.Writer {

    int id();

    record CreateTeamAction(Component displayName, byte friendlyFlags, NameTagVisibility nameTagVisibility,
                            CollisionRule collisionRule, NamedTextColor teamColor, Component teamPrefix,
                            Component teamSuffix, Collection<String> entities) implements TeamAction {

        public CreateTeamAction {
            entities = List.copyOf(entities);
        }

        public CreateTeamAction(@NotNull PacketBuffer reader) {
            this(
                    reader.read(COMPONENT),
                    reader.read(BYTE),
                    NameTagVisibility.fromIdentifier(reader.read(STRING)),
                    CollisionRule.fromIdentifier(reader.read(STRING)),
                    NamedTextColor.namedColor(reader.read(VAR_INT)),
                    reader.read(COMPONENT),
                    reader.read(COMPONENT),
                    reader.readCollection(STRING)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.write(COMPONENT, this.displayName);
            writer.write(BYTE, this.friendlyFlags);
            writer.write(STRING, this.nameTagVisibility.getIdentifier());
            writer.write(STRING, this.collisionRule.getIdentifier());
            writer.write(VAR_INT, AdventurePacketConverter.getNamedTextColorValue(this.teamColor));
            writer.write(COMPONENT, this.teamPrefix);
            writer.write(COMPONENT, this.teamSuffix);
            writer.writeCollection(STRING, this.entities);
        }

        @Override
        public int id() {
            return 0;
        }
    }

    record RemoveTeamAction() implements TeamAction {

        @Override
        public void write(@NotNull PacketBuffer writer) {
        }

        @Override
        public int id() {
            return 1;
        }
    }

    record UpdateTeamAction(Component displayName, byte friendlyFlags, NameTagVisibility nameTagVisibility,
                            CollisionRule collisionRule, NamedTextColor teamColor, Component teamPrefix,
                            Component teamSuffix) implements TeamAction {

        public UpdateTeamAction(@NotNull PacketBuffer reader) {
            this(
                    reader.read(COMPONENT),
                    reader.read(BYTE),
                    NameTagVisibility.fromIdentifier(reader.read(STRING)),
                    CollisionRule.fromIdentifier(reader.read(STRING)),
                    NamedTextColor.namedColor(reader.read(VAR_INT)),
                    reader.read(COMPONENT),
                    reader.read(COMPONENT)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.write(COMPONENT, this.displayName);
            writer.write(BYTE, this.friendlyFlags);
            writer.write(STRING, this.nameTagVisibility.getIdentifier());
            writer.write(STRING, this.collisionRule.getIdentifier());
            writer.write(VAR_INT, AdventurePacketConverter.getNamedTextColorValue(this.teamColor));
            writer.write(COMPONENT, this.teamPrefix);
            writer.write(COMPONENT, this.teamSuffix);
        }

        @Override
        public int id() {
            return 2;
        }
    }

    record AddEntitiesToTeamAction(@NotNull Collection<@NotNull String> entities) implements TeamAction {

        public AddEntitiesToTeamAction {
            entities = List.copyOf(entities);
        }

        public AddEntitiesToTeamAction(@NotNull PacketBuffer reader) {
            this(
                    reader.readCollection(STRING)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.writeCollection(STRING, this.entities);
        }

        @Override
        public int id() {
            return 3;
        }
    }

    record RemoveEntitiesFromTeamAction(@NotNull Collection<@NotNull String> entities) implements TeamAction {

        public RemoveEntitiesFromTeamAction {
            entities = List.copyOf(entities);
        }

        public RemoveEntitiesFromTeamAction(@NotNull PacketBuffer reader) {
            this(
                    reader.readCollection(STRING)
            );
        }

        @Override
        public void write(@NotNull PacketBuffer writer) {
            writer.writeCollection(STRING, this.entities);
        }

        @Override
        public int id() {
            return 4;
        }
    }
}