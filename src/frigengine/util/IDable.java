package frigengine.util;

public abstract class IDable<E> {
	// Attributes
	protected E id;

	// Getters and setters
	public E getId() {
		return id;
	}

	// Utilities
	public boolean equals(IDable<E> other) {
		return this.id.equals(other.id);
	}
}
