package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Items.*;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.SuperMario;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;


public class PlayScreen implements Screen {
    //Reference to our Game, used to set Screens
    private SuperMario game;
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Mario player;

    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public PlayScreen(SuperMario game, String mapSrc) {
        atlas = new TextureAtlas("Mario_and_Enemies1.pack");

        this.game = game;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(SuperMario.V_WIDTH / SuperMario.PPM, SuperMario.V_HEIGHT / SuperMario.PPM, gamecam);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load(mapSrc);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / SuperMario.PPM);

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        //create mario in our game world
        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = SuperMario.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }


    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
            if (idef.type == Star.class) {
                items.add(new Star(this, idef.position.x, idef.position.y));
            }
            if (idef.type == Flower.class) {
                items.add(new Flower(this, idef.position.x, idef.position.y));
            }
        }
    }


    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {


    }

    public void handleInput(float dt) {
        // Enters menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            SuperMario.manager.get("audio/sounds/pause.wav", Sound.class).play();
            music.stop();
            game.setScreen(new MenuScreen(game));
        }


        //control our player using immediate impulses
        if (player.currentState != Mario.State.DEAD && player.currentState != Mario.State.NEXTMAP) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                player.fire();
        }

    }

    public void update(float dt) {
        //handle user input first
        handleInput(dt);
        handleSpawningItems();

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(dt);
        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / SuperMario.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        for (Item item : items)
            item.update(dt);

        SuperMario.getHud().update(dt);

        //attach our gamecam to our players.x coordinate
        if (player.currentState != Mario.State.DEAD) {
            // Make sure to not show outside map
            if (player.b2body.getPosition().x < 2)
                gamecam.position.x = 2;
            else if (player.b2body.getPosition().x > 36.4)
                gamecam.position.x = 36.4f;
            else
                gamecam.position.x = player.b2body.getPosition().x;
        }

        //update our gamecam with correct coordinates after changes
        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);

//        System.out.println("X: " + player.b2body.getPosition().x + "    Y: " + player.b2body.getPosition().y);

    }


    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);
        game.batch.end();

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(SuperMario.getHud().stage.getCamera().combined);
        SuperMario.getHud().stage.draw();

        if (gameOver()) {
            // Game is not finished
            if (SuperMario.mapSourcesIterator.hasNext()){

                // No more lives
                if (SuperMario.livesLeft <= 0){
                    SuperMario.setLivesLeft(SuperMario.difficulty);
                    Hud.getMarioLabel().setText("MARIO x " + SuperMario.livesLeft);
                    game.setScreen(new GameOverScreen(game));
                }
                // More lives
                else {
                    var previousMap = SuperMario.mapSourcesIterator.previous();
                    game.setScreen(new PlayScreen(game, SuperMario.mapSourcesIterator.next()));
                    Hud.setWorldTimer(100);
                }
            }
            // Game is finished
            else {
                SuperMario.setLivesLeft(SuperMario.difficulty);
                game.setScreen(new GameOverScreen(game));
            }
            dispose();
        }

        if (nextGame()) {
            // Game is finished
            if (SuperMario.mapSourcesIterator.hasNext()){
                var nextMap = SuperMario.mapSourcesIterator.next();
                game.setScreen(new PlayScreen(game, nextMap));
                Hud.setPause(false);
                Hud.getLevelLabel().setText(nextMap.substring(0, nextMap.indexOf(".")));
            }
            // Game is not finished
            else {
                game.setScreen(new GameOverScreen(game));
//                SuperMario.mapSourcesIterator = SuperMario.mapSources.iterator();
//                game.setScreen(new PlayScreen(game, SuperMario.mapSourcesIterator.next()));
            }
            dispose();
        }

    }

    public boolean gameOver() {
        if (player.currentState == Mario.State.DEAD && player.getStateTimer() > 3) {
            return true;
        }
        return false;
    }

    public boolean nextGame() {
        if (player.currentState == Mario.State.NEXTMAP && player.getStateTimer() > 5.5) {
//        if (player.currentState == Mario.State.NEXTMAP && player.getStateTimer() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gamePort.update(width, height);

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
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
        //dispose of all our opened resources
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
