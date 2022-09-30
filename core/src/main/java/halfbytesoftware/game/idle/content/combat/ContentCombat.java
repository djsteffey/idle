package halfbytesoftware.game.idle.content.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import halfbytesoftware.game.idle.GameState;
import halfbytesoftware.game.idle.TableWithBackground;
import halfbytesoftware.game.idle.content.Content;
import halfbytesoftware.game.idle.content.combat.areaselector.AreaSelector;
import halfbytesoftware.game.idle.content.combat.battle.Battle;

public class ContentCombat extends Content {
    // variables
    private Container<Group> m_container;
    private Battle.IListener m_battleListener;
    private AreaSelector.IListener m_areaSelectorListener;

    // methods
    public ContentCombat(GameState gameState){
        super("Combat", gameState);

        // create listeners
        this.m_battleListener = new Battle.IListener() {
            @Override
            public void onBattleQuit(Battle battle) {
                // start the selector
                ContentCombat.this.m_container.setActor(new AreaSelector(ContentCombat.this.m_gameState, ContentCombat.this.m_areaSelectorListener));
            }
        };

        this.m_areaSelectorListener = new AreaSelector.IListener() {
            @Override
            public void onUiAreaSelectorSelection(AreaSelector ui, GameState.CombatAreaInfo areaInfo) {
                // first check if unlocked
                if (areaInfo.getLevelLocked()){
                    // do nothing
                    // todo maybe try to unlock it?
                    return;
                }

                // reset to stage 1
                areaInfo.setCurrentStage(1);

                // reset mobs created and killed
                areaInfo.resetForBattle();

                // start the battle
                ContentCombat.this.m_container.setActor(new Battle(ContentCombat.this.m_gameState, areaInfo, ContentCombat.this.m_battleListener));
            }
        };

        // table for the container
        Table table = new TableWithBackground(Color.WHITE);
        table.pad(2);
        table.defaults().space(0);
        table.setFillParent(true);
        this.addActor(table);

        // combat content
        this.m_container = new Container<>(new AreaSelector(this.m_gameState, this.m_areaSelectorListener));
        this.m_container.fill();
        table.add(this.m_container).expand().fill();
    }
}
