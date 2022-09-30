package halfbytesoftware.game.idle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class TableWithBackground extends Table {
    // variables
    private NinePatchDrawable m_background;

    // methods
    public TableWithBackground(Color color){
        this.m_background = new NinePatchDrawable(GameAssetManager.getInstance().getNinePatch("panel_thin.png")).tint(color);
        this.setBackground(this.m_background);
        this.pad(8);
        this.defaults().space(4);
    }

    @Override
    protected void sizeChanged() {
        if (this.m_background != null){
            this.m_background.setMinSize(this.getWidth(), this.getHeight());
        }
        super.sizeChanged();
    }
}
