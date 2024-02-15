package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class EarthDefense extends Game {

    protected int ScreenSizeWidth = 800;
    protected int ScreenSizeLength = 480;

    SpriteBatch batch;
    BitmapFont font;
    BitmapFont fontsmall;
    BitmapFont fontMedium;
    BitmapFont fontMediumish;

    @Override
    public void create() {
        batch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("8bitOperatorPlus8-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);
        parameter.size = 18;
        fontsmall = generator.generateFont(parameter);
        parameter.size = 20;
        fontMedium = generator.generateFont(parameter);
        parameter.size = 32;
        fontMediumish = generator.generateFont(parameter);
        generator.dispose();
        this.setScreen(new MainMenuScreen(this));
    }

    public void render(){
        super.render();
    }

    public void dispose(){
        batch.dispose();
        font.dispose();
        fontMedium.dispose();
        fontsmall.dispose();
        fontMediumish.dispose();
    }
}
