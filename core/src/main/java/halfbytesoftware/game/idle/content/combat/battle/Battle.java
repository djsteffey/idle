package halfbytesoftware.game.idle.content.combat.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.util.ArrayList;
import java.util.List;
import halfbytesoftware.game.idle.Config;
import halfbytesoftware.game.idle.GameAssetManager;
import halfbytesoftware.game.idle.GameState;

public class Battle extends Group {
    private enum EState{
        INIT, PLAYING, STAGE_COMPLETE
    }

    public interface IListener{
        void onBattleQuit(Battle battle);
    }

    // variables
    private EState m_state;
    private GameState.CombatAreaInfo m_areaInfo;
    private Table m_table;
    private Header m_header;
    private Battlefield m_battlefield;
    private List<CharacterStateDisplay> m_CharacterStateDisplayList;

    // methods
    public Battle(GameState gameState, GameState.CombatAreaInfo areaInfo, IListener listener){
        // state
        this.m_state = EState.INIT;

        // parameter
        this.m_areaInfo = areaInfo;

        // table for everything
        this.m_table = new Table();
        this.m_table.setFillParent(true);
        this.m_table.pad(0);
        this.m_table.defaults().space(0);
        this.m_table.setClip(true);
        this.addActor(this.m_table);

        // header
        this.m_header = new Header(areaInfo, new Header.IListener() {
            @Override
            public void onHeaderQuitClick(Header header) {
                // notify the battle listener who will shut down the battle and show the area selector
                listener.onBattleQuit(Battle.this);
            }
        });
        this.m_table.add(this.m_header).expandX().height(40).fill().colspan(2);

        // hero ui status
        this.m_table.row();
        Table heroStatusTable = new Table();
        heroStatusTable.pad(0);
        heroStatusTable.defaults().space(0);
        this.m_table.add(heroStatusTable).width(128).expandY().fill();

        // character state ui
        this.m_CharacterStateDisplayList = new ArrayList<>();
        for (int i = 0; i < Config.BATTLE_MAX_HEROES; ++i){
            heroStatusTable.row();
            CharacterStateDisplay ui = new CharacterStateDisplay();
            this.m_CharacterStateDisplayList.add(ui);
            heroStatusTable.add(ui).expandX().expandY().fill();
        }

        // battlefield
        this.m_battlefield = new Battlefield(this.m_areaInfo);
        this.m_table.add(this.m_battlefield).expandX().expandY().fill();

        // add the heroes
        for (int i = 0; i < gameState.getCharacterStateList().size(); ++i){
            // handle to the character state
            GameState.CharacterState cs = gameState.getCharacterStateList().get(i);

            // add to the ui
            this.m_CharacterStateDisplayList.get(i).setBattlefieldCharacter(cs);

            // add to the battlefield
            this.m_battlefield.addHero(cs);
        }

        // make up some mobs
        this.addAction(Actions.forever(Actions.sequence(
                Actions.delay(Config.BATTLE_MOB_GENERATION_RATE_BASE),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        Battle.this.generateMob();
                        return true;
                    }
                }
        )));

        // ready to play
        this.m_state = EState.PLAYING;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // check for stage clear
        if (this.m_state == EState.PLAYING) {
            if (this.m_areaInfo.getNumMobsKilledThisStage() == Config.BATTLE_MOBS_PER_STAGE) {
                // stage clear
                this.m_state = EState.STAGE_COMPLETE;

                // increment to next stage
                this.nextStage();
            }
        }
    }

    private void nextStage(){
        // display stage clear
        Label.LabelStyle style = new Label.LabelStyle(GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.HUGE));
        style.background = new TextureRegionDrawable(GameAssetManager.getInstance().getTexture("pixel.png")).tint(new Color(0, 0, 0, 0.75f));
        Label label = new Label("Stage Cleared", style);
        label.setAlignment(Align.center);
        label.setSize(this.m_table.getWidth(), this.m_table.getHeight());
        label.setPosition(this.m_table.getWidth(), 0);
        label.addAction(Actions.sequence(
                Actions.moveTo(0, 0, 1.0f),
                Actions.delay(1.0f),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        // increment stage counter
                        Battle.this.m_areaInfo.setCurrentStage(Battle.this.m_areaInfo.getCurrentStage() + 1);

                        // reset mobs killed and created
                        Battle.this.m_areaInfo.resetForBattle();

                        // back to playing
                        Battle.this.m_state = EState.PLAYING;

                        // done
                        return true;
                    }
                },
                Actions.moveTo(-this.m_table.getWidth(), 0, 1.0f),
                Actions.removeActor()
        ));
        this.m_table.addActor(label);
    }

    private void generateMob(){
        // check how many we have
        if (this.m_areaInfo.getNumMobsCreatedThisStage() < Config.BATTLE_MOBS_PER_STAGE) {
            // not yet at max
            GameState.CharacterState state = this.m_areaInfo.generateMob();
            this.m_battlefield.addMob(state);
            this.m_areaInfo.modifyNumMobsCreatedThisStage(1);
        }
    }
}