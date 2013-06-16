package frigengine.entities;

import org.newdawn.slick.util.xml.XMLElement;

import frigengine.util.IDable;
import frigengine.util.Initializable;

public abstract class Component extends IDable<Class<? extends Component>> implements Initializable {
	// Constructors and initialization
	public Component() {
		// Auto-assign ID
		this.id = this.getClass();
	}
	public abstract void init(XMLElement xmlElement);
}
