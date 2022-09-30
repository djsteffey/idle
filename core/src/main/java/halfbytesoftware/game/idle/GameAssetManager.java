package halfbytesoftware.game.idle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.util.HashMap;
import java.util.Map;

public class GameAssetManager extends AssetManager {
    public enum ETextSize{
        TINY, SMALLEST, SMALLER, SMALL, STANDARD, LARGE, LARGER, LARGEST, HUGE;
        public int size(){
            switch (this){
                case TINY: return 8;
                case SMALLEST: return 12;
                case SMALLER: return 16;
                case SMALL: return 20;
                case STANDARD: return 24;
                case LARGE: return 28;
                case LARGER: return 32;
                case LARGEST: return 64;
                case HUGE: return 128;
            }
            return 20;
        }
    }

    // variables
    private static GameAssetManager s_instance;
    private final Map<String, NinePatch> m_ninePatches;

    // methods
    public static GameAssetManager getInstance(){
        if (GameAssetManager.s_instance == null){
            GameAssetManager.s_instance = new GameAssetManager();
        }
        return GameAssetManager.s_instance;
    }

    private GameAssetManager(){
        super();
        this.m_ninePatches = new HashMap<>();
    }

    public void loadAssets(){
        // load the ui skin
        this.load("ui/skin.atlas", TextureAtlas.class);
        this.load("ui/skin.json", Skin.class, new SkinLoader.SkinParameter("ui/skin.atlas"));

        // load some images
        this.load("pixel.png", Texture.class);
        this.load("panel.png", Texture.class);
        this.load("panel_thin.png", Texture.class);

        // finish loading everything
        this.finishLoading();

        // make some nine patches
        this.m_ninePatches.put("panel.png", new NinePatch(this.getTexture("panel.png"), 4, 4, 4, 4));
        this.m_ninePatches.put("panel_thin.png", new NinePatch(this.getTexture("panel_thin.png"), 4, 4, 4, 4));
    }

    public Texture getTexture(String name){
        // check if already loaded
        if (this.contains(name) == false){
            // not loaded...check if it exists
            if (Gdx.files.internal(name).exists()){
                // does exist so load it
                this.load(name, Texture.class);
                this.finishLoadingAsset(name);
            }
            else{
                // does not exist so return the invalid
                return this.getTexture("invalid.png");
            }
        }
        return this.get(name, Texture.class);
    }

    public NinePatch getNinePatch(String name){
        return this.m_ninePatches.get(name);
    }

    public Skin getSkin(){
        return this.get("ui/skin.json", Skin.class);
    }

    public BitmapFont getFont(ETextSize size){
        // see if already exists
        if (this.contains("" + size.size(), BitmapFont.class) == false){
            // load it
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/droid_serif_bold.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameters.size = size.size();
            parameters.borderColor = Color.BLACK;
            parameters.borderWidth = 2.0f;
            BitmapFont font = generator.generateFont(parameters);
            this.addAsset("" + size.size(), BitmapFont.class, font);
        }

        // done
        return this.get("" + size.size(), BitmapFont.class);
    }

    public Label.LabelStyle getLabelStyle(ETextSize size){
        // see if the style already exists
        if (this.contains("" + size.size(), Label.LabelStyle.class) == false){
            // need to build it
            Label.LabelStyle style = new Label.LabelStyle(this.getSkin().get(Label.LabelStyle.class));
            style.font = this.getFont(size);
            this.addAsset("" + size.size(), Label.LabelStyle.class, style);
        }

        // done
        return this.get("" + size.size(), Label.LabelStyle.class);
    }

    public TextButton.TextButtonStyle getTextButtonStyle(ETextSize size){
        // see if the style already exists
        if (this.contains("" + size.size(), TextButton.TextButtonStyle.class) == false){
            // need to build it
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(this.getSkin().get(TextButton.TextButtonStyle.class));
            style.font = this.getFont(size);
            this.addAsset("" + size.size(), TextButton.TextButtonStyle.class, style);
        }

        // done
        return this.get("" + size.size(), TextButton.TextButtonStyle.class);
    }

    public TextButton.TextButtonStyle getTextButtonToggleStyle(ETextSize size){
        // see if already exists
        if (this.contains("toggle-" + size.size(), TextButton.TextButtonStyle.class) == false){
            // build it
            TextButton.TextButtonStyle style = this.getTextButtonStyle(size);
            style.checked = style.down;
            this.addAsset("toggle-" + size.size(), TextButton.TextButtonStyle.class, style);
        }
        return this.get("toggle-" + size.size(), TextButton.TextButtonStyle.class);
    }

    public TextField.TextFieldStyle getTextFieldStyle(ETextSize size){
        // see if the style already exists
        if (this.contains("" + size.size(), TextField.TextFieldStyle.class) == false){
            // need to build it
            TextField.TextFieldStyle style = new TextField.TextFieldStyle(this.getSkin().get(TextField.TextFieldStyle.class));
            style.font = this.getFont(size);
            this.addAsset("" + size.size(), TextField.TextFieldStyle.class, style);
        }

        // done
        return this.get("" + size.size(), TextField.TextFieldStyle.class);
    }

    public Slider.SliderStyle getNewSliderStyle(){
        // need to build it
        return new Slider.SliderStyle(this.getSkin().get("default-horizontal", Slider.SliderStyle.class));
    }

    public Tileset getTileset(String name, int tileSize){
        if (this.contains(name, Tileset.class) == false){
            Tileset tileset = new Tileset(this.getTexture(name), tileSize);
            this.addAsset(name, Tileset.class, tileset);
        }
        return this.get(name, Tileset.class);
    }
}


