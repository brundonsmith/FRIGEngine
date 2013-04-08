package frigengine.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;

import frigengine.exceptions.IDableException;

public class IDableCollection<K, V extends IDable<K>> implements Iterable<V> {
	// Attributes
	private Map<K, V> items;

	// Constructors and initialization
	public IDableCollection() {
		this.items = new Hashtable<K, V>();
	}

	// Collection methods
	public void add(V idable) throws IDableException {
		if (this.items.get(idable.getID()) == null)
			this.items.put(idable.getID(), idable);
		else
			throw new IDableException("Object with ID '" + idable.getID()
					+ "' already exists in the collection");
	}
	public void remove(K id) {
		this.items.remove(id);
	}
	public boolean contains(K id) {
		if (id != null) {
			return this.items.containsKey(id);
		}
		return false;
	}
	public V get(K id) {
		if (this.contains(id))
			return this.items.get(id);
		else
			throw new IDableException("Object with ID '" + id
					+ "' does not exist in the collection");
	}
	public boolean isEmpty() {
		return this.items.isEmpty();
	}
	public Iterator<V> iterator() {
		return this.items.values().iterator();
	}
	public int size() {
		return this.items.size();
	}
}
