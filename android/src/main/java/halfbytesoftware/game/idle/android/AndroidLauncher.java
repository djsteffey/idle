package halfbytesoftware.game.idle.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.RatioResolutionStrategy;
import halfbytesoftware.game.idle.IPlatformServices;
import halfbytesoftware.game.idle.IdleGame;

public class AndroidLauncher extends AndroidApplication implements IPlatformServices {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// config
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.resolutionStrategy = new RatioResolutionStrategy(1280, 720);

		// init
		initialize(new IdleGame(this), config);
	}
}