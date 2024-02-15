package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameWinScreen implements Screen {

    final EarthDefense game;


    public GameWinScreen(final EarthDefense game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0.110f,0.216f,0.330f,1);

        game.batch.begin();
        game.fontMedium.draw(game.batch,"Congrats soldier you did it",50,Gdx.graphics.getHeight()/2+200);
        game.fontMedium.draw(game.batch,"The Aliens have been repelled",50,Gdx.graphics.getHeight()/2+170);
        game.fontMedium.draw(game.batch,"You are true american hero",50,Gdx.graphics.getHeight()/2+140);
        game.fontMedium.draw(game.batch,"Even if the government if after you",50,Gdx.graphics.getHeight()/2+110);
        game.fontMedium.draw(game.batch,"Our thoughts and prayers are with you",50,Gdx.graphics.getHeight()/2+80);
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
