package halfbytesoftware.game.idle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class Tileset {
    // variables
    private final List<TextureRegion> m_regions;

    // methods
    public Tileset(Texture texture, int tile_size){
        this.m_regions = new ArrayList<>();
        for (int y = 0; y < texture.getHeight(); y += tile_size){
            for (int x = 0; x < texture.getWidth(); x += tile_size){
                this.m_regions.add(new TextureRegion(texture, x, y, tile_size, tile_size));
            }
        }
    }

    public TextureRegion getRegion(int index){
        return this.m_regions.get(index);
    }
}

