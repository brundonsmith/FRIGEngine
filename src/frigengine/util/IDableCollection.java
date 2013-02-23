package frigengine.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;

public class IDableCollection<E extends IDable> implements Iterable<E> {
	// Attributes
	private Map<String, E> items;

	// Constructors and initialization
	public IDableCollection() {
		this.items = new Hashtable<String, E>();
	}

	// Other methods
	public void add(E idable) throws IDableException {
		if (items.get(idable.getID()) == null)
			this.items.put(idable.getID(), idable);
		else
			throw new IDableException("Object with ID '" + idable.getID()
					+ "' already exists in the collection");
	}

	public E get(String id) {
		if (items.get(id) != null)
			return items.get(id);
		else
			throw new IDableException("Object with ID '" + id
					+ "' does not exist in the collection");
	}

	public boolean contains(String id) {
		return items.containsKey(id);
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public Iterator<E> iterator() {
		return items.values().iterator();
	}

	public void remove(String id) {
		items.remove(id);
	}

	public int size() {
		return items.size();
	}
}
