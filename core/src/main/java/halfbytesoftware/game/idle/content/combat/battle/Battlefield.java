package halfbytesoftware.game.idle.content.combat.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

import halfbytesoftware.game.idle.GameAssetManager;
import halfbytesoftware.game.idle.GameState;
import halfbytesoftware.game.idle.Utils;
import halfbytesoftware.game.idle.content.combat.battle.character.BattlefieldCharacter;
import halfbytesoftware.game.idle.content.combat.battle.character.BattlefieldCharacterRanged;

public class Battlefield extends Group {

    // variables
    private GameState.CombatAreaInfo m_areaInfo;
    private Image m_background;
    private List<BattlefieldCharacter> m_heroList;
    private List<BattlefieldCharacter> m_mobList;
    private Group m_actorGroup;

    // methods
    public Battlefield(GameState.CombatAreaInfo areaInfo) {
        // parameters
        this.m_areaInfo = areaInfo;

        // background
        this.m_background = new Image(GameAssetManager.getInstance().getTexture("oga/Fupi/grass.png"));
        this.m_background.setColor(Color.GRAY);
        this.addActor(this.m_background);

        // table for supporting clipping
        Table table = new Table();
        table.pad(0);
        table.defaults().space(0);
        table.setFillParent(true);
        table.setClip(true);
        this.addActor(table);

        // group for the actors
        this.m_actorGroup = new Group();
        table.add(this.m_actorGroup).expand().fill();

        // heroes
        this.m_heroList = new ArrayList<>();

        // mobs
        this.m_mobList = new ArrayList<>();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // hero ai
        for (BattlefieldCharacter bc : this.m_heroList) {
            bc.ai(this, this.m_heroList, this.m_mobList, delta);
        }

        // mob ai
        for (BattlefieldCharacter bc : this.m_mobList) {
            bc.ai(this, this.m_mobList, this.m_heroList, delta);
        }

        // loop heroes
        for (BattlefieldCharacter bc : this.m_heroList) {
            // check if dead
            if (bc.getCharacterState().getIsAlive() == false) {
                // turn into a tombstone
                bc.tombstone();
            }
        }
//                this.m_heroList.removeIf(bc->(bc.getCharacterState().getIsAlive() == false));

        // remove dead mobs
        for (BattlefieldCharacter bc : this.m_mobList) {
            if (bc.getCharacterState().getIsAlive() == false) {
                bc.remove();
                this.m_areaInfo.modifyNumMobsKilledThisStage(1);
            }
        }
        this.m_mobList.removeIf(bc -> (bc.getCharacterState().getIsAlive() == false));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.m_actorGroup.getChildren().sort((Actor a, Actor b) -> {
            if (a instanceof BattlefieldCharacter && b instanceof BattlefieldCharacter) {
                return (int) ((b.getY() * 10000) - (a.getY() * 10000));
            }
            return 1;
        });
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (this.m_background != null) {
            this.m_background.setSize(this.getWidth(), this.getHeight());
        }
    }

    public void addHero(GameState.CharacterState characterState) {
        // ready the character for battle
        characterState.resetForBattle();

        // create a new battlefield character from the state
        BattlefieldCharacter bc = Utils.randomBool() ? new BattlefieldCharacterRanged(characterState) : new BattlefieldCharacter(characterState);

        // randomize its position somewhere around center of battlefield
        float dir = Utils.randomFloat(0, (float) (2 * Math.PI));
        bc.setPosition(
                (float) (1064 * 0.50f + Math.sin(dir) * 100.0f),
                (float) (528 * 0.50f + Math.cos(dir) * 100.0f)
        );

        // add to list of heroes
        this.m_heroList.add(bc);

        // add to the group for rendering and acting
        this.m_actorGroup.addActor(bc);
    }

    public void addMob(GameState.CharacterState characterState) {
        // ready the character for battle
        characterState.resetForBattle();

        // create new battlefield character from the character state
        BattlefieldCharacter bc = new BattlefieldCharacter(characterState);

        // randomize its position in circle around center of battlefield, but off screen
        float dir = Utils.randomFloat(0, (float) (2 * Math.PI));
        bc.setPosition(
                (float) (this.getWidth() * 0.50f + Math.sin(dir) * this.getWidth() * 0.65f),
                (float) (this.getHeight() * 0.50f + Math.cos(dir) * this.getWidth() * 0.65f)
        );
        this.m_mobList.add(bc);
        this.m_actorGroup.addActor(bc);
    }

    public int getNumMobs() {
        return this.m_mobList.size();
    }
}
