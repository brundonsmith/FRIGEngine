package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;

public class CameraComponent extends Component {
	// Required components
	@Override
	public Collection<Class<? extends Component>> requiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionComponent.class
			));
	}
	
	// Attributes
	private float width;
	private float height;
	private String centeredOn;

	// Constructors and initialization
	public CameraComponent() {
		this.width = 400;
		this.height = 300;
		this.centeredOn = null;
	}
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// width
		try {
			this.width = (float) xmlElement.getDoubleAttribute("width", this.getWidth());
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		
		// height
		try {
			this.height = (float) xmlElement.getDoubleAttribute("height", this.getHeight());
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
		
		// centeredOn
		this.centeredOn = xmlElement.getAttribute("centeredon", this.centeredOn);
	}

	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		// Stay centered on an entity if necessary
		if(this.centeredOn != null && Entity.entityExists(this.centeredOn)) {
			if(getEntity(this.centeredOn).hasComponent(SpriteComponent.class)) { // If the entity has a sprite, center on it
				getComponent(PositionComponent.class).setPosition(getEntity(this.centeredOn).getComponent(SpriteComponent.class).getWorldPresence().getCenter());
			} else if(getEntity(this.centeredOn).hasComponent(PositionComponent.class)) { // Else center on its real position
				getComponent(PositionComponent.class).setPosition(getEntity(this.centeredOn).getComponent(PositionComponent.class).getPosition());
			}
		}
	}
	
	// Getters and setters
	public float getMinX() {
		return getComponent(PositionComponent.class).getX() - (this.width / 2);
	}
	public float getMinY() {
		return getComponent(PositionComponent.class).getY() - (this.height / 2);
	}
	public float getMaxX() {
		return getComponent(PositionComponent.class).getX() + (this.width / 2);
	}
	public float getMaxY() {
		return getComponent(PositionComponent.class).getY() + (this.height / 2);
	}
	public float getWidth() {
		return this.width;
	}
	public float getHeight() {
		return this.height;
	}
	public Point getCenter() {
		return new Point(getComponent(PositionComponent.class).getX(), getComponent(PositionComponent.class).getY());
	}
	public void setCenter(Point center) {
		getComponent(PositionComponent.class).setX(center.getX());
		getComponent(PositionComponent.class).setY(center.getY());
	}
	public Rectangle getViewport() {
		return new Rectangle(
				getComponent(PositionComponent.class).getX() - (this.width / 2),
				getComponent(PositionComponent.class).getY() - (this.height / 2),
				this.width,
				this.height
				);
	}
	
	// Operations
	public void moveLeft(float increment) {
		getComponent(PositionComponent.class).setX(getComponent(PositionComponent.class).getX() - increment);
	}
	public void moveRight(float increment) {
		getComponent(PositionComponent.class).setX(getComponent(PositionComponent.class).getX() + increment);
	}
	public void moveUp(float increment) {
		getComponent(PositionComponent.class).setY(getComponent(PositionComponent.class).getY() - increment);
	}
	public void moveDown(float increment) {
		getComponent(PositionComponent.class).setY(getComponent(PositionComponent.class).getY() + increment);
	}
	public void zoom(float scale) {
		this.width *= scale;
		this.height *= scale;
	}

	// Utilities
	@Override
	public String toString() {
		return this.getCenter() + " "
				+ this.getWidth() + "x" + this.getHeight();
	}
}
