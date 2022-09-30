package halfbytesoftware.game.idle.content;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import java.util.List;
import halfbytesoftware.game.idle.GameAssetManager;
import halfbytesoftware.game.idle.GameState;
import halfbytesoftware.game.idle.TableWithBackground;

public class ContentHeroes extends Content {
    private static class UiHeroStats extends Group{
        // variables
        private GameState.CharacterState m_characterState;
        private Label m_labelName;
        private Label m_labelClazz;
        private Label m_labelEffectiveStrength;
        private Label m_labelEffectiveIntelligence;
        private Label m_labelEffectiveDexterity;

        // methods
        public UiHeroStats(){
            // parameters
            this.m_characterState = null;

            // table for everything
            Table table = new TableWithBackground(Color.WHITE);
            table.setFillParent(true);
            this.addActor(table);

            // header
            Label label = new Label("Stats", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.STANDARD));
            label.setAlignment(Align.center);
            table.add(label).expandX().fill();

            // name
            table.row();
            this.m_labelName = new Label("", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.SMALL));
            this.m_labelName.setAlignment(Align.center);
            table.add(this.m_labelName).expandX().fill();

            // clazz
            table.row();
            this.m_labelClazz = new Label("", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.SMALL));
            this.m_labelClazz.setAlignment(Align.center);
            table.add(this.m_labelClazz).expandX().fill();

            // strength
            table.row();
            this.m_labelEffectiveStrength = new Label("", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.SMALL));
            this.m_labelEffectiveStrength.setAlignment(Align.center);
            table.add(this.m_labelEffectiveStrength).expandX().fill();

            // intelligence
            table.row();
            this.m_labelEffectiveIntelligence = new Label("", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.SMALL));
            this.m_labelEffectiveIntelligence.setAlignment(Align.center);
            table.add(this.m_labelEffectiveIntelligence).expandX().fill();

            // dexterity
            table.row();
            this.m_labelEffectiveDexterity = new Label("", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.SMALL));
            this.m_labelEffectiveDexterity.setAlignment(Align.center);
            table.add(this.m_labelEffectiveDexterity).expandX().fill();
        }

        public void setHeroState(GameState.CharacterState characterState){
            this.m_characterState = characterState;
            if (this.m_characterState == null){
                this.m_labelName.setText("");
                this.m_labelClazz.setText("");
                this.m_labelEffectiveStrength.setText("");
                this.m_labelEffectiveIntelligence.setText("");
                this.m_labelEffectiveDexterity.setText("");
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (this.m_characterState != null){
                this.m_labelName.setText(this.m_characterState.getName());
                this.m_labelClazz.setText(this.m_characterState.getClazz());
                this.m_labelEffectiveStrength.setText("Str: " + this.m_characterState.getEffectiveStrength());
                this.m_labelEffectiveIntelligence.setText("Int: " + this.m_characterState.getEffectiveIntelligence());
                this.m_labelEffectiveDexterity.setText("Dex: " + this.m_characterState.getEffectiveDexterity());
            }
            super.draw(batch, parentAlpha);
        }
    }

    private static class UiHeroSkills extends Group{
        // variables
        private GameState.CharacterState m_characterState;

        // methods
        public UiHeroSkills(){
            Table table = new TableWithBackground(Color.WHITE);
            table.setFillParent(true);
            this.addActor(table);

            Label label = new Label("Skills", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.STANDARD));
            label.setAlignment(Align.center);
            table.add(label).expandX().fill();
        }

        public void setHeroState(GameState.CharacterState characterState){
            this.m_characterState = characterState;
        }
    }

    private static class UiHeroEquipment extends Group{
        // variables
        private GameState.CharacterState m_characterState;

        // methods
        public UiHeroEquipment(){
            Table table = new TableWithBackground(Color.WHITE);
            table.setFillParent(true);
            this.addActor(table);

            Label label = new Label("Equipment", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.STANDARD));
            label.setAlignment(Align.center);
            table.add(label).expandX().fill();
        }

        public void setHeroState(GameState.CharacterState characterState){
            this.m_characterState = characterState;
        }
    }

    private static class UiHeroSelector extends Group{
        public interface IListener{
            void onHeroSelectorSelected(UiHeroSelector selector, GameState.CharacterState characterState);
        }

        private static class UiHeroSelectorEntry extends Button {
            // variables
            private Image m_image;

            // methods
            public UiHeroSelectorEntry(){
                super(GameAssetManager.getInstance().getSkin());

                this.m_image = new Image(GameAssetManager.getInstance().getTexture("invalid.png"));
                this.m_image.setPosition(this.getWidth() / 2, this.getHeight() / 2, Align.center);
                this.addActor(this.m_image);
            }

            @Override
            protected void sizeChanged() {
                super.sizeChanged();
                if (this.m_image != null){
                    this.m_image.setSize(this.getHeight() - 8, this.getHeight() - 8);
                    this.m_image.setPosition(this.getWidth() / 2, this.getHeight() / 2, Align.center);
                }
            }
        }

        // variables

        // methods
        public UiHeroSelector(List<GameState.CharacterState> characterStateList, IListener listener){
            Table table = new TableWithBackground(Color.WHITE);
            table.setFillParent(true);
            this.addActor(table);

            for (GameState.CharacterState characterState : characterStateList){
                UiHeroSelectorEntry button = new UiHeroSelectorEntry();
                button.setUserObject(characterState);
                button.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        listener.onHeroSelectorSelected(UiHeroSelector.this, (GameState.CharacterState) button.getUserObject());
                    }
                });
                table.add(button).expandX().expandY().fill();
            }
        }
    }

    private static class UiInventory extends Group{
        // variables

        // methods
        public UiInventory(){
            Table table = new TableWithBackground(Color.WHITE);
            table.setFillParent(true);
            this.addActor(table);

            Label label = new Label("Inventory", GameAssetManager.getInstance().getLabelStyle(GameAssetManager.ETextSize.STANDARD));
            label.setAlignment(Align.center);
            table.add(label).expandX().fill();
        }
    }

    // variables
    private UiHeroStats m_uiHeroStats;
    private UiHeroSkills m_uiHeroSkills;
    private UiHeroEquipment m_uiHeroEquipment;

    // methods
    public ContentHeroes(GameState gameState){
        super("Heroes", gameState);

        Table table = new TableWithBackground(Color.WHITE);
        table.setFillParent(true);
        this.addActor(table);

        Table characterInfoTable = new Table();
        characterInfoTable.defaults().space(4);
        table.add(characterInfoTable).expandX().expandY().fill();

        UiInventory inventory = new UiInventory();
        table.add(inventory).width(512).expandY().fill();

        this.m_uiHeroStats = new UiHeroStats();
        characterInfoTable.add(this.m_uiHeroStats).expandX().expandY().fill();

        this.m_uiHeroSkills = new UiHeroSkills();
        characterInfoTable.add(this.m_uiHeroSkills).expandX().expandY().fill();

        this.m_uiHeroEquipment = new UiHeroEquipment();
        characterInfoTable.add(this.m_uiHeroEquipment).expandX().expandY().fill();

        characterInfoTable.row();
        UiHeroSelector heroSelector = new UiHeroSelector(this.m_gameState.getCharacterStateList(), new UiHeroSelector.IListener() {
            @Override
            public void onHeroSelectorSelected(UiHeroSelector selector, GameState.CharacterState heroState) {
                ContentHeroes.this.onHeroSelectorSelected(selector, heroState);
            }
        });
        characterInfoTable.add(heroSelector).expandX().height(64).fill().colspan(3);
    }

    private void onHeroSelectorSelected(UiHeroSelector selector, GameState.CharacterState characterState){
        this.m_uiHeroStats.setHeroState(characterState);
        this.m_uiHeroSkills.setHeroState(characterState);
        this.m_uiHeroEquipment.setHeroState(characterState);
    }
}
