package frigengine;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.exceptions.AttributeFormatException;
import frigengine.exceptions.DataParseException;
import frigengine.exceptions.InvalidTagException;
import frigengine.util.IDable;

public class FRIGAnimation extends IDable implements Initializable {
	private static int animationCount = 0;

	@Override
	public String getTagName() {
		return "animation";
	}

	// Attributes
	private Animation animation;
	private Rectangle presence;

	// Constructors and initialization
	public FRIGAnimation() {
		animationCount++;
		this.id = "animation_" + Integer.toString(animationCount, 4);
	}

	public void init(XMLElement xmlElement) {
		if (!xmlElement.getName().equals(getTagName()))
			throw new InvalidTagException(getTagName(), xmlElement.getName());

		// ID
		this.id = xmlElement.getAttribute("id", this.getID());

		// Load image
		Image image;
		try {
			image = new Image(xmlElement.getAttribute("sprite", ""));
		} catch (SlickException e) {
			throw new DataParseException("Failed to load image '"
					+ xmlElement.getAttribute("sprite", "")
					+ "' for animation '" + this.getID() + "'");
		}

		// Animation slicing
		int cols;
		try {
			cols = xmlElement.getIntAttribute("cols", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "cols",
					xmlElement.getAttribute("cols"));
		}
		int rows;
		try {
			rows = xmlElement.getIntAttribute("rows", 1);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "cols",
					xmlElement.getAttribute("cols"));
		}
		SpriteSheet sprite = new SpriteSheet(image, image.getWidth() / cols,
				image.getHeight() / rows);

		// Animation details
		try {
			this.animation = new Animation(sprite,
					(int) (xmlElement.getDoubleAttribute("spf", 1) * 1000));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "spf",
					xmlElement.getAttribute("spf"));
		}
		this.animation.setAutoUpdate(false);
		try {
			this.animation.setLooping(xmlElement
					.getBooleanAttribute("looping", true));
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "looping",
					xmlElement.getAttribute("looping"));
		}

		// Presence
		float offset_x;
		try {
			offset_x = (float) xmlElement.getDoubleAttribute("offset_x", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "offset_x",
					xmlElement.getAttribute("offset_x"));
		}
		float offset_y;
		try {
			offset_y = (float) xmlElement.getDoubleAttribute("offset_y", 0);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "offset_y",
					xmlElement.getAttribute("offset_y"));
		}
		float width;
		try {
			width = (float) xmlElement.getDoubleAttribute("width",
					animation.getWidth());
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "width",
					xmlElement.getAttribute("width"));
		}
		float height;
		try {
			height = (float) xmlElement.getDoubleAttribute("height",
					animation.getHeight());
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(getTagName(), "height",
					xmlElement.getAttribute("height"));
		}
		presence = new Rectangle(offset_x, offset_y, width, height);
	}

	// Getters and setters
	public Rectangle getPresence() {
		return presence;
	}

	// Animation
	public void addFrame(Image frame, int duration) {
		this.animation.addFrame(frame, duration);
	}

	public void addFrame(int duration, int x, int y) {
		this.animation.addFrame(duration, x, y);
	}

	public Animation copy() {
		return this.animation.copy();
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

	public void drawFlash(float arg0, float arg1, float arg2, float arg3,
			Color arg4) {
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
	}

	@SuppressWarnings("deprecation")
	public void updateNoDraw() {
		this.animation.updateNoDraw();
	}
}