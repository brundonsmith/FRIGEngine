package frigengine.battle;

public enum BuffEffect implements Effect {
	GROWL(ApplicationMethod.ONCE, ModificationType.INCREMENT, -5, 0, 0),
	ENTOMB(ApplicationMethod.TURN, ModificationType.INCREMENT, 0, 0, -5)
	;

	// Attributes
	private ApplicationMethod applicationMethod;
	private ModificationType modificationType;
	private float attackModifier;
	private float defenseModifier;
	private float speedModifier;
	
	// Constructors and initialization
	private BuffEffect(ApplicationMethod applicationMethod,ModificationType modificationType, float attackModifier, float defenseModifier, float speedModifier) {
		this.applicationMethod = applicationMethod;
		this.modificationType = modificationType;
		this.attackModifier = attackModifier;
		this.defenseModifier = defenseModifier;
		this.speedModifier = speedModifier;
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
	
	// Operations
	public int modifyAttack(int currentAttack) {
		switch(this.modificationType) {
		case INCREMENT:
			return currentAttack + (int)this.attackModifier;
		case SCALE:
			return (int)Math.round(currentAttack * this.attackModifier);
		default:
			return currentAttack;
		}
	}
	public int modifyDefense(int currentDefense) {
		switch(this.modificationType) {
		case INCREMENT:
			return currentDefense + (int)this.defenseModifier;
		case SCALE:
			return (int)Math.round(currentDefense * this.defenseModifier);
		default:
			return currentDefense;
		}
	}
	public int modifySpeed(int currentSpeed) {
		switch(this.modificationType) {
		case INCREMENT:
			return currentSpeed + (int)this.speedModifier;
		case SCALE:
			return (int)Math.round(currentSpeed * this.speedModifier);
		default:
			return currentSpeed;
		}
	}
}
