package frigengine.core.exceptions.component;

import frigengine.core.component.Component;
import frigengine.core.component.Entity;

@SuppressWarnings("serial")
public class ComponentNotFoundException extends RuntimeException {
	public ComponentNotFoundException(Entity entity, Class<? extends Component> component) {
		super("Entity '" + entity.getId() + "' does not have a component of the type " + component.getSimpleName());
	}
}
