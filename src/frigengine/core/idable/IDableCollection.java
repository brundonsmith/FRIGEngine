package frigengine.core.idable;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author Brandon An IDableCollection is essentially a hashtable in which all
 *         values are aware of their keys. This is significant for two reasons.
 *         First, it means the key is bound to the object even outside of the
 *         table. Second, it allows the object to set its own key, or ID.
 * 
 * @param <K>
 *            The type to be used as the key
 * @param <V>
 *            The type of object being referenced by keys. Must extend
 *            IDable<K>, where K is the same ID type as the one used by the
 *            table
 */
public class IDableCollection<K, V extends IDable<K>> implements Iterable<V> {
	// Attributes
	private Hashtable<K, V> table;
	
	// Constructors and initialiation
	public IDableCollection() {
		this.table = new Hashtable<K, V>();
	}
	
	// Operations
	public V add(V idable) {
		if (idable != null) {
			idable.setIDableCollection(this);
			return this.table.put(idable.getId(), idable);
		} else {
			return null;
		}
	}
	public V remove(K id) {
		return table.remove(id);
	}
	public V get(K id) {
		return this.table.get(id);
	}
	public boolean contains(K id) {
		return this.table.containsKey(id);
	}
	public boolean containsAll(Collection<K> ids) {
		for(K key : ids) {
			if(!this.table.containsKey(key)) {
				return false;
			}
		}
		return true;
	}
	public int size() {
		return this.table.size();
	}
	/*package*/ void reposition(K oldId) {
		V idable = this.remove(oldId);
		this.add(idable);
	}

	// Iterable
	@Override
	public Iterator<V> iterator() {
		return this.table.values().iterator();
	}
}
