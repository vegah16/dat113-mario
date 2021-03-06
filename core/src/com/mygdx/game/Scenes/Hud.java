package com.mygdx.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SuperMario;


public class Hud implements Disposable {

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    //Mario score/time Tracking Variables
    private static Integer worldTimer;
    private static boolean timeUp; // true when the world timer reaches 0
    private static float timeCount;
    private static Integer score;
    private static boolean pause;

    //Scene2D widgets
    private static Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private static Label levelLabel;
    private Label worldLabel;
    private static Label marioLabel;

    public Hud(SpriteBatch sb) {
        //define our tracking variables
        worldTimer = 100;
        timeCount = 0;
        score = 0;
        pause = false;


        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(SuperMario.V_WIDTH, SuperMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO x " + SuperMario.livesLeft, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        //add our table to the stage
        stage.addActor(table);

    }

    public void update(float dt) {
        if (!pause){
            timeCount += dt;
            if (timeCount >= 1) {
                if (worldTimer > 0) {
                    worldTimer--;
                } else {
                    timeUp = true;
                }
                countdownLabel.setText(String.format("%03d", worldTimer));
                timeCount = 0;
            }
        }
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public static void reset(){
        setWorldTimer(100);
        levelLabel.setText("1-1");
        setScore(0);
        setPause(false);
        timeUp = false;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public static Integer getWorldTimer() {
        return worldTimer;
    }

    public static void setWorldTimer(Integer worldTimer) {
        Hud.worldTimer = worldTimer;
        countdownLabel.setText(String.format("%03d", worldTimer));
    }

    public static boolean isPause() {
        return pause;
    }

    public static void setPause(boolean pause) {
        Hud.pause = pause;
    }

    public static Label getLevelLabel() {
        return levelLabel;
    }

    public static Integer getScore() {
        return score;
    }

    public static void setScore(Integer score) {
        Hud.score = score;
        scoreLabel.setText(String.format("%06d", score));
    }

    public static Label getMarioLabel() {
        return marioLabel;
    }

    public static void setMarioLabel(Label marioLabel) {
        Hud.marioLabel = marioLabel;
    }
}
