package frigengine.core.gui;

import org.newdawn.slick.geom.Rectangle;

public abstract class AbstractMeter extends GUIFrame {
	// Attributes
	protected int value;
	protected int capacity;
	
	// Constructors and initialization
	public AbstractMeter(Rectangle presence, int capacity) {
		super(presence);
		
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
		return (float)this.value / (float)this.capacity;
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
