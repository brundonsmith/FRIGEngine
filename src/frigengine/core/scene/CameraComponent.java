package frigengine.core.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.*;
import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.geom.*;
import frigengine.field.*;

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
	private Point shakingOffset;

	// Constructors and initialization
	public CameraComponent() {
		this.width = 400;
		this.height = 300;
		this.centeredOn = null;
		this.shakingOffset = null;
	}
	private CameraComponent(CameraComponent other) {
		super(other);

		this.width = other.width;
		this.height = other.height;
		this.centeredOn = other.centeredOn;
		this.shakingOffset = null;
	}
	@Override
	public CameraComponent clone() {
		return new CameraComponent(this);
	}
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// width
		try {
			this.width = (float) xmlElement.getDoubleAttribute("width", this.width);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		
		// height
		try {
			this.height = (float) xmlElement.getDoubleAttribute("height", this.height);
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
		if(this.isShaking()) {
			this.shakingOffset.setX((float)Math.random() * 100.0f - 50.0f);
			this.shakingOffset.setY((float)Math.random() * 100.0f - 50.0f);
		}
		
		// Stay centered on an entity if necessary
		if(this.centeredOn != null && Entity.entityExists(this.centeredOn)) {
			if(getEntity(this.centeredOn).hasComponent(SpriteComponent.class)) { // If the entity has a sprite, center on it
				getComponent(PositionComponent.class).setPosition(
						getEntity(this.centeredOn).getComponent(SpriteComponent.class).getWorldDomain().getCenter()[0] + (this.isShaking() ? this.shakingOffset.getX() : 0),
						getEntity(this.centeredOn).getComponent(SpriteComponent.class).getWorldDomain().getCenter()[1] + (this.isShaking() ? this.shakingOffset.getY() : 0)
						);
				getComponent(PositionComponent.class).setPosition(getEntity(this.centeredOn).getComponent(SpriteComponent.class).getWorldDomain().getCenter());
			} else if(getEntity(this.centeredOn).hasComponent(PositionComponent.class)) { // Else center on its real position
				getComponent(PositionComponent.class).setPosition(
						getEntity(this.centeredOn).getComponent(PositionComponent.class).getPosition().getX() + (this.isShaking() ? this.shakingOffset.getX() : 0),
						getEntity(this.centeredOn).getComponent(PositionComponent.class).getPosition().getY() + (this.isShaking() ? this.shakingOffset.getY() : 0)
						);
			}
		}
		
		if(hasComponent(ColliderComponent.class)) {
			//getComponent(ColliderComponent.class).setWidth(this.width);
			//getComponent(ColliderComponent.class).setHeight(this.height);
		}
	}
	
	// Getters and setters
	public Point getCenter() {
		return getComponent(PositionComponent.class).getPosition();
	}
	public void setCenter(Point center) {
		getComponent(PositionComponent.class).setX(center.getX());
		getComponent(PositionComponent.class).setY(center.getY());
	}
	public void setCenter(float centerX, float centerY) {
		getComponent(PositionComponent.class).setX(centerX);
		getComponent(PositionComponent.class).setY(centerY);
	}
	private Rectangle viewport = new Rectangle(0,0,0,0);
	public Rectangle getViewport() {
		viewport.setX(getComponent(PositionComponent.class).getX() - (this.width / 2));
		viewport.setY(getComponent(PositionComponent.class).getY() - (this.height / 2));
		viewport.setWidth(this.width);
		viewport.setHeight(this.height);
		return viewport;
	}
	public boolean isShaking() {
		return this.shakingOffset != null;
	}
	public Transform getTransform() {
		return			Transform.createScaleTransform(FRIGGame.getScreenWidth() / this.width, FRIGGame.getScreenHeight() / this.height)	
		.concatenate(	Transform.createTranslateTransform(-1.0f * (this.getCenter().getX() - this.width / 2), -1.0f * (this.getCenter().getY() - this.height / 2)));
	}
	public Transform getTransform(int elevation) {
		float scale = (float)Math.pow(1.5, elevation);
		return			Transform.createScaleTransform(scale, scale)
		.concatenate(   Transform.createScaleTransform(FRIGGame.getScreenWidth() / this.width, FRIGGame.getScreenHeight() / this.height))
		.concatenate(	Transform.createTranslateTransform(-1.0f * (this.getCenter().getX() - this.width / (2 * scale)), -1.0f * (this.getCenter().getY() - this.height / (2 * scale))));
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
	public void zoomFitScene() {
		this.width = this.getContainingEntity().getScene().getDomain().getWidth();
		this.height = this.getContainingEntity().getScene().getDomain().getHeight();
	}
	public void startShaking() {
		this.shakingOffset = new Point(0,0);
	}
	public void stopShaking() {
		this.shakingOffset = null;
	}
	
	// Utilities
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + Arrays.toString(this.getCenter().getCenter()) + "; "
				+ this.width + "x" + this.height;
	}
}
