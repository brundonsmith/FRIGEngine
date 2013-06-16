package frigengine.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import frigengine.events.AnimationFinishedListener;
import frigengine.util.geom.Rectangle;
import frigengine.util.graphics.Animation;

public class Drawable extends Category implements AnimationFinishedListener {
	// Constants
	@Override
	public  Collection<Class<? extends Component>> getRequiredComponents() {
		return new ArrayList<Class<? extends Component>>(Arrays.asList(
				PositionData.class,
				GraphicsData.class
			));
	}
	
	// Attributes
	private List<AnimationFinishedListener> animationFinishedListeners;
	// TODO Events
	// Access
	protected PositionData posData() {
		return (PositionData)this.components.get(PositionData.class);
	}
	protected GraphicsData grphcsData() {
		return (GraphicsData)this.components.get(GraphicsData.class);
	}

	// Main loop methods
	public void update(int delta) {
		if (this.grphcsData().hasActiveAnimation() && this.grphcsData().getActiveAnimation().isStopped()) {
			this.grphcsData().clearActiveAnimation();
		}
		this.getCurrentAnimation().update(delta);
	}

	// Getters and setters
	public float getMinX() {
		if (this.grphcsData().getPresence() == null) {
			return this.posData().getX()
					+ this.getCurrentAnimation().getPresence().getX()
					- this.getCurrentAnimation().getPresence().getWidth() / 2;
		} else {
			return this.posData().getX()
					+ this.grphcsData().getPresence().getX() - this.grphcsData().getPresence().getWidth() / 2;
		}
	}
	public float getMinY() {
		if (this.grphcsData().getPresence() == null) {
			return this.posData().getY()
					+ this.getCurrentAnimation().getPresence().getY()
					- this.getCurrentAnimation().getPresence().getHeight() / 2;
		} else {
			return this.posData().getY()
					+ this.grphcsData().getPresence().getY() - this.grphcsData().getPresence().getHeight() / 2;
		}
	}
	public float getMaxX() {
		return this.getMinX() + this.getWidth();
	}
	public float getMaxY() {
		return this.getMinY() + this.getHeight();
	}
	public float getWidth() {
		if (this.grphcsData().getPresence() == null) {
			return this.getCurrentAnimation().getPresence().getWidth();
		} else {
			return this.grphcsData().getPresence().getWidth();
		}
	}
	public float getHeight() {
		if (this.grphcsData().getPresence() == null) {
			return this.getCurrentAnimation().getPresence().getHeight();
		} else {
			return this.grphcsData().getPresence().getHeight();
		}
	}
	public Rectangle getWorldPresence() {
		return new Rectangle(this.getMinX(), this.getMinY(), this.getWidth(), this.getHeight());
	}
	public Animation getCurrentAnimation() {
		return this.grphcsData().getCurrentAnimation();
	}
	public void playAnimation(String animation) {
		this.grphcsData().setActiveAnimation(animation);
	}
	public Animation getAnimation() {
		return this.grphcsData().getContinuousAnimation();
	}
	public void setAnimation(String animation) {
		this.grphcsData().setContinuousAnimation(animation);
	}
	
	// Events
	public void addFinishedListener(AnimationFinishedListener listener) {
		this.animationFinishedListeners.add(listener);
	}
	@Override
	public void animationFinished(Animation source) {
		for(AnimationFinishedListener listener : this.animationFinishedListeners) {
			listener.animationFinished(source);
		}
	}
}