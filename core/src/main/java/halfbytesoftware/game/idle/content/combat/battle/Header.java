package halfbytesoftware.game.idle.content.combat.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import halfbytesoftware.game.idle.Config;
import halfbytesoftware.game.idle.GameAssetManager;
import halfbytesoftware.game.idle.GameState;

class Header extends Group {
    public interface IListener {
        void onHeaderQuitClick(Header header);
    }

    // variables
    private GameState.CombatAreaInfo m_combatAreaInfo;
    private Image m_background;
    private Label m_labelAreaName;
    private Label m_labelStage;
    private Label m_labelNumMobs;

    // methods
    public Header(GameState.CombatAreaInfo areaInfo, IListener listener) {
        // parameter
        this.m_combatAreaInfo = areaInfo;

        // header background
        this.m_background = new Image(GameAssetManager.getInstance().getTexture("pixel.png"));
        this.m_background.setColor(Color.GRAY);
        this.addActor(this.m_background);

        // table for header ingo
        Table table = new Table();
        table.pad(4);
        table.defaults().space(4);
        table.setFillParent(true);
        this.addActor(table);

        // area name
        this.m_labelAreaName = new Label(this.m_combatAreaInfo.getLevelName(), GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.STANDARD));
        this.m_labelAreaName.setAlignment(Align.center);
        table.add(this.m_labelAreaName).expandX().expandY().fill();

        // stage
        this.m_labelStage = new Label("" + this.m_combatAreaInfo.getCurrentStage(), GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.STANDARD));
        this.m_labelStage.setAlignment(Align.center);
        table.add(this.m_labelStage).expandX().expandY().fill();

        // num mobs remaining
        this.m_labelNumMobs = new Label("0", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.STANDARD));
        this.m_labelNumMobs.setAlignment(Align.center);
        table.add(this.m_labelNumMobs).expandX().expandY().fill();

        // exit area button
        TextButton button = new TextButton("Quit", GameAssetManager.getInstance().getTextButtonStyle(GameAssetManager.ETextSize.STANDARD));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                listener.onHeaderQuitClick(Header.this);
            }
        });
        table.add(button).width(96).expandY().fill();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.m_labelStage.setText("Stage " + this.m_combatAreaInfo.getCurrentStage());
        this.m_labelNumMobs.setText("Defeated: " + this.m_combatAreaInfo.getNumMobsKilledThisStage() + "/" + Config.BATTLE_MOBS_PER_STAGE);
        super.draw(batch, parentAlpha);
    }

    public void setNumMobs(int numMobs) {
        this.m_labelNumMobs.setText("" + numMobs);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (this.m_background != null) {
            this.m_background.setSize(this.getWidth(), this.getHeight());
        }
    }
}
