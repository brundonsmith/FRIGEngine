package frigengine;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

public class FRIGMain {
	public static void main(String[] args) throws SlickException {
		int width = 1600;
		int height = 900;
		ScalableGame game = new ScalableGame(FRIGGame.getInstance(), width, height);
		AppGameContainer gameContainer = new AppGameContainer(game);
		gameContainer.setDisplayMode(width, height, true);
		gameContainer.setTargetFrameRate(240);
		
		gameContainer.start();
	}
}
