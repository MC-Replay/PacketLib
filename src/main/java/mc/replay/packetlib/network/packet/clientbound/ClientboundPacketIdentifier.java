package mc.replay.packetlib.network.packet.clientbound;

import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifier;

public enum ClientboundPacketIdentifier implements PacketIdentifier {

    BUNDLE_DELIMITER,
    ENTITY_SPAWN,
    EXPERIENCE_ORB_SPAWN,
    PLAYER_SPAWN,
    ENTITY_ANIMATION,
    STATISTICS,
    ACKNOWLEDGE_BLOCK_CHANGE,
    BLOCK_BREAK_ANIMATION,
    BLOCK_ENTITY_DATA,
    BLOCK_ACTION,
    BLOCK_CHANGE,
    BOSS_BAR,
    SERVER_DIFFICULTY,
    CHUNK_BIOMES,
    CLEAR_TITLES,
    TAB_COMPLETE,
    DECLARE_COMMANDS,
    CLOSE_WINDOW,
    WINDOW_ITEMS,
    WINDOW_PROPERTY,
    SET_SLOT,
    SET_COOLDOWN,
    CUSTOM_CHAT_COMPLETIONS,
    PLUGIN_MESSAGE,
    DAMAGE_EVENT,
    CUSTOM_SOUND_EFFECT_754_760,
    DELETE_CHAT_MESSAGE,
    DISCONNECT,
    DISGUISED_CHAT,
    ENTITY_STATUS,
    EXPLOSION,
    UNLOAD_CHUNK,
    CHANGE_GAME_STATE,
    OPEN_HORSE_WINDOW,
    HURT_ANIMATION,
    INITIALIZE_WORLD_BORDER,
    KEEP_ALIVE,
    CHUNK_DATA,
    WORLD_EVENT,
    PARTICLE,
    UPDATE_LIGHT,
    JOIN_GAME,
    MAP_DATA,
    TRADE_LIST,
    ENTITY_POSITION,
    ENTITY_POSITION_AND_ROTATION,
    ENTITY_ROTATION,
    VEHICLE_MOVE,
    OPEN_BOOK,
    OPEN_WINDOW,
    OPEN_SIGN_EDITOR,
    PING,
    CRAFT_RECIPE_RESPONSE,
    PLAYER_ABILITIES,
    PLAYER_CHAT_HEADER,
    PLAYER_CHAT,
    END_COMBAT_EVENT,
    ENTER_COMBAT_EVENT,
    DEATH_COMBAT_EVENT,
    PLAYER_INFO_REMOVE,
    PLAYER_INFO,
    FACE_PLAYER,
    PLAYER_POSITION_AND_LOOK,
    UNLOCK_RECIPES,
    ENTITY_DESTROY,
    ENTITY_REMOVE_EFFECT,
    RESOURCE_PACK_SEND,
    RESPAWN,
    ENTITY_HEAD_ROTATION,
    MULTI_BLOCK_CHANGE,
    SELECT_ADVANCEMENT_TAB,
    SERVER_DATA,
    ACTION_BAR,
    WORLD_BORDER_CENTER,
    WORLD_BORDER_LERP_SIZE,
    WORLD_BORDER_SIZE,
    WORLD_BORDER_WARNING_DELAY,
    WORLD_BORDER_WARNING_REACH,
    CAMERA,
    HELD_ITEM_CHANGE,
    UPDATE_VIEW_POSITION,
    UPDATE_VIEW_DISTANCE,
    SPAWN_POSITION,
    DISPLAY_SCOREBOARD,
    ENTITY_METADATA,
    ENTITY_ATTACH,
    ENTITY_VELOCITY,
    ENTITY_EQUIPMENT,
    SET_EXPERIENCE,
    UPDATE_HEALTH,
    SCOREBOARD_OBJECTIVE,
    SET_PASSENGERS,
    TEAMS,
    UPDATE_SCORE,
    SET_SIMULATION_DISTANCE,
    SET_SUBTITLE_TEXT,
    TIME_UPDATE,
    SET_TITLE_TEXT,
    SET_TITLE_TIME,
    ENTITY_SOUND_EFFECT,
    SOUND_EFFECT,
    STOP_SOUND,
    SYSTEM_CHAT,
    PLAYER_LIST_HEADER_AND_FOOTER,
    NBT_QUERY_RESPONSE,
    COLLECT_ITEM,
    ENTITY_TELEPORT,
    ADVANCEMENTS,
    ENTITY_PROPERTIES,
    UPDATE_ENABLED_FEATURES,
    ENTITY_EFFECT,
    DECLARE_RECIPES,
    TAGS,

    CHAT_PREVIEW_759_760,
    SET_DISPLAY_CHAT_PREVIEW_759_760,
    ADD_VIBRATION_SIGNAL_755_758,
    ACKNOWLEDGE_PLAYER_DIGGING_754_758,
    SPAWN_LIVING_ENTITY_754_758,
    SPAWN_PAINTING_754_758,
    PLAYER_INFO_754_758,
    WINDOW_CONFIRMATION_754,
    COMBAT_EVENT_754,
    WORLD_BORDER_754,
    TITLE_754;

    @Override
    public int getIdentifier() {
        return PacketLib.getPacketIdentifierLoader().getClientboundIdentifiers().getOrDefault(this, -1);
    }
}