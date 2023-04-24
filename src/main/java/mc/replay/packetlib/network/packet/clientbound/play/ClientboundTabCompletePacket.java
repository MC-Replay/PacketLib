package mc.replay.packetlib.network.packet.clientbound.play;

import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.utils.AdventurePacketConverter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

public record ClientboundTabCompletePacket(int transactionId, int start, int length,
                                           @NotNull List<Match> matches) implements ClientboundPacket {

    public ClientboundTabCompletePacket {
        matches = List.copyOf(matches);
    }

    public ClientboundTabCompletePacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(VAR_INT),
                reader.read(VAR_INT),
                reader.readCollection(Match::new)
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.transactionId);
        writer.write(VAR_INT, this.start);
        writer.write(VAR_INT, this.length);
        writer.writeCollection(this.matches);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.TAB_COMPLETE;
    }

    public record Match(@NotNull String match, @Nullable Component tooltip) implements Writer {

        public Match(@NotNull String match, @Nullable String plainTooltip) {
            this(
                    match,
                    (plainTooltip == null) ? null : AdventurePacketConverter.asComponent(plainTooltip)
            );
        }

        public Match(@NotNull ReplayByteBuffer reader) {
            this(
                    reader.read(STRING),
                    reader.readOptional(COMPONENT)
            );
        }

        public @Nullable String tooltipAsPlain() {
            return (this.tooltip == null) ? null : AdventurePacketConverter.asPlain(this.tooltip);
        }

        @Override
        public void write(@NotNull ReplayByteBuffer writer) {
            writer.write(STRING, this.match);
            writer.writeOptional(COMPONENT, this.tooltip);
        }
    }
}