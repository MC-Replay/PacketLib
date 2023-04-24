package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.team.TeamAction;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundTeamsPacket(@NotNull String teamName,
                                     @NotNull TeamAction action) implements ClientboundPacket {

    public ClientboundTeamsPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(STRING),
                switch (reader.read(BYTE)) {
                    case 0 -> new TeamAction.CreateTeamAction(reader);
                    case 1 -> new TeamAction.RemoveTeamAction();
                    case 2 -> new TeamAction.UpdateTeamAction(reader);
                    case 3 -> new TeamAction.AddEntitiesToTeamAction(reader);
                    case 4 -> new TeamAction.RemoveEntitiesFromTeamAction(reader);
                    default -> throw new RuntimeException("Unknown action id");
                }
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(STRING, this.teamName);
        writer.write(BYTE, (byte) this.action.id());
        writer.write(this.action);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.TEAMS;
    }
}
