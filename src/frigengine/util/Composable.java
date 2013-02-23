package frigengine.util;

import java.util.Iterator;

public abstract class Composable<E extends Component> extends IDable implements Iterable<E> {
	// Attributes
	private IDableCollection<E> components = new IDableCollection<E>();

	// Other methods
	public void addComponent(E component) {
		components.add(component);
	}

	public E getComponent(String id) {
		return components.get(id);
	}

	public boolean hasComponent(String id) {
		return components.contains(id);
	}

	public Iterator<E> iterator() {
		return components.iterator();
	}
}
