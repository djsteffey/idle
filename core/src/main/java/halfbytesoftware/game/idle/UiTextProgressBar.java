package halfbytesoftware.game.idle;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class UiTextProgressBar extends UiProgressBar {
    // variables
    private final Label m_label;
    private final boolean m_textFollowValue;

    // methods
    public UiTextProgressBar(long max_value, long value, String text, boolean textFollowValue) {
        this(max_value, value, text, GameAssetManager.ETextSize.STANDARD, textFollowValue);
    }

    public UiTextProgressBar(long max_value, long value, String text, GameAssetManager.ETextSize textSize, boolean textFollowValue) {
        super(max_value, value);

        this.m_label = new Label(text, GameAssetManager.getInstance().getLabelStyle(textSize));
        this.m_label.setX(4.0f);
        this.addActor(this.m_label);

        this.m_textFollowValue = textFollowValue;
        if (this.m_textFollowValue){
            this.m_label.setAlignment(Align.center);
        }
        else{
            this.m_label.setX(4.0f);
        }

        this.setValue(value, max_value);
    }

    @Override
    public void setValue(long value) {
        super.setValue(value);
        this.m_label.setText(this.m_value + " / " + this.m_maxValue);
    }

    @Override
    public void setMaxValue(long max_value) {
        super.setMaxValue(max_value);
        this.m_label.setText(this.m_value + " / " + this.m_maxValue);
    }

    @Override
    public void setValue(long value, long maxValue){
        super.setValue(value, maxValue);
        this.m_label.setText(this.m_value + " / " + this.m_maxValue);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (this.m_textFollowValue){
            this.m_label.setSize(this.getWidth(), this.getHeight());
        }
        else {
            this.m_label.setY(this.getHeight() / 2, Align.center);
        }
    }
}

