package frigengine.core.gui;

import frigengine.core.geom.*;

public abstract class AbstractMeter extends GUIFrame {
	// Attributes
	protected int value;
	protected int capacity;
	
	// Constructors and initialization
	public AbstractMeter(Rectangle domain, int capacity) {
		super(domain);
		// TODO Change all rectangles to slick?
		// value
		this.value = 0;
		
		// capacity
		this.capacity = capacity;
	}

	// Getters and setters
	public int getValue() {
		return this.value;
	}
	public void setValue(int value) {
		this.value = Math.max(Math.min(value, this.capacity), 0);
	}
	public float getPercentage() {
		return Math.min((float)this.value / (float)this.capacity, 1.0f);
	}
	public int getCapacity() {
		return this.capacity;
	}
	
	// Controls
	public void increase(int amount) {
		this.setValue(this.getValue() + amount);
	}
	public void decrease(int amount) {
		this.setValue(this.getValue() - amount);
	}
	public void empty() {
		this.setValue(0);
	}
	public void fill() {
		this.setValue(this.getCapacity());
	}
}
