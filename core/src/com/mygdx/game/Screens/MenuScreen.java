package com.mygdx.game.Screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.SuperMario;

public class MenuScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;

    private final BitmapFont bitmapFont;
    private TextButton.TextButtonStyle redButtonStyle;
    private TextButton.TextButtonStyle whiteButtonStyle;

    public MenuScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(SuperMario.V_WIDTH, SuperMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SuperMario) game).batch);

        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(2);

        redButtonStyle = new TextButton.TextButtonStyle();
        redButtonStyle.font =  bitmapFont;
        redButtonStyle.fontColor = Color.RED;

        whiteButtonStyle = new TextButton.TextButtonStyle();
        whiteButtonStyle.font =  bitmapFont;
        whiteButtonStyle.fontColor = Color.WHITE;

        loadStartStage();
    }

    public void loadStartStage(){
        Table table = new Table();
        table.center();
        table.top();
        table.setFillParent(true);

        BitmapFont bitmapFontScaled2x = new BitmapFont();
        bitmapFontScaled2x.getData().setScale(2.5f);
        Label gameTitle = new Label("Super Mario", new Label.LabelStyle(bitmapFont, Color.RED));

        TextButton newGameButton = new TextButton("New Game", whiteButtonStyle);
        newGameButton.padTop(10);
        newGameButton.center();

        newGameButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                stage.getActors().clear();
                loadDifficultyStage();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

        });

        TextButton controls = new TextButton("Controls", whiteButtonStyle);
        controls.center();

        controls.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                loadControlsStage();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

        });

        TextButton credits = new TextButton("Credits", whiteButtonStyle);
        controls.center();

        credits.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                loadCreditsStage();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

        });

        TextButton exit = new TextButton("Exit", whiteButtonStyle);
        exit.center();

        exit.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Gdx.app.exit();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

        });


        table.add(gameTitle);
        table.row();
        table.add(newGameButton);
        table.row();
        table.add(controls);
        table.row();
        table.add(credits);
        table.row();
        table.add(exit);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    public void loadDifficultyStage(){


        Table table = new Table();
        table.center();
        table.top();
        table.setFillParent(true);

        TextButton easyButton = new TextButton("Easy", whiteButtonStyle);
        easyButton.padTop(10);
        easyButton.center();

        easyButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                SuperMario.difficulty = 1;
                SuperMario.setLivesLeft(1);
                Hud.getMarioLabel().setText("MARIO x " + SuperMario.livesLeft);
                SuperMario.mapSourcesIterator = SuperMario.mapSources.listIterator();
                game.setScreen(new PlayScreen((SuperMario) game, SuperMario.mapSourcesIterator.next()));
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }

        });

        TextButton mediumButton = new TextButton("Medium", whiteButtonStyle);
        mediumButton.center();
        mediumButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                SuperMario.difficulty = 2;
                SuperMario.setLivesLeft(2);
                Hud.getMarioLabel().setText("MARIO x " + SuperMario.livesLeft);
                SuperMario.mapSourcesIterator = SuperMario.mapSources.listIterator();
                game.setScreen(new PlayScreen((SuperMario) game, SuperMario.mapSourcesIterator.next()));
                dispose();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });

        TextButton hardButton = new TextButton("Hard", whiteButtonStyle);
        hardButton.center();
        hardButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                SuperMario.difficulty = 3;
                SuperMario.setLivesLeft(3);
                Hud.getMarioLabel().setText("MARIO x " + SuperMario.livesLeft);
                SuperMario.mapSourcesIterator = SuperMario.mapSources.listIterator();
                game.setScreen(new PlayScreen((SuperMario) game, SuperMario.mapSourcesIterator.next()));
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });

        TextButton backButton = new TextButton("Back", redButtonStyle);
        backButton.center();
        backButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                loadStartStage();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });



        table.add(easyButton);
        table.row();
        table.add(mediumButton);
        table.row();
        table.add(hardButton);
        table.row();
        table.add(backButton);
        stage.addActor(table);


    }

    public void loadControlsStage(){


        Table table = new Table();
        table.center();
        table.top();
        table.setFillParent(true);

        Label move = new Label("Move - Left/Right Arrows ", new Label.LabelStyle(bitmapFont, Color.WHITE));
        Label jump = new Label("Jump - Up Arrow", new Label.LabelStyle(bitmapFont, Color.WHITE));
        Label fireball = new Label("Shoot Fireball - Space", new Label.LabelStyle(bitmapFont, Color.WHITE));
        Label menu = new Label("Menu - Esc", new Label.LabelStyle(bitmapFont, Color.WHITE));

        TextButton backButton = new TextButton("Back", redButtonStyle);


        backButton.center();
        backButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                loadStartStage();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });


        table.center();
        table.add(move);
        table.row();
        table.add(jump);
        table.row();
        table.add(fireball);
        table.row();
        table.add(menu);
        table.row();
        table.add(backButton);
        stage.addActor(table);



    }

    private void loadCreditsStage(){


        Table table = new Table();
        table.center();
        table.top();
        table.setFillParent(true);

        BitmapFont bitmapFontCredits = new BitmapFont();

        bitmapFontCredits.getData().setScale(1);

        Label sprites = new Label("Sprites - https://www.spriters-resource.com/", new Label.LabelStyle(bitmapFontCredits, Color.WHITE));
        Label brent = new Label("Brent Aureli's Code School - https://www.youtube.com/user/Profyx", new Label.LabelStyle(bitmapFontCredits, Color.WHITE));
        Label source = new Label("Inspiration Project - https://github.com/BrentAureli/SuperMario", new Label.LabelStyle(bitmapFontCredits, Color.WHITE));
        Label libGDX = new Label("LibGDX framework https://libgdx.badlogicgames.com", new Label.LabelStyle(bitmapFontCredits, Color.WHITE));


        TextButton backButton = new TextButton("Back", redButtonStyle);


        backButton.center();
        backButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                loadStartStage();
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });


        table.center();
        table.add(sprites);
        table.row();
        table.add(brent);
        table.row();
        table.add(source);
        table.row();
        table.add(libGDX);
        table.row();
        table.add(backButton);
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
