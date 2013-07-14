package frigengine.core.gui;

public class MenuItem {
	// Attributes
	private String label;

	// Constructors and initialization
	public MenuItem(String label) {
		this.label = label;
	}

	// Getters and setters
	public String getLabel() {
		return this.label;
	}

	// Utilities
	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof MenuItem &&
				this.label.equals(((MenuItem) other).label);
	}
	@Override
	public String toString() {
		return this.label;
	}
}
