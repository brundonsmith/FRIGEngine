package frigengine.util;

public abstract class IDable {
	// Attributes
	protected String id;

	// Constructors and initialization
	public String getID() {
		return id;
	}

	// Other methods
	public boolean equals(IDable other) {
		return this.id.equals(other.id);
	}

	public static String iDFromPath(String contentPath) {
		return contentPath.split("/")[contentPath.split("/").length - 1].split("\\.")[0];
	}
}
