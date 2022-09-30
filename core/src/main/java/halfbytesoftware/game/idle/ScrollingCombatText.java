package halfbytesoftware.game.idle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class ScrollingCombatText extends Group {
    public ScrollingCombatText(float x, float y, float dx, float dy, float duration, Color color, String text, GameAssetManager.ETextSize textSize){
        // setup the label
        Label label = new Label(text, GameAssetManager.getInstance().getLabelStyle(textSize));
        label.setPosition(0, 0, Align.center);
        label.setColor(color);
        this.addActor(label);

        // setup this group
        this.setPosition(x, y);
        this.addAction(Actions.sequence(
                Actions.scaleTo(1.25f, 1.25f, duration * 0.25f),
                Actions.scaleTo(1.0f, 1.0f, duration * 0.25f),
                Actions.parallel(
                        Actions.moveBy(dx, dy, duration * 0.50f),
                        Actions.fadeOut(duration * 0.50f)
                ),
                Actions.removeActor()
        ));

        this.setTouchable(Touchable.disabled);
    }

    public ScrollingCombatText(float x, float y, Color color, GameAssetManager.ETextSize size, boolean success, boolean crit, int amount){
        this(
                x,
                y,
                0,
                100,
                1.0f,
                color,
                success ? "" + amount : "missed",
                crit ? size : size
        );
    }
}

