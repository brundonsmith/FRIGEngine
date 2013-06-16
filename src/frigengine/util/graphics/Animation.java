package frigengine.util.graphics;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.events.AnimationFinishedListener;
import frigengine.exceptions.data.AttributeFormatException;
import frigengine.exceptions.data.InvalidTagException;
import frigengine.exceptions.data.MissingFileException;
import frigengine.util.IDable;
import frigengine.util.Initializable;
import frigengine.util.geom.Rectangle;

public class Animation extends IDable<String> implements Initializable {
	// Constants
	public static final String PLACEHOLDER_ID = "PLACEHOLDER";

	// Attributes
	private static int animationCount = 0;
	private org.newdawn.slick.Animation animation;
	private Rectangle presence;
	private List<AnimationFinishedListener> finishedListeners;

	// Constructors and initialization
	public Animation() {
		Animation.animationCount++;
		this.id = "animation_" + Integer.toString(animationCount, 4);
	}
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}
		
		// id
		this.id = xmlElement.getAttribute("id", this.getId());

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
		try {
			this.animation = new org.newdawn.slick.Animation(sprite,
					(int) (xmlElement.getDoubleAttribute("spf", 1) * 1000));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "spf", xmlElement.getAttribute("spf"));
		}
		this.animation.setAutoUpdate(false);
		try {
			this.animation.setLooping(xmlElement.getBooleanAttribute("looping", true));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "looping",
					xmlElement.getAttribute("looping"));
		}

		// presence
		float offset_x;
		try {
			offset_x = (float) xmlElement.getDoubleAttribute("offset_x", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "offset_x",
					xmlElement.getAttribute("offset_x"));
		}
		float offset_y;
		try {
			offset_y = (float) xmlElement.getDoubleAttribute("offset_y", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "offset_y",
					xmlElement.getAttribute("offset_y"));
		}
		float width;
		try {
			width = (float) xmlElement.getDoubleAttribute("width", animation.getWidth());
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		float height;
		try {
			height = (float) xmlElement.getDoubleAttribute("height", animation.getHeight());
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}
		presence = new Rectangle(0, 0, 0, 0);
		presence.setX(offset_x);
		presence.setY(offset_y);
		presence.setWidth(width);
		presence.setHeight(height);
		
		// finishedListeners
		this.finishedListeners = new ArrayList<AnimationFinishedListener>();
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
		placeholder.id = Animation.PLACEHOLDER_ID;
		Image image = null;
		try {
			image = new Image(10, 10);
		} catch (SlickException e) {
		}
		byte[] textureData = image.getTexture().getTextureData();
		for (int i = 0; i < textureData.length; i++) {
			textureData[i] = (byte) 255;
		}
		placeholder.animation = new org.newdawn.slick.Animation(new Image[] { image }, 1);
		placeholder.presence = new Rectangle(0, 0, image.getWidth(), image.getHeight());
		return placeholder;
	}

	// Events
	public void addFinishedListener(AnimationFinishedListener listener) {
		this.finishedListeners.add(listener);
	}
	private void notifyFinishedListeners() {
		for(AnimationFinishedListener listener : finishedListeners)
			listener.animationFinished(this);
	}
}