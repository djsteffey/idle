package halfbytesoftware.game.idle.content.combat.battle.character;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import java.util.List;
import halfbytesoftware.game.idle.Config;
import halfbytesoftware.game.idle.GameState;
import halfbytesoftware.game.idle.Utils;
import halfbytesoftware.game.idle.content.combat.battle.Battlefield;
import halfbytesoftware.game.idle.content.combat.battle.effects.Projectile;

public class BattlefieldCharacterRanged extends BattlefieldCharacter {
    // variables


    // methods
    public BattlefieldCharacterRanged(GameState.CharacterState characterState) {
        super(characterState);
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

        // find the closest enemy
        BattlefieldCharacter enemyTarget = this.aiGetClosestEnemy(battlefield, this, enemyList);

        // make sure we got one
        if (enemyTarget != null) {
            // get the range to target
            float distance = Utils.getDistance(this, enemyTarget);

            // in range?
            if (distance < Config.BATTLE_CHARACTER_SIZE * 4.0f) {
                // check if ready to do an action
                if (this.m_characterState.getActionCooldown() == 0.0f) {
                    // make a projectile
                    Projectile projectile = new Projectile(enemyTarget, this.getX(Align.center), this.getY(Align.center));

                    // add it
                    this.getParent().addActor(projectile);

                    // reset action cooldown
                    this.getCharacterState().setActionCooldown(Config.BATTLE_CHARACTER_ACTION_COOLDOWN);
                }
            } else {
                // not in range; move towards target
                Vector2 dir = Utils.getDirection(this, enemyTarget, true);
                this.moveBy(dir.x * Config.BATTLE_CHARACTER_MOVE_SPEED_BASE * delta, dir.y * Config.BATTLE_CHARACTER_MOVE_SPEED_BASE * delta);
            }
        } else {
            // no enemy target...move towards center
            Vector2 dir = Utils.getDirection(this.getX(Align.center), this.getY(Align.center), this.getParent().getWidth() * 0.50f, this.getParent().getHeight() * 0.50f, true);
            this.moveBy(dir.x * Config.BATTLE_CHARACTER_MOVE_SPEED_BASE * delta, dir.y * Config.BATTLE_CHARACTER_MOVE_SPEED_BASE * delta);
        }
    }
}
