package frigengine.util.graphics;

import java.util.LinkedList;
import java.util.Queue;

public class BufferPool implements BufferReleaseListener {
	// Constants
	public static final int DEFAULT_POOL_SIZE = 10;
	
	// Attributes
	private int maxWidth;
	private int maxHeight;
	private Queue<ImageBuffer> activeBuffers;
	private Queue<ImageBuffer> idleBuffers;
	
	// Constructors and initialization
	public BufferPool(int maxWidth, int maxHeight) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		activeBuffers = new LinkedList<ImageBuffer>();
		idleBuffers = new LinkedList<ImageBuffer>();
		
		for(int i = 0; i < DEFAULT_POOL_SIZE; i++)
			this.addBuffer();
	}
	public BufferPool(int maxWidth, int maxHeight, int poolSize) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		activeBuffers = new LinkedList<ImageBuffer>();
		idleBuffers = new LinkedList<ImageBuffer>();
		
		for(int i = 0; i < poolSize; i++)
			this.addBuffer();
	}

	// Getters and setters
	public ImageBuffer getBuffer(int width, int height) {
		if(width > maxWidth) {
			throw new IllegalArgumentException("Requested buffer width exceeds maximum width for this pool");
		} else if(height > maxHeight) {
			throw new IllegalArgumentException("Requested buffer height exceeds maximum height for this pool");
		} else {
			if(idleBuffers.size() == 0) {
				this.addBuffer();
			}
			
			ImageBuffer result = idleBuffers.remove();
			result.setWidth(width);
			result.setHeight(height);
			
			activeBuffers.add(result);
			return result;
		}
	}
	private void addBuffer() {
		ImageBuffer buffer = new ImageBuffer(maxWidth, maxHeight);
		buffer.addBufferReleaseListener(this);
		idleBuffers.add(buffer);
		System.out.println("Buffer created: " + (idleBuffers.size() + activeBuffers.size()));
	}
	
	// Events
	@Override
	public void bufferReleased(ImageBuffer source) {
		if(activeBuffers.contains(source)) {
			idleBuffers.add(source);
			activeBuffers.remove(source);
			source.getGraphics().clear();
		}
	}
	
	// Utilities
	@Override
	public String toString() {
		return "{\n + " +
				"Active buffers: " + this.activeBuffers.size() + "\n" +
				"Idle buffers: " + this.idleBuffers.size() + "\n" +
				"}";
	}
}
