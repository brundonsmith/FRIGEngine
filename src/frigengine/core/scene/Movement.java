package frigengine.core.scene;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.exceptions.data.*;
import frigengine.core.util.*;

public class Movement implements Initializable {
	// Attributes
	private int duration;
	private Curve path;
	private String animationId;
	
	// Constructors and initialization
	public Movement() {
		this.duration = 0;
		this.path = getStationaryPath(new Vector2f(0,0));
		this.animationId = Animation.PLACEHOLDER_ID;
	}
	public Movement(int duration, Curve path, String animationId) {
		this.duration = duration;
		this.path = path;
		this.animationId = animationId;
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// duration
		try {
			this.duration = xmlElement.getIntAttribute("duration", this.duration);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "duration", xmlElement.getAttribute("duration"));
		}
		
		// path
		XMLElement child = xmlElement.getChildrenByName(Curve.class.getSimpleName()).get(0);
		if(child.getAttribute("type") != null) {
			List<Vector2f> points = new ArrayList<Vector2f>();
			for(int i = 0; i < child.getChildrenByName("Point").size(); i++) {
				XMLElement pointElement = child.getChildrenByName("Point").get(i);
				float x = 0;
				try {
					x = (float)pointElement.getDoubleAttribute("x", x);
				} catch (SlickXMLException e) {
					throw new AttributeFormatException(pointElement.getName(), "x", pointElement.getAttribute("x"));
				}
				float y = 0;
				try {
					y = (float)pointElement.getDoubleAttribute("y", y);
				} catch (SlickXMLException e) {
					throw new AttributeFormatException(pointElement.getName(), "y", pointElement.getAttribute("y"));
				}
				
				points.add(new Vector2f(x, y));
			}
			
			switch(child.getAttribute("type")) {
			case "stationary":
				try {
					this.path = Movement.getStationaryPath(points.get(0));
				} catch(IndexOutOfBoundsException e) {
					throw new MissingElementException("Point", child.getName(), 1);
				}
				break;
			case "linear":
				try {
					this.path = Movement.getLinearPath(points.get(0), points.get(1));
				} catch(IndexOutOfBoundsException e) {
					throw new MissingElementException("Point", child.getName(), 2);
				}
				break;
			case "bezier":
				try {
					this.path = Movement.getBezierPath(points.get(0), points.get(1), points.get(2), points.get(3));
				} catch(IndexOutOfBoundsException e) {
					throw new MissingElementException("Point", child.getName(), 4);
				}
				break;
			}
		}
		
		// animationId
		this.animationId = xmlElement.getAttribute("animation", this.animationId);
	}
	
	// Getters and setters
	public int getDuration() {
		return this.duration;
	}
	public Point getPosition(int progress) {
		float percentage = Math.min(Math.abs((float)progress / (float)this.duration), 1);
		Vector2f pos = this.path.pointAt(percentage);
		return new Point(pos.getX(), pos.getY());
	}
	public String getAnimationId() {
		return this.animationId;
	}
	
	// Utilities
	public static Curve getStationaryPath(Vector2f location) {
		return new Curve(location, location, location, location);
	}
	public static Curve getLinearPath(Vector2f source, Vector2f destination) {
		return new Curve(source, new Vector2f(source.getX() + ((destination.getX() - source.getX()) / 3), source.getY() + ((destination.getY() - source.getY()) / 3)), new Vector2f(source.getX() + ((destination.getX() - source.getX()) * 2 / 3), source.getY() + ((destination.getY() - source.getY()) / 3)), destination);
	}
	public static Curve getBezierPath(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3) {
		return new Curve(p0, p1, p2, p3);
	}
}
