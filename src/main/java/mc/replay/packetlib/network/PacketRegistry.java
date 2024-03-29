package mc.replay.packetlib.network;

import mc.replay.packetlib.network.packet.PacketInfo;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacket;
import mc.replay.packetlib.network.packet.clientbound.ClientboundPacketIdentifier;
import mc.replay.packetlib.network.packet.clientbound.play.*;
import mc.replay.packetlib.network.packet.clientbound.play.legacy.*;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifier;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacket;
import mc.replay.packetlib.network.packet.serverbound.ServerboundPacketIdentifier;
import mc.replay.packetlib.network.packet.serverbound.play.*;
import mc.replay.packetlib.utils.ProtocolVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class PacketRegistry {

    private final Map<ServerboundPacketIdentifier, PacketDefinition<ServerboundPacketIdentifier, ? extends ServerboundPacket>> serverboundPacketRegistry = new HashMap<>();
    private final Map<ClientboundPacketIdentifier, PacketDefinition<ClientboundPacketIdentifier, ? extends ClientboundPacket>> clientboundPacketRegistry = new HashMap<>();

    public PacketRegistry() {
        this.registerClientboundPacket(ClientboundPacketIdentifier.ACKNOWLEDGE_BLOCK_CHANGE, ClientboundAcknowledgeBlockChangePacket.class, ClientboundAcknowledgeBlockChangePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.BLOCK_ACTION, ClientboundBlockActionPacket.class, ClientboundBlockActionPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.BLOCK_BREAK_ANIMATION, ClientboundBlockBreakAnimationPacket.class, ClientboundBlockBreakAnimationPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.BLOCK_CHANGE, ClientboundBlockChangePacket.class, ClientboundBlockChangePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.BLOCK_ENTITY_DATA, ClientboundBlockEntityDataPacket.class, ClientboundBlockEntityDataPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.COLLECT_ITEM, ClientboundCollectItemPacket.class, ClientboundCollectItemPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.CUSTOM_CHAT_COMPLETIONS, ClientboundCustomChatCompletionPacket.class, ClientboundCustomChatCompletionPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.DELETE_CHAT_MESSAGE, ClientboundDeleteChatPacket.class, ClientboundDeleteChatPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.DISGUISED_CHAT, ClientboundDisguisedChatPacket.class, ClientboundDisguisedChatPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_ANIMATION, ClientboundEntityAnimationPacket.class, ClientboundEntityAnimationPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_ATTACH, ClientboundEntityAttachPacket.class, ClientboundEntityAttachPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_DESTROY, ClientboundEntityDestroyPacket.class, ClientboundEntityDestroyPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_EFFECT, ClientboundEntityEffectPacket.class, ClientboundEntityEffectPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_EQUIPMENT, ClientboundEntityEquipmentPacket.class, ClientboundEntityEquipmentPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_HEAD_ROTATION, ClientboundEntityHeadRotationPacket.class, ClientboundEntityHeadRotationPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_METADATA, ClientboundEntityMetadataPacket.class, ClientboundEntityMetadataPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_POSITION_AND_ROTATION, ClientboundEntityPositionAndRotationPacket.class, ClientboundEntityPositionAndRotationPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_PROPERTIES, ClientboundEntityPropertiesPacket.class, ClientboundEntityPropertiesPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_REMOVE_EFFECT, ClientboundEntityRemoveEffectPacket.class, ClientboundEntityRemoveEffectPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_ROTATION, ClientboundEntityRotationPacket.class, ClientboundEntityRotationPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_SOUND_EFFECT, ClientboundEntitySoundEffectPacket.class, ClientboundEntitySoundEffectPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_SPAWN, ClientboundEntitySpawnPacket.class, ClientboundEntitySpawnPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_STATUS, ClientboundEntityStatusPacket.class, ClientboundEntityStatusPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_TELEPORT, ClientboundEntityTeleportPacket.class, ClientboundEntityTeleportPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.ENTITY_VELOCITY, ClientboundEntityVelocityPacket.class, ClientboundEntityVelocityPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.EXPERIENCE_ORB_SPAWN, ClientboundExperienceOrbSpawnPacket.class, ClientboundExperienceOrbSpawnPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.HELD_ITEM_CHANGE, ClientboundHeldItemChangePacket.class, ClientboundHeldItemChangePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.MULTI_BLOCK_CHANGE, ClientboundMultiBlockChangePacket.class, ClientboundMultiBlockChangePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.PARTICLE, ClientboundParticlePacket.class, ClientboundParticlePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.PLAYER_INFO_REMOVE, ClientboundPlayerInfoRemovePacket.class, ClientboundPlayerInfoRemovePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.PLAYER_INFO, ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.PLAYER_POSITION_AND_LOOK, ClientboundPlayerPositionAndLookPacket.class, ClientboundPlayerPositionAndLookPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.PLAYER_SPAWN, ClientboundPlayerSpawnPacket.class, ClientboundPlayerSpawnPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.SET_COOLDOWN, ClientboundSetCooldownPacket.class, ClientboundSetCooldownPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.SET_PASSENGERS, ClientboundSetPassengersPacket.class, ClientboundSetPassengersPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.SET_SLOT, ClientboundSetSlotPacket.class, ClientboundSetSlotPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.SOUND_EFFECT, ClientboundSoundEffectPacket.class, ClientboundSoundEffectPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.STATISTICS, ClientboundStatisticsPacket.class, ClientboundStatisticsPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.STOP_SOUND, ClientboundStopSoundPacket.class, ClientboundStopSoundPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.SYSTEM_CHAT, ClientboundSystemChatMessagePacket.class, ClientboundSystemChatMessagePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.TAB_COMPLETE, ClientboundTabCompletePacket.class, ClientboundTabCompletePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.TEAMS, ClientboundTeamsPacket.class, ClientboundTeamsPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.TIME_UPDATE, ClientboundTimeUpdatePacket.class, ClientboundTimeUpdatePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.UPDATE_HEALTH, ClientboundUpdateHealthPacket.class, ClientboundUpdateHealthPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.VEHICLE_MOVE, ClientboundVehicleMovePacket.class, ClientboundVehicleMovePacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.WINDOW_ITEMS, ClientboundWindowItemsPacket.class, ClientboundWindowItemsPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.WINDOW_PROPERTY, ClientboundWindowPropertyPacket.class, ClientboundWindowPropertyPacket::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.WORLD_EVENT, ClientboundWorldEventPacket.class, ClientboundWorldEventPacket::new);

        this.registerClientboundPacket(ClientboundPacketIdentifier.ACKNOWLEDGE_PLAYER_DIGGING_754_758, ClientboundAcknowledgePlayerDigging754_758Packet.class, ClientboundAcknowledgePlayerDigging754_758Packet::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.CUSTOM_SOUND_EFFECT_754_760, ClientboundCustomSoundEffect_754_758Packet.class, ClientboundCustomSoundEffect_754_758Packet::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.SPAWN_LIVING_ENTITY_754_758, ClientboundLivingEntitySpawn754_758Packet.class, ClientboundLivingEntitySpawn754_758Packet::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.SPAWN_PAINTING_754_758, ClientboundPaintingSpawn754_758Packet.class, ClientboundPaintingSpawn754_758Packet::new);
        this.registerClientboundPacket(ClientboundPacketIdentifier.PLAYER_INFO_754_758, ClientboundPlayerInfo754_758Packet.class, ClientboundPlayerInfo754_758Packet::new);

        this.registerServerboundPacket(ServerboundPacketIdentifier.ANIMATION, ServerboundAnimationPacket.class, ServerboundAnimationPacket::new);
        this.registerServerboundPacket(ServerboundPacketIdentifier.INTERACT_ENTITY, ServerboundInteractEntityPacket.class, ServerboundInteractEntityPacket::new);
        this.registerServerboundPacket(ServerboundPacketIdentifier.PLAYER_BLOCK_PLACEMENT, ServerboundPlayerBlockPlacementPacket.class, ServerboundPlayerBlockPlacementPacket::new);
        this.registerServerboundPacket(ServerboundPacketIdentifier.PLAYER_DIGGING, ServerboundPlayerDiggingPacket.class, ServerboundPlayerDiggingPacket::new);
        this.registerServerboundPacket(ServerboundPacketIdentifier.PLAYER_POSITION_AND_ROTATION, ServerboundPlayerPositionAndRotationPacket.class, ServerboundPlayerPositionAndRotationPacket::new);
        this.registerServerboundPacket(ServerboundPacketIdentifier.PLAYER_POSITION, ServerboundPlayerPositionPacket.class, ServerboundPlayerPositionPacket::new);
        this.registerServerboundPacket(ServerboundPacketIdentifier.PLAYER_ROTATION, ServerboundPlayerRotationPacket.class, ServerboundPlayerRotationPacket::new);
    }

    public boolean isClientboundRegistered(@Nullable ClientboundPacketIdentifier identifier) {
        return identifier != null && this.clientboundPacketRegistry.containsKey(identifier);
    }

    public boolean isClientboundRegistered(int packetId) {
        return this.isClientboundRegistered(PacketIdentifier.getPacketIdentifier(ClientboundPacketIdentifier.class, packetId));
    }

    public boolean isServerboundRegistered(@Nullable ServerboundPacketIdentifier identifier) {
        return identifier != null && this.serverboundPacketRegistry.containsKey(identifier);
    }

    public boolean isServerboundRegistered(int packetId) {
        return this.isServerboundRegistered(PacketIdentifier.getPacketIdentifier(ServerboundPacketIdentifier.class, packetId));
    }

    @SuppressWarnings("unchecked")
    public <P extends ClientboundPacket> @Nullable P getClientboundPacket(@NotNull ClientboundPacketIdentifier identifier, @NotNull ReplayByteBuffer reader) {
        PacketDefinition<ClientboundPacketIdentifier, ?> packetDefinition = this.clientboundPacketRegistry.get(identifier);
        if (packetDefinition == null) return null;

        return (P) packetDefinition.construct(reader);
    }

    public <P extends ClientboundPacket> @Nullable P getClientboundPacket(int packetId, @NotNull ReplayByteBuffer reader) {
        ClientboundPacketIdentifier packetIdentifier = PacketIdentifier.getPacketIdentifier(ClientboundPacketIdentifier.class, packetId);
        if (packetIdentifier == null) return null;

        return this.getClientboundPacket(packetIdentifier, reader);
    }

    @SuppressWarnings("unchecked")
    public <P extends ServerboundPacket> @Nullable P getServerboundPacket(@NotNull ServerboundPacketIdentifier identifier, @NotNull ReplayByteBuffer reader) {
        PacketDefinition<ServerboundPacketIdentifier, ?> packetDefinition = this.serverboundPacketRegistry.get(identifier);
        if (packetDefinition == null) return null;

        return (P) packetDefinition.construct(reader);
    }

    public <P extends ServerboundPacket> @Nullable P getServerboundPacket(int packetId, @NotNull ReplayByteBuffer reader) {
        ServerboundPacketIdentifier packetIdentifier = PacketIdentifier.getPacketIdentifier(ServerboundPacketIdentifier.class, packetId);
        if (packetIdentifier == null) return null;

        return this.getServerboundPacket(packetIdentifier, reader);
    }

    private <P extends ClientboundPacket> void registerClientboundPacket(@NotNull ClientboundPacketIdentifier identifier,
                                                                         @NotNull Class<P> packetClass,
                                                                         @NotNull Function<@NotNull ReplayByteBuffer, @NotNull P> packetConstructor) {
        if (!this.validatePacketInfo(packetClass)) return;

        this.clientboundPacketRegistry.put(identifier, new PacketDefinition<>(identifier, packetClass, packetConstructor));
    }

    private <P extends ServerboundPacket> void registerServerboundPacket(@NotNull ServerboundPacketIdentifier identifier,
                                                                         @NotNull Class<P> packetClass,
                                                                         @NotNull Function<@NotNull ReplayByteBuffer, @NotNull P> packetConstructor) {
        if (!this.validatePacketInfo(packetClass)) return;

        this.serverboundPacketRegistry.put(identifier, new PacketDefinition<>(identifier, packetClass, packetConstructor));
    }

    private boolean validatePacketInfo(Class<?> packetClass) {
        PacketInfo annotation = packetClass.getAnnotation(PacketInfo.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Packet class " + packetClass.getName() + " is missing @PacketInfo annotation");
        }

        // Prevent conflicts with newer and older packets
        return !annotation.since().isHigher(ProtocolVersion.getServerVersion())
                && !annotation.until().isLower(ProtocolVersion.getServerVersion());
    }
}