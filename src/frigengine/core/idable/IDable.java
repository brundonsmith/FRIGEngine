package frigengine.core.idable;

public abstract class IDable<E> {
	// Attributes
	private E id;
	private IDableCollection<E, ? extends IDable<E>> idableCollection;

	// Constructors and initialization
	public IDable() {
		this.id = null;
		this.idableCollection = null;
	}
	
	// Getters and setters
	public E getId() {
		return id;
	}
	protected void setId(E id) {
		E oldId = this.id;
		this.id = id;
		if(this.idableCollection != null) {
			this.idableCollection.reposition(oldId);
		}
	}
	/*package*/ void setIDableCollection(IDableCollection<E, ? extends IDable<E>> idableCollection) {
		this.idableCollection = idableCollection;
	}

	// Utilities
	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof IDable<?> && this.id.equals(((IDable<E>)other).id);
	}
}
