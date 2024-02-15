package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hillbilly {
    public Vector2 pos;
    public Sprite sprite;
    public Sprite spriteBullet;
    public Vector2 bulletPos;
    int speed = 250;
    int speed_bullet = 700;
    Sound playerShoot;
    public Hillbilly(Texture image, Texture imgBullet,Sound playerShoot){
        sprite = new Sprite(image);
        spriteBullet = new Sprite(imgBullet);
        spriteBullet.setScale(2);
        sprite.scale(2);
        pos = new Vector2((float) Gdx.graphics.getWidth()/2,sprite.getScaleX()*sprite.getHeight()-2*sprite.getHeight());
        bulletPos = new Vector2(0,1000);
        this.playerShoot = playerShoot;

    }

    public void update(float delta){
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            pos.x -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            pos.x += speed * delta;
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && bulletPos.y >= Gdx.graphics.getHeight()){
            playerShoot.play();
            bulletPos.x = pos.x+2;
            bulletPos.y = pos.y;
        }

        if (pos.x-(sprite.getWidth() * sprite.getScaleX()) <= 0) pos.x = (sprite.getWidth() * sprite.getScaleX());
        if (pos.x+(sprite.getWidth() * sprite.getScaleX()) >= Gdx.graphics.getWidth()){
            pos.x = Gdx.graphics.getWidth() - (sprite.getWidth() * sprite.getScaleX());
        }
        bulletPos.y += delta * speed_bullet;

    }

    public void Draw(SpriteBatch batch){
        update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(pos.x, pos.y);
        sprite.draw(batch);
        spriteBullet.setPosition(bulletPos.x,bulletPos.y);
        spriteBullet.draw(batch);
    }
}
