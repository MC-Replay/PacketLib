package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.data.entity.statistic.Statistic;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ClientboundStatisticsPacket(List<Statistic> statistics) implements ClientboundPacket {

    public ClientboundStatisticsPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.readCollection(Statistic::new)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.writeCollection(this.statistics);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.STATISTICS;
    }
}