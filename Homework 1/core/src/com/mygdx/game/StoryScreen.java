package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class StoryScreen implements Screen {
    final EarthDefense game;
    int time = 500000000;

    public StoryScreen(EarthDefense game){
        this.game = game;

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0.110f,0.216f,0.330f,1);
        game.batch.begin();
        game.fontMedium.draw(game.batch, "It has begun the aliens have arrive on earth",50, Gdx.graphics.getHeight()/2+200);
        game.fontMedium.draw(game.batch, "The Government says they have come in peace",50, Gdx.graphics.getHeight()/2+170);
        game.fontMedium.draw(game.batch, "That they are here to help us and raise us",50, Gdx.graphics.getHeight()/2+140);
        game.fontMedium.draw(game.batch, "up to the galactic level,but it's all lies.",50, Gdx.graphics.getHeight()/2+110);
        game.fontMediumish.draw(game.batch, "DO YOU TRUST THE GOVERNMENT?",40, Gdx.graphics.getHeight()/2+50);
        game.font.draw(game.batch, "HECK NO!!!!!",150, Gdx.graphics.getHeight()/2);
        game.fontMedium.draw(game.batch, "That's right now go get them tiger",50, Gdx.graphics.getHeight()/2-100);
        game.fontMedium.draw(game.batch, "Press Space to begin",50, 50);
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            game.setScreen(new GameScreen(game));
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
