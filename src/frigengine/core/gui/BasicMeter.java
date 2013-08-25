package frigengine.core.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import frigengine.core.geom.*;

public class BasicMeter extends AbstractMeter {
	// Attributes
	private Rectangle filledSection;
	private Color outlineColor;
	private Color fillColor;
	
	// Constructors and initialization
	public BasicMeter(Rectangle domain, int capacity, Color fillColor) {
		super(domain, capacity);
		
		// filledSection
		this.filledSection = new Rectangle(this.domain.getX(), this.domain.getY(), 0, this.domain.getHeight());
		
		// outlineColor
		this.outlineColor = Color.black;
		
		// fillColor
		this.fillColor = fillColor;
	}
	public BasicMeter(Rectangle domain, int capacity, Color fillColor, Color outlineColor) {
		super(domain, capacity);
		
		// filledSection
		this.filledSection = new Rectangle(this.domain.getX(), this.domain.getY(), 0, this.domain.getHeight());

		// outlineColor
		this.outlineColor = outlineColor;
		
		// fillColor
		this.fillColor = fillColor;
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		this.filledSection.setWidth(this.getPercentage() * this.domain.getWidth());
	}
	@Override
	public void render(Graphics g, GUIContext context) {
		this.context = context;
		
		Color graphicsColor = g.getColor();
		
		g.setColor(this.outlineColor);
		this.context.drawShape(g, this.domain);
		g.setColor(this.fillColor);
		this.context.fillShape(g, this.filledSection);
		
		g.setColor(graphicsColor);
	}
}
