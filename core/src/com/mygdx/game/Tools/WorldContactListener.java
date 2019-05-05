package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Items.Item;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Sprites.Other.FireBall;
import com.mygdx.game.Sprites.TileObjects.InteractiveTileObject;
import com.mygdx.game.SuperMario;


public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case SuperMario.MARIO_HEAD_BIT | SuperMario.BRICK_BIT:
            case SuperMario.MARIO_HEAD_BIT | SuperMario.COIN_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case SuperMario.ENEMY_HEAD_BIT | SuperMario.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                else
                    ((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                break;
            case SuperMario.ENEMY_BIT | SuperMario.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case SuperMario.MARIO_BIT | SuperMario.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit((Enemy) fixB.getUserData());
                else
                    ((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
                break;
            case SuperMario.ENEMY_BIT | SuperMario.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).hitByEnemy((Enemy) fixB.getUserData());
                ((Enemy) fixB.getUserData()).hitByEnemy((Enemy) fixA.getUserData());
                break;
            case SuperMario.ITEM_BIT | SuperMario.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ITEM_BIT)
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case SuperMario.ITEM_BIT | SuperMario.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
            case SuperMario.FIREBALL_BIT | SuperMario.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.FIREBALL_BIT)
                    ((FireBall) fixA.getUserData()).setToDestroy();
                else
                    ((FireBall) fixB.getUserData()).setToDestroy();
                break;
            case SuperMario.FIREBALL_BIT | SuperMario.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).setToDestroy();
                    ((FireBall) fixB.getUserData()).setToDestroy();
                }
                else {
                    ((Enemy) fixB.getUserData()).setToDestroy();
                    ((FireBall) fixA.getUserData()).setToDestroy();
                }
                break;
            case SuperMario.MARIO_BIT | SuperMario.POLE_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.MARIO_BIT)
                    ((Mario) fixA.getUserData()).goNextMap();
                else
                    ((Mario) fixB.getUserData()).goNextMap();
                break;
            case SuperMario.MARIO_BIT | SuperMario.DOOR_BIT:
                if (fixA.getFilterData().categoryBits == SuperMario.MARIO_BIT)
                    ((Mario) fixA.getUserData()).setDestroy(true);
                else
                    ((Mario) fixB.getUserData()).setDestroy(true);
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
