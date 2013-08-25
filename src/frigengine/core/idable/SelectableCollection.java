package frigengine.core.idable;

public class SelectableCollection<K, V extends IDable<K>> extends IDableCollection<K,V> {
	// Attributes
	private K selectionId;
	
	// Constructors and initialization
	public SelectableCollection() {
		this.selectionId = null;
	}
	
	// Operations
	public void select(K selectionId) {
		this.selectionId = selectionId;
	}
	public K getSelectionId() {
		return this.selectionId;
	}
	public V getSelection() {
		return this.get(this.selectionId);
	}
	public boolean hasSelection() {
		return this.selectionId  != null;
	}
}
