package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SuperMario;

import static com.google.gwt.thirdparty.guava.common.collect.Iterators.peekingIterator;


public class GameOverScreen implements Screen {
    private final BitmapFont bitmapFont;
    private Viewport viewport;
    private Stage stage;

    private Game game;

    public GameOverScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(SuperMario.V_WIDTH, SuperMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SuperMario) game).batch);

        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(2);

        Table table = new Table();
        table.center();
        table.top();
        table.setFillParent(true);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font =  bitmapFont;
        buttonStyle.fontColor = Color.WHITE;


        BitmapFont bitmapFontScaled2x = new BitmapFont();
        bitmapFontScaled2x.getData().setScale(2.5f);
        Label gameTitle = new Label("Game Over", new Label.LabelStyle(bitmapFont, Color.RED));


        TextButton playAgainButton = new TextButton("Play again", buttonStyle);
        playAgainButton.padTop(10);
        playAgainButton.center();

        playAgainButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                SuperMario.mapSourcesIterator = SuperMario.mapSources.listIterator();
                Hud.reset();
                game.setScreen(new PlayScreen((SuperMario) game, SuperMario.mapSourcesIterator.next()));
                dispose();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

        });

        TextButton menu = new TextButton("Menu", buttonStyle);
        menu.center();

        menu.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game));
                dispose();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

        });

        table.add(gameTitle);
        table.row();
        table.add(playAgainButton);
        table.row();
        table.add(menu);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
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
