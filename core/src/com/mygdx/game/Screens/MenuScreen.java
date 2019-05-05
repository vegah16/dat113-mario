package com.mygdx.game.Screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SuperMario;

public class MenuScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;
    //private TextureAtlas atlas;
    //protected Skin skin;



    public MenuScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(SuperMario.V_WIDTH, SuperMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SuperMario) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        //atlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        //skin = new Skin(Gdx.files.internal("skin.json"), atlas);
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label mainMenuLabel = new Label("Main Menu", font);
        //Create buttons
        Label playLabel = new Label("Play", font);
        //TextButton levelSelectButton = new TextButton("Level Select", skin);
        Label exitLabel = new Label("Exit", font);

        //Add listeners to buttons

//        playLabel.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                ((Game)Gdx.app.getApplicationListener()).setScreen(new PlayScreen(new SuperMario()));
//            }
//        });

        exitLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(mainMenuLabel).expandX();
        table.row();
        table.add(playLabel).expandX().padTop(10f);
        table.row();
        //table.add(levelSelectButton).expandX().padTop(20f);
        table.row();
        table.add(exitLabel).expandX().padTop(30f);

        stage.addActor(table);

    }

    @Override
    public void show() {
    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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

        stage.dispose();
    }
}
