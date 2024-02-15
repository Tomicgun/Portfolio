package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final EarthDefense game;

    public MainMenuScreen(EarthDefense game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0.110f,0.216f,0.330f,1);


        game.batch.begin();
        game.font.draw(game.batch, "EARTH PROTECTOR",100,Gdx.graphics.getHeight()/2+100);
        game.font.draw(game.batch, "PRESS ENTER TO",120,Gdx.graphics.getHeight()/2+20);
        game.font.draw(game.batch, "PROTECT EARTH",120,Gdx.graphics.getHeight()/2-50);
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            game.setScreen(new StoryScreen(game));
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
