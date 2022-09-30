package halfbytesoftware.game.idle.content.combat.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import halfbytesoftware.game.idle.GameAssetManager;
import halfbytesoftware.game.idle.GameState;
import halfbytesoftware.game.idle.TableWithBackground;
import halfbytesoftware.game.idle.UiProgressBar;

class CharacterStateDisplay extends Group {
    // variables
    private Image m_overlay;
    private GameState.CharacterState m_characterState;
    private Image m_image;
    private Label m_name;
    private UiProgressBar m_hpBar;

    // methods
    public CharacterStateDisplay() {
        this.m_characterState = null;

        Table table = new TableWithBackground(Color.ORANGE);
        table.setFillParent(true);
        this.addActor(table);

        this.m_image = new Image(GameAssetManager.getInstance().getTexture("invalid.png"));
        table.add(this.m_image).width(32).height(32).fill();

        this.m_name = new Label("Name", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.SMALLER));
        table.add(this.m_name).expandX().expandY().fill();

        table.row();
        this.m_hpBar = new UiProgressBar(100, 75);
        table.add(this.m_hpBar).expandX().expandY().fill().colspan(2);

        // by default no character so overlay it
        this.m_overlay = new Image(GameAssetManager.getInstance().getTexture("pixel.png"));
        this.m_overlay.setColor(Color.BLACK);
        this.addActor(this.m_overlay);
    }

    public void setBattlefieldCharacter(GameState.CharacterState cs) {
        this.m_characterState = cs;
        if (this.m_characterState != null) {
            this.m_image.setDrawable(new TextureRegionDrawable(GameAssetManager.getInstance().getTileset("actors_24x24.png", 24).getRegion(this.m_characterState.getGraphicId())));
            this.m_name.setText(this.m_characterState.getName());
            this.m_hpBar.setValue(this.m_characterState.getHpCurrent(), this.m_characterState.getHpMax());

            this.m_overlay.remove();
        } else {
            this.addActor(this.m_overlay);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.m_characterState != null) {
            this.m_hpBar.setValue(this.m_characterState.getHpCurrent(), this.m_characterState.getHpMax());

            // if dead make sure overlay is applied
            if (this.m_characterState.getIsAlive() == false) {
                if (this.m_overlay.hasParent() == false) {
                    this.m_overlay.setColor(0.0f, 0.0f, 0.0f, 0.75f);
                    this.addActor(this.m_overlay);
                }
            }
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (this.m_overlay != null) {
            this.m_overlay.setSize(this.getWidth(), this.getHeight());
        }
    }
}
