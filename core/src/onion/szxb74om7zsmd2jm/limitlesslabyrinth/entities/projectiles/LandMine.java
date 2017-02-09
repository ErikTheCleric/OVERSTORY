package onion.szxb74om7zsmd2jm.limitlesslabyrinth.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import onion.szxb74om7zsmd2jm.limitlesslabyrinth.screens.Play;

/**
 * Created by chris on 2/7/2017.
 */
public class LandMine extends Projectile {
    public LandMine(float x1, float y1, float x2, float y2){
        dmg = Play.getPlayer().getDmg();
        sprite = new Sprite(new Texture("LandMine.png"));
        x = Play.getPlayer().getSprite().getX();
        y = Play.getPlayer().getSprite().getY();
    }

    public LandMine(){

    }

    @Override
    public void contact() {
        Play.getProjectiles().add(new Explosion(sprite.getX(), sprite.getY(), dmg));
        remove();
    }

    @Override
    public void draw() {
        time = System.currentTimeMillis() + 10000;
        sprite.setPosition(x,y);
        sprite.draw(Play.getRenderer().getBatch());
    }
}