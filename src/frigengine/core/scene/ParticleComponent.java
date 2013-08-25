package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;

public class ParticleComponent extends Component {
	// Required components
	@Override
	public Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class
			));
	}
	
	// Attributes
	private List<ParticleEmitter> particleEmitters;
	
	// Constructors and initialization
	public ParticleComponent() {
		this.particleEmitters = new ArrayList<ParticleEmitter>();
	}
	private ParticleComponent (ParticleComponent other) {
		super(other);
		
		this.particleEmitters = new ArrayList<ParticleEmitter>();
		for(ParticleEmitter p : other.particleEmitters) {
			this.particleEmitters.add(p);
		}
	}
	@Override
	public ParticleComponent clone() {
		return new ParticleComponent(this);
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
	}
}
