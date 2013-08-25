package frigengine.core.gui;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;

import frigengine.core.geom.Rectangle;
import frigengine.core.scene.*;

public interface GUIContext {
	public void drawShape(Graphics g, Shape shape);
	public void fillShape(Graphics g, Shape shape);
	public void renderObjectForeground(Graphics g, Image image, Rectangle domain);
	public void renderObjectForeground(Graphics g, Animation animation, Rectangle domain);
	public void renderStringBoxForeground(Graphics g, String text, Rectangle box, Font font);
}
