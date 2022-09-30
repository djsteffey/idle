package halfbytesoftware.game.idle.content.combat.battle.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import halfbytesoftware.game.idle.Config;
import halfbytesoftware.game.idle.GameAssetManager;
import halfbytesoftware.game.idle.GameState;
import halfbytesoftware.game.idle.ScrollingCombatText;
import halfbytesoftware.game.idle.Utils;
import halfbytesoftware.game.idle.content.combat.battle.Battlefield;

public class BattlefieldCharacter extends Group {
    // variables
    protected GameState.CharacterState m_characterState;
    private Image m_image;

    // methods
    public BattlefieldCharacter(GameState.CharacterState characterState) {
        // parameters
        this.m_characterState = characterState;

        this.setSize(Config.BATTLE_CHARACTER_SIZE, Config.BATTLE_CHARACTER_SIZE);

        this.m_image = new Image(GameAssetManager.getInstance().getTileset("actors_24x24.png", 24).getRegion(this.m_characterState.getGraphicId()));
        this.m_image.setSize(this.getWidth(), this.getHeight());
        this.addActor(this.m_image);
    }

    public GameState.CharacterState getCharacterState() {
        return this.m_characterState;
    }

    public void tombstone() {
        this.m_image.setDrawable(new TextureRegionDrawable(GameAssetManager.getInstance().getTileset("actors_24x24.png", 24).getRegion(Config.GRAPHICS_TOMBSTONE_GRAPHIC_ID)));
    }

    public void ai(Battlefield battlefield, List<BattlefieldCharacter> friendlyList, List<BattlefieldCharacter> enemyList, float delta) {
        // check if we are dead
        if (this.m_characterState.getIsAlive() == false) {
            // don't do anything and ensure actions are clear
            this.clearActions();
            return;
        }

        // reduce action cooldown
        this.getCharacterState().updateActionCooldown(delta);

        // check if ready to do an action
        if (this.m_characterState.getActionCooldown() == 0.0f) {
            // find the closest enemy
            BattlefieldCharacter enemyTarget = this.aiGetClosestEnemy(battlefield, this, enemyList);

            // ensure we got one
            if (enemyTarget != null) {
                // get the distance
                float distance = Utils.getDistance(this, enemyTarget);

                // check if close enough
                if (distance < Config.BATTLE_CHARACTER_SIZE * 0.75f) {
                    // compute amount to damage
                    int amount = Utils.randomInt(1, 3);

                    // damage the enemy
                    enemyTarget.getCharacterState().damage(1);

                    // sct
                    enemyTarget.getParent().addActor(new ScrollingCombatText(
                            enemyTarget.getX(Align.center),
                            enemyTarget.getY(Align.center),
                            Color.RED,
                            GameAssetManager.ETextSize.STANDARD,
                            true,
                            false,
                            amount
                    ));

                    // reset action cooldown
                    this.getCharacterState().setActionCooldown(Config.BATTLE_CHARACTER_ACTION_COOLDOWN);
                } else {
                    // move towards target
                    Vector2 dir = Utils.getDirection(this, enemyTarget, true);
                    this.moveBy(dir.x * Config.BATTLE_CHARACTER_MOVE_SPEED_BASE * delta, dir.y * Config.BATTLE_CHARACTER_MOVE_SPEED_BASE * delta);
                }
            }
        }
    }

    protected BattlefieldCharacter aiGetClosestEnemy(Battlefield battlefield, BattlefieldCharacter bc, List<BattlefieldCharacter> enemyList) {
        // make battlefield collision rect
        Rectangle rectangle = new Rectangle(0, 0, battlefield.getWidth(), battlefield.getHeight());

        // keep track
        BattlefieldCharacter closest = null;
        float closestDistance = 999999.0f;

        // loop enemylist
        for (BattlefieldCharacter enemy : enemyList) {
            // check if enemy is alive
            if (enemy.getCharacterState().getIsAlive() == false) {
                // skip
                continue;
            }

            // check if enemy is on the battlefield
            if (Utils.actorInRectangleCollision(enemy, rectangle) == false) {
                // skip
                continue;
            }

            // get distance to this enemy
            float distance = Utils.getDistance(bc, enemy);

            // check if closest
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = enemy;
            }
        }

        // done
        return closest;
    }
}
