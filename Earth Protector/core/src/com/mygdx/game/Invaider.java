package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Invaider {
    public Vector2 pos;
    public Sprite sprite;
    public Vector2 bulletPos;
    public Vector2 intial;
    Sprite bullet;
    Sound alienShoot;
    boolean isAlive = true;
    int bulletSpeed = 200;


    public Invaider(Texture invaiderImg,Texture bulletImg,Vector2 pos,Sound alienShoot){
        bulletPos = new Vector2(0,-100);
        sprite = new Sprite(invaiderImg);
        bullet = new Sprite(bulletImg);
        sprite.setScale(2);
        bullet.setScale(2);
        this.pos = pos;
        intial = this.pos;
        this.alienShoot = alienShoot;
    }

    public void shoot(){
        float x = MathUtils.random(0f,100f);
        if(x<0.04f && bulletPos.y < 0) {
            alienShoot.play();
            bulletPos.x = pos.x;
            bulletPos.y = pos.y;
        }
    }

    public void Draw(SpriteBatch batch){
        update(Gdx.graphics.getDeltaTime());
        sprite.setPosition(pos.x, pos.y);
        sprite.draw(batch);
        bullet.setPosition(bulletPos.x,bulletPos.y);
        bullet.draw(batch);
    }

    public void update(float delta){
        shoot();
        bulletPos.y -= delta * bulletSpeed;
    }
}
