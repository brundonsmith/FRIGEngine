package frigengine.core.scene;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.geom.*;
import frigengine.core.idable.*;
import frigengine.core.util.Initializable;


public class Animation extends IDable<String> implements Initializable {
	// Constants
	public static final String PLACEHOLDER_ID = "PLACEHOLDER";

	// Attributes
	private static int animationCount = 0;
	
	private int defaultDuration;
	private org.newdawn.slick.Animation animation;
	private Rectangle presence;
	private List<AnimationFinishedListener> finishedListeners;

	// Constructors and initialization
	public Animation() {
		this.setId("animation" + Integer.toString(Animation.animationCount++, 4));
		this.animation = null;
		this.defaultDuration = 1000;
		this.presence = new Rectangle(0, 0, 0, 0);
		this.finishedListeners = new ArrayList<AnimationFinishedListener>();
	}
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// id
		this.setId(xmlElement.getAttribute("id", this.getId()));

		// defaultDuration
		try {
			this.defaultDuration = xmlElement.getIntAttribute("duration", this.defaultDuration);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "duration",
					xmlElement.getAttribute("duration"));
		}
		
		// animation
		Image image;
		try {
			image = new Image(xmlElement.getAttribute("sprite", ""));
		} catch (SlickException e) {
			throw new MissingFileException(xmlElement.getAttribute("sprite", ""), this.getClass(), this.getId());
		}
		int cols;
		try {
			cols = xmlElement.getIntAttribute("cols", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "cols",
					xmlElement.getAttribute("cols"));
		}
		int rows;
		try {
			rows = xmlElement.getIntAttribute("rows", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "cols",
					xmlElement.getAttribute("cols"));
		}
		SpriteSheet sprite = new SpriteSheet(image, image.getWidth() / cols, image.getHeight()
				/ rows);
		this.animation = new org.newdawn.slick.Animation(sprite, this.defaultDuration / (rows * cols));
		this.animation.setAutoUpdate(false);
		try {
			this.animation.setLooping(xmlElement.getBooleanAttribute("looping", true));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "looping",
					xmlElement.getAttribute("looping"));
		}

		// presence
		try {
			this.presence.setX((float) xmlElement.getDoubleAttribute("offset_x", this.presence.getX()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "offset_x",
					xmlElement.getAttribute("offset_x"));
		}
		try {
			this.presence.setY((float) xmlElement.getDoubleAttribute("offset_y", this.presence.getY()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "offset_y",
					xmlElement.getAttribute("offset_y"));
		}
		try {
			this.presence.setWidth((float) xmlElement.getDoubleAttribute("width", this.animation.getWidth()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		try {
			this.presence.setHeight((float) xmlElement.getDoubleAttribute("height", this.animation.getHeight()));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
		
		// finishedListeners
	}

	// Getters and setters
	public Rectangle getPresence() {
		return presence;
	}

	// Exposed Animation methods
	public void addFrame(Image frame, int duration) {
		this.animation.addFrame(frame, duration);
	}
	public void addFrame(int duration, int x, int y) {
		this.animation.addFrame(duration, x, y);
	}
	public Animation copy() {
		Animation copy = new Animation();
		copy.animation = this.animation.copy();
		copy.presence = this.presence;
		return copy;
	}
	public void draw() {
		this.animation.draw();
	}
	public void draw(float x, float y, Color filter) {
		this.animation.draw(x, y, filter);
	}
	public void draw(float arg0, float arg1, float arg2, float arg3, Color arg4) {
		this.animation.draw(arg0, arg1, arg2, arg3, arg4);
	}
	public void draw(float x, float y, float width, float height) {
		this.animation.draw(x, y, width, height);
	}
	public void draw(float x, float y) {
		this.animation.draw(x, y);
	}
	public void drawFlash(float arg0, float arg1, float arg2, float arg3, Color arg4) {
		this.animation.drawFlash(arg0, arg1, arg2, arg3, arg4);
	}
	public void drawFlash(float x, float y, float width, float height) {
		this.animation.drawFlash(x, y, width, height);
	}
	public Image getCurrentFrame() {
		return this.animation.getCurrentFrame();
	}
	public int getDuration(int index) {
		return this.animation.getDuration(index);
	}
	public int[] getDurations() {
		return this.animation.getDurations();
	}
	public int getFrame() {
		return this.animation.getFrame();
	}
	public int getFrameCount() {
		return this.animation.getFrameCount();
	}
	public int getHeight() {
		return this.animation.getHeight();
	}
	public Image getImage(int index) {
		return this.animation.getImage(index);
	}
	public long getPauseDuration() {
		return this.animation.getPauseDuration();
	}
	public float getSpeed() {
		return this.animation.getSpeed();
	}
	public int getWidth() {
		return this.animation.getWidth();
	}
	public boolean isPause() {
		return this.animation.isPause();
	}
	public boolean isStopped() {
		return this.animation.isStopped();
	}
	public void renderInUse(int arg0, int arg1, float arg2) {
		this.animation.renderInUse(arg0, arg1, arg2);
	}
	public void renderInUse(int arg0, int arg1) {
		this.animation.renderInUse(arg0, arg1);
	}
	public void restart() {
		this.animation.restart();
	}
	public void setAutoUpdate(boolean auto) {
		this.animation.setAutoUpdate(auto);
	}
	public void setCurrentFrame(int index) {
		this.animation.setCurrentFrame(index);
	}
	public void setDuration(int index, int duration) {
		this.animation.setDuration(index, duration);
	}
	public void setLooping(boolean loop) {
		this.animation.setLooping(loop);
	}
	public void setPauseDuration(long pauseDuration) {
		this.animation.setPauseDuration(pauseDuration);
	}
	public void setPingPong(boolean pingPong) {
		this.animation.setPingPong(pingPong);
	}
	public void setSpeed(float spd) {
		this.animation.setSpeed(spd);
	}
	public void start() {
		this.animation.start();
	}
	public void stop() {
		this.animation.stop();
	}
	public void stopAt(int frameIndex) {
		this.animation.stopAt(frameIndex);
	}
	public String toString() {
		return this.animation.toString();
	}
	public void update(long delta) {
		this.animation.update(delta);
		if(this.isStopped()) {
			this.notifyFinishedListeners();
		}
	}
	@SuppressWarnings("deprecation")
	public void updateNoDraw() {
		this.animation.updateNoDraw();
	}

	// Utilities
	public static Animation getPlaceholder() {
		Animation placeholder = new Animation();
		placeholder.setId(Animation.PLACEHOLDER_ID);
		ImageBuffer buf = new ImageBuffer(100,100);
		for(int x = 0; x < buf.getWidth(); x++) {
			for(int y = 0; y < buf.getHeight(); y++) {
				buf.setRGBA(x, y, 255, 0, 255, 255);
			}
		}
		Image image = new Image(buf);
		
		placeholder.animation = new org.newdawn.slick.Animation(new Image[] { image }, 1);
		placeholder.presence = new Rectangle(0, 0, image.getWidth(), image.getHeight());
		return placeholder;
	}

	// Events
	public void addFinishedListener(AnimationFinishedListener listener) {
		this.finishedListeners.add(listener);
	}
	private void notifyFinishedListeners() {
		for(int i = 0; i < this.animation.getFrameCount(); i++) {
			this.animation.setDuration(i, this.defaultDuration / this.animation.getFrameCount());
		}
		
		for(AnimationFinishedListener listener : finishedListeners) {
			listener.animationFinished(this);
		}
	}
}