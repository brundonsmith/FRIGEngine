package frigengine.battle;

public enum HealthMagicEffect implements Effect {
	;

	// Attributes
	private ApplicationMethod applicationMethod;
	private ModificationType modificationType;
	private float healthModifier;
	private float magicModifier;

	// Constructors and initialization
	
	// Operations
	public int modifyHealth(int currentHealth) {
		switch(this.modificationType) {
		case INCREMENT:
			return currentHealth + (int)this.healthModifier;
		case SCALE:
			return (int)Math.round(currentHealth * this.healthModifier);
		default:
			return currentHealth;
		}
	}
	public int modifyMagic(int currentMagic) {
		switch(this.modificationType) {
		case INCREMENT:
			return currentMagic + (int)this.magicModifier;
		case SCALE:
			return (int)Math.round(currentMagic * this.magicModifier);
		default:
			return currentMagic;
		}
	}
	
	// Getters and setters
	@Override
	public ApplicationMethod getApplicationMethod() {
		return this.applicationMethod;
	}
	@Override
	public ModificationType getModificationType() {
		return this.modificationType;
	}
}
