package halfbytesoftware.game.idle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class UiProgressBar extends Group {
    // variables
    private final Image m_background;
    private final Image m_empty;
    private final Image m_progress;
    protected long m_maxValue;
    protected long m_value;
    private boolean m_dirty;

    // methods
    public UiProgressBar(long maxValue, long value){
        // save
        this.m_maxValue = maxValue;
        this.m_value = value;

        // background
        this.m_background = new Image(GameAssetManager.getInstance().getTexture("pixel.png"));
        this.m_background.setColor(Color.BLACK);
        this.addActor(this.m_background);

        // empty
        this.m_empty = new Image(GameAssetManager.getInstance().getTexture("pixel.png"));
        this.m_empty.setColor(Color.WHITE);
        this.m_empty.setPosition(2, 2);
        this.addActor(this.m_empty);

        // progress
        this.m_progress = new Image(GameAssetManager.getInstance().getTexture("pixel.png"));
        this.m_progress.setColor(Color.GREEN);
        this.m_progress.setPosition(2, 2);
        this.addActor(this.m_progress);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.m_dirty){
            this.m_progress.setWidth((this.getWidth() - 4) * this.m_value / (float)this.m_maxValue);
            this.m_dirty = false;
        }
        super.draw(batch, parentAlpha);
    }

    public void setMaxValue(long maxValue){
        this.m_maxValue = maxValue;
        this.m_dirty = true;
    }

    public void setValue(long value){
        this.m_value = value;
        this.m_dirty = true;
    }

    public void setValue(long value, long maxValue){
        this.m_value = value;
        this.m_maxValue = maxValue;
        this.m_dirty = true;
    }

    public long getValue(){
        return this.m_value;
    }

    public void setProgressColor(Color color){
        this.m_progress.setColor(color);
    }

    public void setEmptyColor(Color color){
        this.m_empty.setColor(color);
    }

    public void setBorderColor(Color color){
        this.m_background.setColor(color);
    }

    public void setColors(Color border, Color empty, Color progress){
        this.m_background.setColor(border);
        this.m_empty.setColor(empty);
        this.m_progress.setColor(progress);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (this.m_background != null) {
            this.m_background.setSize(this.getWidth(), this.getHeight());
        }
        if (this.m_empty != null){
            this.m_empty.setSize(this.getWidth() - 4, this.getHeight() - 4);
        }
        if (this.m_progress != null){
            this.m_progress.setSize(
                    (this.getWidth() - 4) * this.m_value / (float) this.m_maxValue,
                    this.getHeight() - 4);
        }
    }
}


