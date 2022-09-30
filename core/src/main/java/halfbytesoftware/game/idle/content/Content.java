package halfbytesoftware.game.idle.content;

import com.badlogic.gdx.scenes.scene2d.Group;
import halfbytesoftware.game.idle.GameState;

public abstract class Content extends Group {
    // variables
    protected GameState m_gameState;

    // methods
    public Content(String name, GameState gameState){
        // parameters
        this.setName(name);
        this.m_gameState = gameState;
    }
}
