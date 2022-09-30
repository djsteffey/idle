package halfbytesoftware.game.idle.content.combat.areaselector;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import halfbytesoftware.game.idle.GameState;

public class AreaSelector extends Group {
    public interface IListener{
        void onUiAreaSelectorSelection(AreaSelector ui, GameState.CombatAreaInfo areaInfo);
    }

    // variables
    private ClickListener m_entryClickListener;

    // methods
    public AreaSelector(GameState gameState, IListener listener){
        // listener for entry clicks
        this.m_entryClickListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // handle to the info
                GameState.CombatAreaInfo info = ((AreaSelectorEntry)event.getListenerActor()).getCombatAreaInfo();

                // call selector listener
                listener.onUiAreaSelectorSelection(AreaSelector.this, info);

                // super
                super.clicked(event, x, y);
            }
        };

        // table for the selector
        Table table = new Table();
        table.setFillParent(true);
        table.pad(8);
        table.defaults().space(8);
        this.addActor(table);

        // build the areas
        int count = 0;
        for (GameState.CombatAreaInfo info : gameState.getCombatAreaInfoList()){
            // build the entry
            AreaSelectorEntry entry = new AreaSelectorEntry(info);
            entry.addListener(this.m_entryClickListener);
            table.add(entry).expandX().expandY().fill();

            // increment counter
            count += 1;

            // compute for 5 wide
            if (count == 5){
                count = 0;
                table.row();
            }
        }
    }
}
