package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {

    final EarthDefense game;

    public GameOverScreen(final EarthDefense game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0.255f,0.127f,0.127f,1);

        game.batch.begin();
        game.fontMedium.draw(game.batch,"You have failed your country",50,Gdx.graphics.getHeight()/2+200);
        game.fontMedium.draw(game.batch,"The Aliens have entered society",50,Gdx.graphics.getHeight()/2+170);
        game.fontMedium.draw(game.batch,"There freedom hating ideals are",50,Gdx.graphics.getHeight()/2+140);
        game.fontMedium.draw(game.batch,"now mainstream The world you knew",50,Gdx.graphics.getHeight()/2+110);
        game.fontMedium.draw(game.batch,"is now over your guns gone and bear",50,Gdx.graphics.getHeight()/2+80);
        game.fontMedium.draw(game.batch,"weak the world is truly over.",50,Gdx.graphics.getHeight()/2+50);
        game.fontMedium.draw(game.batch,"Press Escape to Escape",50,Gdx.graphics.getHeight()/2);
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            dispose();
            Gdx.app.exit();
        }

    }

    @Override
    public void resize(int i, int i1) {

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

    }
}
