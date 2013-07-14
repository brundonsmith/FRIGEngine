package frigengine.battle.statseffects;

public interface Effect {
	// Getters and setters
	public ApplicationMethod getApplicationMethod();
	public ModificationType getModificationType();
	
	// Enums
	public enum ApplicationMethod {
		ONCE,
		TURN
	}
	public enum ModificationType {
		INCREMENT,
		SCALE
	}
}
