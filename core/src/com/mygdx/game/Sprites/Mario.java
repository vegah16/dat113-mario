package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Enemies.Turtle;
import com.mygdx.game.Sprites.Other.FireBall;
import com.mygdx.game.SuperMario;


public class Mario extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, NEXTMAP}

    ;
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private Animation marioRun;
    private TextureRegion marioJump;
    private TextureRegion marioDead;

    private TextureRegion bigMarioStand;
    private Animation bigMarioRun;
    private TextureRegion bigMarioJump;

    private TextureRegion invincibleMarioStand;
    private Animation invincibleMarioRun;
    private TextureRegion invincibleMarioJump;

    private TextureRegion fireMarioStand;
    private Animation fireMarioRun;
    private TextureRegion fireMarioJump;

    private Animation growMario;

    private float stateTimer;

    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean marioIsInvincible;
    private boolean runInvincibleAnimation;
    private boolean timeToDefineInvincibleMario;
    private boolean marioIsFire;
    private boolean runFireAnimation;
    private boolean timeToDefineFireMario;
    private boolean timeToRedefineMario;
    private boolean isDead;
    private boolean hasFinishedMap;
    private boolean collidedWithDoor;
    private boolean destroy;
    private boolean destroyed;

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }


    public boolean hasCollidedWithDoor() {
        return collidedWithDoor;
    }

    public void setCollidedWithDoor(boolean collidedWithDoor) {
        this.collidedWithDoor = collidedWithDoor;
    }


    private PlayScreen screen;

    private Array<FireBall> fireballs;

    public Mario(PlayScreen screen) {
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to marioRun Animation
        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        marioRun = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        bigMarioRun = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("invinciblity_mario"), i * 16, 0, 16, 16));
        invincibleMarioRun = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("fire_mario"), i * 16, 0, 16, 16));
        fireMarioRun = new Animation(0.1f, frames);

        frames.clear();

        //get set animation frames from growing mario
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);


        //get jump animation frames and add them to marioJump Animation
        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
        invincibleMarioJump = new TextureRegion(screen.getAtlas().findRegion("invinciblity_mario"), 80, 0, 16, 16);
        fireMarioJump = new TextureRegion(screen.getAtlas().findRegion("fire_mario"), 80, 0, 16, 16);

        //create texture region for mario standing
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
        invincibleMarioStand = new TextureRegion(screen.getAtlas().findRegion("invinciblity_mario"), 80, 0, 16, 16);
        fireMarioStand = new TextureRegion(screen.getAtlas().findRegion("fire_mario"), 0, 0, 16, 16);

        //create dead mario texture region
        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        //define mario in Box2d
        defineMario();

        //set initial values for marios location, width and height. And initial frame as marioStand.
        setBounds(0, 0, 16 / SuperMario.PPM, 16 / SuperMario.PPM);
        setRegion(marioStand);


        fireballs = new Array<FireBall>();

    }

    public void update(float dt) {

        if (destroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }

        if (hasFinishedMap){
            if (stateTimer > 1) {
                b2body.setLinearVelocity(new Vector2(1, -2));
            }
        }


        // Time up will kill mario
        if (SuperMario.getHud().isTimeUp() && !isDead()) {
            die();
        }

        // Mario will die if he falls under the ground
        if (b2body.getPosition().y < 0){
            die();
        }

        //update our sprite to correspond with the position of our Box2D body
        if (marioIsBig)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / SuperMario.PPM);
        else if (marioIsInvincible)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        else if (marioIsFire)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //update sprite with the correct frame depending on marios current action
        setRegion(getFrame(dt));
        if (timeToDefineBigMario)
            defineBigMario();
        if (timeToDefineInvincibleMario)
            defineInvincibleMario();
        if (timeToDefineFireMario)
            defineFireMario();
        if (timeToRedefineMario)
            redefineMario();

        for (FireBall ball : fireballs) {
            ball.update(dt);
            if (ball.isDestroyed())
                fireballs.removeValue(ball, true);
        }
    }

    public TextureRegion getFrame(float dt) {
        //get marios current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch (currentState) {
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                if  (marioIsBig){
                    region = bigMarioJump;
                } else if (marioIsInvincible){
                    region = invincibleMarioJump;
                } else if (marioIsFire){
                    region = fireMarioJump;
                } else {
                    region = marioJump;
                }
                break;
            case NEXTMAP:
                if (marioIsBig){
                    region = (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true);
                } else{
                    region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                }
            case RUNNING:
                if  (marioIsBig){
                    region = (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true);
                } else if (marioIsInvincible){
                    region = (TextureRegion) invincibleMarioRun.getKeyFrame(stateTimer, true);
                }else if (marioIsFire){
                    region = (TextureRegion) fireMarioRun.getKeyFrame(stateTimer, true);
                } else {
                    region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                }
                break;
            case FALLING:
            case STANDING:
            default:
                if  (marioIsBig){
                    region = bigMarioStand;
                } else if (marioIsInvincible){
                    region = invincibleMarioStand;
                }else if (marioIsFire){
                    region = fireMarioStand;
                } else {
                    region = marioStand;
                }
                break;
        }

        //if mario is running left and the texture isnt facing left... flip it.
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }

        //if mario is running right and the texture isnt facing right... flip it.
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

    }

    public State getState() {
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if (isDead)
            return State.DEAD;
        else if (hasFinishedMap)
            return State.NEXTMAP;
        else if (runGrowAnimation)
            return State.GROWING;
        else if ((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
            //if negative in Y-Axis mario is falling
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if mario is positive or negative in the X axis he is running
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
            //if none of these return then he must be standing
        else
            return State.STANDING;
    }

    public void grow() {
        if (!isBig()) {
            runGrowAnimation = true;
            marioIsBig = true;
            timeToDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        }
    }

    public void growInvincible() {
        if (!isInvincible()) {
            runGrowAnimation = false;
            marioIsInvincible = true;
            timeToDefineInvincibleMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight());
            SuperMario.manager.get("audio/sounds/coin.wav", Sound.class).play();

            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    marioIsInvincible = false;
                    timeToRedefineMario = true;
                    setBounds(getX(), getY(), getWidth(), getHeight());
                    SuperMario.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
                }
            }, 10);
        }
    }

    public void growFire() {
        if (!isFire()) {
            runGrowAnimation = false;
            marioIsFire = true;
            timeToDefineFireMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight());
            SuperMario.manager.get("audio/sounds/coin.wav", Sound.class).play();

            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    marioIsFire = false;
                    timeToRedefineMario = true;
                    setBounds(getX(), getY(), getWidth(), getHeight());
                    SuperMario.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
                }
            }, 10);
        }
    }

    public void die() {

        if (!isDead()) {

            SuperMario.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            SuperMario.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            SuperMario.livesLeft--;
            Hud.getMarioLabel().setText("MARIO x " + SuperMario.livesLeft);
            isDead = true;
            Filter filter = new Filter();
            filter.maskBits = SuperMario.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public void goNextMap() {

        if (!hasFinishedMap()) {

            SuperMario.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            SuperMario.manager.get("audio/sounds/stage_clear.wav", Sound.class).play();
            hasFinishedMap = true;

            // Update HUD
            Hud.setPause(true);
            Hud.addScore(Hud.getWorldTimer() * 50);
            Hud.setWorldTimer(100);

            Filter filter = new Filter();
            filter.categoryBits = SuperMario.MARIO_BIT;
            filter.maskBits = SuperMario.GROUND_BIT |
                    SuperMario.COIN_BIT |
                    SuperMario.BRICK_BIT |
                    SuperMario.ENEMY_BIT |
                    SuperMario.OBJECT_BIT |
                    SuperMario.ENEMY_HEAD_BIT |
                    SuperMario.ITEM_BIT |
                    SuperMario.DOOR_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

//            Timer.schedule(new Timer.Task(){
//                @Override
//                public void run() {
////                        b2body.applyLinearImpulse(new Vector2(1, 0), b2body.getWorldCenter(), true);
//                    b2body.setLinearVelocity(new Vector2(2, -2));
//                }
//            }, 1);
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean hasFinishedMap() {
        return hasFinishedMap;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public boolean isBig() {
        return marioIsBig;
    }

    public boolean isInvincible() {
        return marioIsInvincible;
    }

    public boolean isFire() {
        return marioIsFire;
    }

    public void jump() {
        if (currentState != State.JUMPING) {
            if (marioIsBig)
                SuperMario.manager.get("audio/sounds/jump_big.wav", Sound.class).play();
            else
                SuperMario.manager.get("audio/sounds/jump.wav", Sound.class).play();
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void hit(Enemy enemy) {
        if (enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.STANDING_SHELL)
            ((Turtle) enemy).kick(enemy.b2body.getPosition().x > b2body.getPosition().x ? Turtle.KICK_RIGHT : Turtle.KICK_LEFT);
        else {
            if (marioIsInvincible){
                enemy.setToDestroy();
            }
             else if (marioIsBig) {
                marioIsBig = false;
                timeToRedefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                SuperMario.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            }
            else {
                die();
            }
        }
    }

    public void redefineMario() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.ENEMY_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_HEAD_BIT |
                SuperMario.ITEM_BIT |
                SuperMario.POLE_BIT |
                SuperMario.DOOR_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / SuperMario.PPM, 6 / SuperMario.PPM), new Vector2(2 / SuperMario.PPM, 6 / SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

        timeToRedefineMario = false;

    }

    public void defineBigMario() {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / SuperMario.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.ENEMY_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_HEAD_BIT |
                SuperMario.ITEM_BIT |
                SuperMario.POLE_BIT |
                SuperMario.DOOR_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / SuperMario.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / SuperMario.PPM, 6 / SuperMario.PPM), new Vector2(2 / SuperMario.PPM, 6 / SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }

    public void defineInvincibleMario() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.ENEMY_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_HEAD_BIT |
                SuperMario.ITEM_BIT |
                SuperMario.POLE_BIT |
                SuperMario.DOOR_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / SuperMario.PPM, 6 / SuperMario.PPM), new Vector2(2 / SuperMario.PPM, 6 / SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineInvincibleMario = false;
    }

    public void defineFireMario() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.ENEMY_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_HEAD_BIT |
                SuperMario.ITEM_BIT |
                SuperMario.POLE_BIT |
                SuperMario.DOOR_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / SuperMario.PPM, 6 / SuperMario.PPM), new Vector2(2 / SuperMario.PPM, 6 / SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

        timeToDefineFireMario = false;
    }



    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(1, 32 / SuperMario.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.ENEMY_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_HEAD_BIT |
                SuperMario.ITEM_BIT |
                SuperMario.POLE_BIT |
                SuperMario.DOOR_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / SuperMario.PPM, 6 / SuperMario.PPM), new Vector2(2 / SuperMario.PPM, 6 / SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void fire() {
        SuperMario.manager.get("audio/sounds/fireball.wav", Sound.class).play();
        fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
    }

    public void draw(Batch batch) {
        if (!destroyed)
            super.draw(batch);
        for (FireBall ball : fireballs)
            ball.draw(batch);
    }
}
