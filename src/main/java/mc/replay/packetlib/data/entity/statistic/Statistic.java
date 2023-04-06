package mc.replay.packetlib.data.entity.statistic;

import mc.replay.packetlib.network.ReplayByteBuffer;
import org.jetbrains.annotations.NotNull;

import static mc.replay.packetlib.network.ReplayByteBuffer.VAR_INT;

public record Statistic(Categories categories, Statistics statistics, int value) implements ReplayByteBuffer.Writer {

    public Statistic(@NotNull ReplayByteBuffer reader) {
        this(
                reader.readEnum(Categories.class),
                reader.readEnum(Statistics.class),
                reader.read(VAR_INT)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.writeEnum(Categories.class, this.categories);
        writer.writeEnum(Statistics.class, this.statistics);
        writer.write(VAR_INT, this.value);
    }
}