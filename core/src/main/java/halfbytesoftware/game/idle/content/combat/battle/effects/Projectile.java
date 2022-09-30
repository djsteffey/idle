package halfbytesoftware.game.idle.content.combat.battle.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import halfbytesoftware.game.idle.GameAssetManager;
import halfbytesoftware.game.idle.ScrollingCombatText;
import halfbytesoftware.game.idle.Utils;
import halfbytesoftware.game.idle.content.combat.battle.character.BattlefieldCharacter;

public class Projectile extends Group {
    // variables
    private BattlefieldCharacter m_target;

    // methods
    public Projectile(BattlefieldCharacter target, float posX, float posY) {
        // parameters
        this.m_target = target;

        // size
        this.setSize(16, 16);

        // image
        Image image = new Image(GameAssetManager.getInstance().getTexture("projectile.png"));
        image.setSize(this.getWidth(), this.getHeight());
        this.addActor(image);

        // position
        this.setPosition(posX, posY, Align.center);
    }

    @Override
    public void act(float delta) {
        // distance to target
        float distance = Utils.getDistance(this, this.m_target);
        if (distance <= 4.0f) {
            // hit
            if (this.m_target.getCharacterState().getIsAlive()) {
                // still alive; compute damage
                int amount = 1;

                // apply damage
                this.m_target.getCharacterState().damage(amount);

                // sct
                this.m_target.getParent().addActor(new ScrollingCombatText(
                        this.m_target.getX(Align.center),
                        this.m_target.getY(Align.center),
                        Color.RED,
                        GameAssetManager.ETextSize.STANDARD,
                        true,
                        false,
                        amount
                ));
            }
            // either way remove projectile from stage
            this.remove();
        } else {
            // keep going
            Vector2 direction = Utils.getDirection(this, this.m_target, true);
            this.moveBy(direction.x * 128.0f * delta, direction.y * 128.0f * delta);
        }
        super.act(delta);
    }
}
