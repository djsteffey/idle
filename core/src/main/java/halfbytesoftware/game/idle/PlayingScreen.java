package halfbytesoftware.game.idle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.HashMap;
import java.util.Map;
import halfbytesoftware.game.idle.content.Content;
import halfbytesoftware.game.idle.content.combat.ContentCombat;
import halfbytesoftware.game.idle.content.ContentCrafting;
import halfbytesoftware.game.idle.content.ContentGathering;
import halfbytesoftware.game.idle.content.ContentHeroes;

public class PlayingScreen extends AbstractScreen {
    private static class Header extends Group {
        // variables

        // methods
        public Header(){
            // table for everything
            Table table = new TableWithBackground(Color.WHITE);
            table.setFillParent(true);
            this.addActor(table);
        }
    }

    private static class Footer extends Group {
        // variables

        // methods
        public Footer(){
            Table table = new TableWithBackground(Color.WHITE);
            table.setFillParent(true);
            this.addActor(table);
        }
    }

    private static class Tabs extends Group{
        public interface IListener{
            void onTabsTabClicked(Tabs tabs, String tabName);
        }

        // variables
        protected Table m_table;
        private ClickListener m_clickListener;
        private IListener m_listener;

        // methods
        public Tabs(IListener listener){
            // parameters
            this.m_listener = listener;

            // table
            this.m_table = new TableWithBackground(Color.WHITE);
            this.m_table.setFillParent(true);
            this.addActor(this.m_table);

            // click listener
            this.m_clickListener = new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Tabs.this.onTabClicked((TextButton) event.getListenerActor());
                }
            };
        }

        public void addTab(String text){
            this.m_table.row();
            TextButton button = new TextButton(text, GameAssetManager.getInstance().getTextButtonStyle(GameAssetManager.ETextSize.STANDARD));
            button.addListener(this.m_clickListener);
            this.m_table.add(button).expandX().height(48).fill();
        }

        private void onTabClicked(TextButton textButton){
            this.m_listener.onTabsTabClicked(this, textButton.getText().toString());
        }
    }

    // variables
    private Header m_header;
    private Footer m_footer;
    private Tabs m_tabs;
    private Container<Content> m_contentContainer;
    private Map<String, Content> m_contentMap;

    // methods
    public PlayingScreen(IGameServices gs) {
        super(gs);

        // table for everything
        Table table = new Table();
        table.setFillParent(true);
        table.pad(4);
        table.defaults().space(8);
        this.m_stage.addActor(table);

        // header
        this.m_header = new Header();
        table.add(this.m_header).expandX().height(64).fill().colspan(2);

        // tabs
        table.row();
        this.m_tabs = new Tabs(new Tabs.IListener() {
            @Override
            public void onTabsTabClicked(Tabs tabs, String tabName) {
                PlayingScreen.this.onTabsTabClicked(tabs, tabName);
            }
        });
        table.add(this.m_tabs).width(192).expandY().fill();

        // content
        this.m_contentContainer = new Container<>();
        this.m_contentContainer.fill();
        table.add(this.m_contentContainer).expandX().expandY().fill();

        // footer
        table.row();
        this.m_footer = new Footer();
        table.add(this.m_footer).expandX().height(64).fill().colspan(2);

        // create content
        this.m_contentMap = new HashMap<>();
        this.addContent(new ContentHeroes(this.m_gameServices.getGameState()));
        this.addContent(new ContentCombat(this.m_gameServices.getGameState()));
        this.addContent(new ContentGathering(this.m_gameServices.getGameState()));
        this.addContent(new ContentCrafting(this.m_gameServices.getGameState()));

        // initial content
        this.setContent("Heroes");
    }

    private void addContent(Content content){
        this.m_tabs.addTab(content.getName());
        this.m_contentMap.put(content.getName(), content);
    }

    private void setContent(String name){
        this.m_contentContainer.setActor(this.m_contentMap.get(name));
    }

    private void onTabsTabClicked(Tabs tabs, String tabName){
        // set the content
        this.setContent(tabName);
    }
}
