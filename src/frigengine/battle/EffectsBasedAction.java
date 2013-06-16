package frigengine.battle;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.entities.Battleable;
import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.AttributeMissingException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.util.Initializable;

public class EffectsBasedAction extends Action implements Initializable {
	// Attributes
	protected Set<Effect> sourceEffects;
	protected Set<Effect> targetEffects;
	protected Set<Effect> allyEffects;
	protected Set<Effect> enemyEffects;
	
	// Constructors and initialization
	public EffectsBasedAction(String name) {
		super(name);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// id
		this.id = xmlElement.getAttribute("id", this.getId());
		
		// category
		try {
			this.category = ActionCategory.valueOf(xmlElement.getAttribute("category"));
			
			if(xmlElement.getAttribute("category") == null) {
				throw new AttributeMissingException(xmlElement.getName(), "category");
			}
		} catch(IllegalArgumentException e) {
			throw new AttributeFormatException(xmlElement.getName(), "category", xmlElement.getAttribute("category"));
		}
		
		// chargeTime
		if(xmlElement.getAttribute("charge") != null) {
			try {
				this.chargeTime = xmlElement.getIntAttribute("charge");
			} catch (SlickXMLException e) {
				throw new AttributeFormatException(xmlElement.getName(), "charge",
						xmlElement.getAttribute("charge"));
			}
		} else {
			throw new AttributeMissingException(xmlElement.getName(), "charge");
		}
		
		// sourceEffects, targetEffects, allyEffects, enemyEffects
		this.sourceEffects = new HashSet<Effect>();
		this.targetEffects = new HashSet<Effect>();
		this.allyEffects = new HashSet<Effect>();
		this.enemyEffects = new HashSet<Effect>();
		for (int i = 0; i < xmlElement.getChildrenByName(Effect.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(Effect.class.getSimpleName()).get(i);
			
			Effect newEffect;
			if(child.getAttribute("effect") != null) {
				try {
					try {
						newEffect = HealthMagicEffect.valueOf(child.getAttribute("effect").toUpperCase());
					} catch(IllegalArgumentException e) { // Isn't a valid HealthMagicEffect
						newEffect = BuffEffect.valueOf(child.getAttribute("effect"));
					}
				} catch(IllegalArgumentException e) { // Also isn't a valid BuffEffect
					throw new AttributeFormatException(child.getName(), "effect", child.getAttribute("effect"));
				}
			} else {
				throw new AttributeMissingException(child.getName(), "effect");
			}
			
			if(child.getAttribute("appliedTo") != null) {
				switch(child.getAttribute("effect")) {
				case "source":
					this.sourceEffects.add(newEffect);
					break;
				case "target":
					this.targetEffects.add(newEffect);
					break;
				case "allies":
					this.allyEffects.add(newEffect);
					break;
				case "enemies":
					this.enemyEffects.add(newEffect);
					break;
				}
			} else {
				throw new AttributeMissingException(child.getName(), "entity");
			}
		}
	}

	// Application
	@Override
	public final void apply(Battleable source, Battleable target, Set<Battleable> allies, Set<Battleable> enemies) {
		for(Effect se : this.sourceEffects) {
			source.applyStatsEffect(se);
		}
		for(Effect se : this.targetEffects) {
			target.applyStatsEffect(se);
		}
		for(Effect se : this.allyEffects) {
			for(Battleable b : allies) {
				b.applyStatsEffect(se);
			}
		}
		for(Effect se : this.enemyEffects) {
			for(Battleable b : enemies) {
				b.applyStatsEffect(se);
			}
		}
	}
}
