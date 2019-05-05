package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.MenuScreen;
import com.mygdx.game.Screens.PlayScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class SuperMario extends Game {
    //Virtual Screen size and Box2D Scale(Pixels Per Meter)
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
    public static final float PPM = 100;

    //Box2D Collision Bits
    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short ITEM_BIT = 256;
    public static final short MARIO_HEAD_BIT = 512;
    public static final short FIREBALL_BIT = 1024;
    public static final short POLE_BIT = 2048;
    public static final short DOOR_BIT = 4096;

    public static ArrayList<String> mapSources;
    public static ListIterator<String> mapSourcesIterator;

    public static Hud hud;

    public static int difficulty;
    public static int livesLeft;

    public SpriteBatch batch;

    /* WARNING Using AssetManager in a static way can cause issues, especially on Android.
    Instead you may want to pass around Assetmanager to those the classes that need it.
    We will use it in the static context to save time for now. */
    public static AssetManager manager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("audio/music/mario_music.ogg", Music.class);
        manager.load("audio/sounds/coin.wav", Sound.class);
        manager.load("audio/sounds/bump.wav", Sound.class);
        manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
        manager.load("audio/sounds/powerup.wav", Sound.class);
        manager.load("audio/sounds/powerdown.wav", Sound.class);
        manager.load("audio/sounds/stomp.wav", Sound.class);
        manager.load("audio/sounds/mariodie.wav", Sound.class);
        manager.load("audio/sounds/stage_clear.wav", Sound.class);
        manager.load("audio/sounds/jump.wav", Sound.class);
        manager.load("audio/sounds/jump_big.wav", Sound.class);

        manager.finishLoading();

        mapSources = new ArrayList<String>();
        mapSources.add("1-1.tmx");
        mapSources.add("1-2.tmx");
        mapSources.add("1-3.tmx");

        // Set difficulty.
        // 1 = Easy, , 6 life
        // 2 = Medium, 3 life
        // 3 = Hard, 1 life
//        difficulty = 2;
//        setLivesLeft(difficulty);

        //create our game HUD for scores/timers/level info
        hud = new Hud(this.batch);


//        setScreen(new PlayScreen(this, mapSourcesIterator.next()));
        setScreen(new MenuScreen(this));
    }


    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        batch.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    public static Hud getHud() {
        return hud;
    }

    public static void setHud(Hud hud) {
        SuperMario.hud = hud;
    }

    public static int getLivesLeft() {
        return livesLeft;
    }

    public static void setLivesLeft(int difficulty) {
        switch (difficulty){
            case 1:
                livesLeft = 6;
                break;
            case 2:
                livesLeft = 3;
                break;
            case 3:
                livesLeft = 1;
                break;
            default:
        }
    }
}
