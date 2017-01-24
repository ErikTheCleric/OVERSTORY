package onion.szxb74om7zsmd2jm.limitlesslabyrinth.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import onion.szxb74om7zsmd2jm.limitlesslabyrinth.entities.Enemy;
import onion.szxb74om7zsmd2jm.limitlesslabyrinth.entities.Player;
import onion.szxb74om7zsmd2jm.limitlesslabyrinth.threads.Spawn;

import java.util.Random;

/**
 * Created by chris on 1/19/2017.
 */
public class Play implements Screen {

    public TiledMap getMap() {
        return map;
    }
    private static TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    public Player getPlayer() {
        return player;
    }
    private static Player player;
    private Array<Enemy> enemies = new Array<Enemy>();
    private InputMultiplexer im;
    private int[][] spawnTiles;
    private long time = 0;

    @Override
    public void show() {
        map = new TmxMapLoader().load("test.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        camera.zoom = 4f;
        camera.setToOrtho(false);

        player = new Player(new Sprite(new Texture("thor32.png")), 5, 5, 100f, (TiledMapTileLayer) map.getLayers().get(1));

        im = new InputMultiplexer(player);
        Gdx.input.setInputProcessor(im);
        spawnEnemy(new Sprite(new Texture("still1.png")), 1, 1, 100f, (TiledMapTileLayer) map.getLayers().get(1));
        spawnEnemy(new Sprite(new Texture("still1.png")), 15, 15, 100f,  (TiledMapTileLayer) map.getLayers().get(1));
        spawnEnemy(new Sprite(new Texture("still1.png")), 16, 16, 100f,  (TiledMapTileLayer) map.getLayers().get(1));
        spawnEnemy(new Sprite(new Texture("still1.png")), 17, 17, 100f,  (TiledMapTileLayer) map.getLayers().get(1));
        spawnEnemy(new Sprite(new Texture("still1.png")), 18, 18, 100f,  (TiledMapTileLayer) map.getLayers().get(1));
        spawnTiles = (checkMapLayerFor((TiledMapTileLayer) map.getLayers().get(1), "spawnEnemy"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.getSprite().getX() + player.getSprite().getWidth()/2, player.getSprite().getY() + player.getSprite().getHeight()/2, 0);
        camera.update();

        renderer.setView(camera);
        renderer.render();
        renderer.getBatch().begin();
        for(Enemy i : enemies){
            i.draw(renderer.getBatch());
            if(i.getHealth() <= 0) enemies.removeIndex(enemies.indexOf(i, true));
        }
        player.draw(renderer.getBatch());
        renderer.getBatch().end();

        //Spawning in enemies every n seconds
        Random rand = new Random();
        int num = 0;
        num = rand.nextInt(spawnTiles.length);
        if (System.currentTimeMillis() > time) {
            spawnEnemy(new Sprite(new Texture("still1.png")), spawnTiles[num][0], spawnTiles[num][1], 100f, (TiledMapTileLayer) getMap().getLayers().get(1));
            time = System.currentTimeMillis() + 1000;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();

    }

    public void spawnEnemy(Sprite sprite, float x, float y, float health, TiledMapTileLayer collisionLayer){
        enemies.add(new Enemy(sprite, x, y, health, collisionLayer));
        im.addProcessor(enemies.get(enemies.size - 1));
    }

    public int[][] checkMapLayerFor(TiledMapTileLayer layer, String string){
        int count = 0;
        for(int x = 0; x < layer.getWidth(); x++){
            for(int y = 0; y < layer.getHeight(); y++){
                if(layer.getCell(x, y).getTile().getProperties().containsKey(string)) count++;
            }
        }
        int[][] tiles = new int[count][2];
        Gdx.app.log(String.valueOf(layer.getHeight()), string.valueOf(layer.getWidth()));
        count = 0;
        for(int x = 0; x < layer.getWidth(); x++){
            for(int y = 0; y < layer.getHeight(); y++){
                if(layer.getCell(x, y).getTile().getProperties().containsKey(string)) {
                    tiles[count][0] = x;
                    tiles[count++][1] = y;
                }
            }
        }
        return tiles;
    }
}