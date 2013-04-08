package frigengine.util;

public abstract class IDable<E> {
	// Attributes
	protected E id;

	// Getters and setters
	public E getID() {
		return id;
	}

	// Utilities
	public boolean equals(IDable<E> other) {
		return this.id.equals(other.id);
	}
	public static String iDFromPath(String contentPath) {
		return contentPath.split("/")[contentPath.split("/").length - 1].split("\\.")[0];
	}
}
