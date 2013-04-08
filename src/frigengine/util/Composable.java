package frigengine.util;

import java.util.Iterator;

public abstract class Composable<C extends Component, I> extends IDable<I> implements Iterable<C> {
	// Attributes
	private IDableCollection<Class<?>, C> components = new IDableCollection<Class<?>, C>();

	// Other methods
	public void addComponent(C component) {
		components.add(component);
	}
	public C getComponent(Class<?> id) {
		return components.get(id);
	}
	public boolean hasComponent(Class<?> id) {
		return components.contains(id);
	}
	public Iterator<C> iterator() {
		return components.iterator();
	}
}
