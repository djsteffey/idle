package halfbytesoftware.game.idle.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import halfbytesoftware.game.idle.IPlatformServices;
import halfbytesoftware.game.idle.IdleGame;

public class Lwjgl3Launcher implements IPlatformServices {
	public static void main(String[] args) {
		// config
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("IdleInfluence");
		configuration.setWindowedMode(1280, 720);
		configuration.setResizable(false);
		configuration.setWindowPosition(300, 200);
		configuration.useVsync(true);
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

		// create game
		IdleGame game = new IdleGame(new Lwjgl3Launcher());

		// start
		new Lwjgl3Application(game, configuration);
	}
}