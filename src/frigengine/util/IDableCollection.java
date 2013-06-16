package frigengine.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;

public class IDableCollection<K, V extends IDable<K>> implements Iterable<V> {
	// Attributes
	private Map<K, V> items;

	// Constructors and initialization
	public IDableCollection() {
		this.items = new Hashtable<K, V>();
	}

	// Operations
	public void put(V idable) {
		if(idable != null) {
			this.items.put(idable.getId(), idable);
		}
	}
	public void remove(K id) {
		this.items.remove(id);
	}
	public boolean contains(K id) {
		if (id != null) {
			return this.items.get(id) != null;
		} else {
			return false;
		}
	}
	public V get(K id) {
		return this.items.get(id);
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
