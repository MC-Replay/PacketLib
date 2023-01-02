package mc.replay.packetlib.network.packet.serverbound;

import mc.replay.packetlib.PacketLib;
import mc.replay.packetlib.network.packet.identifier.PacketIdentifier;

public enum ServerboundPacketIdentifier implements PacketIdentifier {

    TELEPORT_CONFIRM,
    QUERY_BLOCK_ENTITY_NBT,
    CHANGE_DIFFICULTY,
    CHAT_ACKNOWLEDGE,
    COMMAND_CHAT,
    CHAT_MESSAGE,
    STATUS,
    SETTINGS,
    TAB_COMPLETE,
    CLICK_WINDOW_BUTTON,
    CLICK_WINDOW,
    CLOSE_WINDOW,
    PLUGIN_MESSAGE,
    EDIT_BOOK,
    QUERY_ENTITY_NBT,
    INTERACT_ENTITY,
    GENERATE_STRUCTURE,
    KEEP_ALIVE,
    LOCK_DIFFICULTY,
    PLAYER_POSITION,
    PLAYER_POSITION_AND_ROTATION,
    PLAYER_ROTATION,
    PLAYER_ON_GROUND,
    VEHICLE_MOVE,
    STEER_BOAT,
    PICK_ITEM,
    CRAFT_RECIPE,
    PLAYER_ABILITIES,
    PLAYER_DIGGING,
    ENTITY_ACTION,
    STEER_VEHICLE,
    PONG,
    CHAT_SESSION_UPDATE,
    SET_RECIPE_BOOK_STATE,
    SET_DISPLAYED_RECIPE,
    NAME_ITEM,
    RESOURCE_PACK_STATUS,
    ADVANCEMENT_TAB,
    SELECT_TRADE,
    SET_BEACON_EFFECT,
    HELD_ITEM_CHANGE,
    UPDATE_COMMAND_BLOCK,
    UPDATE_COMMAND_BLOCK_MINECART,
    CREATIVE_INVENTORY_ACTION,
    UPDATE_JIGSAW_BLOCK,
    UPDATE_STRUCTURE_BLOCK,
    UPDATE_SIGN,
    ANIMATION,
    SPECTATE,
    PLAYER_BLOCK_PLACEMENT,
    USE_ITEM,

    CHAT_PREVIEW_759_760,
    WINDOW_CONFIRMATION_754;

    @Override
    public int getIdentifier() {
        return PacketLib.getInstance().getPacketIdentifierLoader().getServerboundIdentifiers().getOrDefault(this, -1);
    }
}