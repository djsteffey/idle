package halfbytesoftware.game.idle.content.combat.areaselector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import halfbytesoftware.game.idle.GameAssetManager;
import halfbytesoftware.game.idle.GameState;
import halfbytesoftware.game.idle.TableWithBackground;

public class AreaSelectorEntry extends Group {
    // variables
    private GameState.CombatAreaInfo m_combatAreaInfo;
    private Label m_nameLabel;
    private Label m_levelRangeLabel;

    // methods
    public AreaSelectorEntry(GameState.CombatAreaInfo combatAreaInfo) {
        // parameters
        this.m_combatAreaInfo = combatAreaInfo;

        // table
        Table table = new TableWithBackground(Color.WHITE);
        table.setFillParent(true);
        this.addActor(table);

        // name label
        this.m_nameLabel = new Label("??????????", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.LARGE));
        this.m_nameLabel.setAlignment(Align.center);
        table.add(this.m_nameLabel).expandX().expandY().fill();

        // level range
        table.row();
        this.m_levelRangeLabel = new Label("??????????", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.SMALLER));
        this.m_levelRangeLabel.setAlignment(Align.center);
        table.add(this.m_levelRangeLabel).expandX().expandY().fill();
    }

    public void unlock() {
        this.m_combatAreaInfo.setLevelLocked(false);
        this.m_nameLabel.setText(this.m_combatAreaInfo.getLevelName());
        this.m_levelRangeLabel.setText("LVL " + this.m_combatAreaInfo.getLevelLow() + " - " + this.m_combatAreaInfo.getLevelHigh());
    }

    public GameState.CombatAreaInfo getCombatAreaInfo() {
        return this.m_combatAreaInfo;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.m_combatAreaInfo.getLevelLocked() == false) {
            this.m_nameLabel.setText(this.m_combatAreaInfo.getLevelName());
            this.m_levelRangeLabel.setText("LVL " + this.m_combatAreaInfo.getLevelLow() + " - " + this.m_combatAreaInfo.getLevelHigh());
        }
        super.draw(batch, parentAlpha);
    }
}
