package frigengine.commands;

public enum Command {
	// Meta Commands
	WAIT,

	// Game Commands
	OPEN_DIALOG, CLOSE_DIALOG, CLOSE_ALL_DIALOGS, EXECUTE_SCRIPT, CHANGE_AREA, START_BATTLE,

	// Area Commands
	ADD_ENTITY_TO_AREA, REMOVE_ENTITY_FROM_AREA, SET_MUSIC, PLAY_SOUND,

	// Entity Commands
	POSITION_ENTITY, MOVE_ENTITY, GIVE_ITEM, GIVE_ITEMS, REMOVE_ITEM, REMOVE_ITEMS;

	public CommandType getType() {
		switch (this) {
		case WAIT:
			return CommandType.META_COMMAND;

		case OPEN_DIALOG:
		case CLOSE_DIALOG:
		case CLOSE_ALL_DIALOGS:
		case EXECUTE_SCRIPT:
		case CHANGE_AREA:
		case START_BATTLE:
			return CommandType.GAME_COMMAND;

		case ADD_ENTITY_TO_AREA:
		case REMOVE_ENTITY_FROM_AREA:
		case SET_MUSIC:
		case PLAY_SOUND:
			return CommandType.AREA_COMMAND;

		case POSITION_ENTITY:
		case MOVE_ENTITY:
		case GIVE_ITEM:
		case GIVE_ITEMS:
		case REMOVE_ITEM:
		case REMOVE_ITEMS:
			return CommandType.ENTITY_COMMAND;

		default:
			return CommandType.GAME_COMMAND;
		}
	}
}