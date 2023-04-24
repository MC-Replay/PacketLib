package mc.replay.packetlib.network.packet.clientbound.play;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import mc.replay.packetlib.network.ReplayByteBuffer;
import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static mc.replay.packetlib.network.ReplayByteBuffer.*;

@PacketInfo
public record ClientboundEntityEffectPacket(int entityId, int effectId, byte amplifier, int duration, byte flags,
                                            @Nullable CompoundTag factorCodec) implements ClientboundPacket {

    private static final byte AMBIENT_FLAG = 0x01;
    private static final byte SHOW_PARTICLES_FLAG = 0x02;
    private static final byte ICON_FLAG = 0x04;

    public ClientboundEntityEffectPacket(int entityId, @NotNull PotionEffect effect, @Nullable CompoundTag factorCodec) {
        this(
                entityId,
                effect.getType().getId(),
                (byte) effect.getAmplifier(),
                effect.getDuration(),
                (byte) ((effect.isAmbient() ? AMBIENT_FLAG : 0) | (effect.hasParticles() ? SHOW_PARTICLES_FLAG : 0) | (effect.hasIcon() ? ICON_FLAG : 0)),
                factorCodec
        );
    }

    public ClientboundEntityEffectPacket(@NotNull ReplayByteBuffer reader) {
        this(
                reader.read(VAR_INT),
                reader.read(VAR_INT),
                reader.read(BYTE),
                reader.read(VAR_INT),
                reader.read(BYTE),
                reader.read(BOOLEAN)
                        ? (CompoundTag) reader.read(NBT)
                        : null
        );
    }

    @Override
    public void write(@NotNull ReplayByteBuffer writer) {
        writer.write(VAR_INT, this.entityId);
        writer.write(VAR_INT, this.effectId);
        writer.write(BYTE, this.amplifier);
        writer.write(VAR_INT, this.duration);
        writer.write(BYTE, this.flags);
        writer.writeOptional(NBT, this.factorCodec);
    }

    @Override
    public @NotNull ClientboundPacketIdentifier identifier() {
        return ClientboundPacketIdentifier.ENTITY_EFFECT;
    }
}