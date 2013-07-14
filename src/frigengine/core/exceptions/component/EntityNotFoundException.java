package frigengine.core.exceptions.component;

@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException(String id) {
		super("There is no known entity with ID '" + id + "'");
	}
}
