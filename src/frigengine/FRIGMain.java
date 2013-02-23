package frigengine;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

public class FRIGMain {
	public static void main(String[] args) throws SlickException {
		AppGameContainer gameContainer = new AppGameContainer(new ScalableGame(new FRIGGame(), 800,
				600));

		gameContainer.start();
	}
}
