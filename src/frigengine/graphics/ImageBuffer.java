package frigengine.graphics;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageBuffer {
	// Attributes
	private int width;
	private int height;
	private Image image;
	private ArrayList<BufferReleaseListener> bufferReleaseListeners;
	
	// Constructors and initialization
	ImageBuffer(int maxWidth, int maxHeight) {
		this.width = maxWidth;
		this.height = maxHeight;
		
		try {
			this.image = Image.createOffscreenImage(maxWidth, maxHeight, Image.FILTER_LINEAR);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	// Getters and setters
	public int getWidth() {
		return this.width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return this.height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Image getImage() {
		return this.image.getSubImage(0, 0, this.width, this.height);
	}
	public Graphics getGraphics() {
		try {
			return this.image.getGraphics();
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Release handling
	public void release() {
		for(BufferReleaseListener listener : this.bufferReleaseListeners)
			listener.bufferReleased(this);
	}
	public void addBufferReleaseListener(BufferReleaseListener listener) {
		if(bufferReleaseListeners == null)
			bufferReleaseListeners = new ArrayList<BufferReleaseListener>();
		
		bufferReleaseListeners.add(listener);
	}
}
