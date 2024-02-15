package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;

public class GameScreen implements Screen {

    final EarthDefense game;
    //OrthographicCamera camera;
    Hillbilly player;
    Texture bullet;
    Texture hillbilly;
    Texture invader;
    Texture missile;
    Texture tree;
    Sound explosion;
    Sound playerHit;
    Sound alienShoot;
    Sound playerShoot;
    Sound treehit;
    Array<Invaider> aliens;
    Array<Rectangle> missiles;
    Array<Cover> cover;
    int invW = 10;
    int invH = 5;
    int invSpacingX = 50;
    int invSpacingY = 40;
    int minX;
    int minY;
    int maxX;
    int maxY;
    Vector2 offsett;
    int invdir = 1;
    int invSpeed = 30;
    int points = 0;
    int invPoints = 100;
    int numoflives = 4;

    public GameScreen(final EarthDefense game){
        this.game = game;

         aliens = new Array<>();
         missiles = new Array<>();
         offsett = new Vector2();

        //loading sprites
        bullet = new Texture(Gdx.files.internal("Shotgun-1.png"));
        hillbilly = new Texture(Gdx.files.internal("Hillbilly-1.png"));
        invader = new Texture(Gdx.files.internal("Alien-1.png"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("ExplosionSound.wav"));
        alienShoot = Gdx.audio.newSound(Gdx.files.internal("Lazer_shot.wav"));
        playerHit = Gdx.audio.newSound(Gdx.files.internal("PlayerHit.wav"));
        missile = new Texture("AlienProjectile.png");
        playerShoot = Gdx.audio.newSound(Gdx.files.internal("shotgunSound.wav"));
        tree = new Texture("tree.png");
        treehit = Gdx.audio.newSound(Gdx.files.internal("treehit.wav"));

        cover = new Array<>();
        player = new Hillbilly(hillbilly,bullet,playerShoot);
        for(int i=0;i<invH;i++){
            for(int j=0;j<invW;j++){
                Vector2 pos = new Vector2(j*invSpacingX,i*invSpacingY);
                pos.x += (float) Gdx.graphics.getWidth() /2;
                pos.y += Gdx.graphics.getHeight();
                pos.x -= ((float) invW /2) * invSpacingX;
                pos.y -= (invH) * invSpacingY;
                aliens.add(new Invaider(invader,missile,pos,alienShoot));
            }
        }

        int l = 90;
        for(int i=0;i<5;i++){
            cover.add(new Cover(tree));
            cover.get(i).Tree.setScale(4);
            cover.get(i).Tree.setPosition(l,70);
            l+=110;
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0,1);
        game.batch.begin();
        player.Draw(game.batch);
        game.fontsmall.draw(game.batch,"Points: " + points,10,470);
        game.fontsmall.draw(game.batch,"Number of Extra Lives left: "+ numoflives,300,470);
        for(Invaider alien: aliens){
            if(alien.isAlive){
                if (player.spriteBullet.getBoundingRectangle().overlaps(alien.sprite.getBoundingRectangle())) {
                    player.bulletPos.y = 1000;
                    alien.isAlive = false;
                    points+=invPoints;
                    explosion.play();
                    break;
                }
            }
        }
        minX = 10000;
        minY = 10000;
        maxX = 0;
        maxY = 0;
        for(int i=0;i<aliens.size;i++){
            if(aliens.get(i).isAlive){
                int indexX = i%invW;
                int indexY = i/invW;
                if(indexX>maxX) maxX = indexX;
                if(indexX<minX) minX = indexX;
                if(indexY>maxY) maxY = indexY;
                if(indexY<minY) minY = indexY;
            }
        }
        for(Invaider alien: aliens){
            alien.pos = new Vector2(alien.intial.x + offsett.x,alien.intial.y + offsett.y);
            if(alien.isAlive){
                alien.Draw(game.batch);
            }
        }

        offsett.x += invdir * Gdx.graphics.getDeltaTime() * invSpeed;
        if(aliens.get(maxX).pos.x + (aliens.get(maxX).sprite.getWidth() * aliens.get(maxX).sprite.getScaleX()) >= Gdx.graphics.getWidth()){
            offsett.y -=aliens.get(0).sprite.getHeight()*(aliens.get(0).sprite.getScaleY()*0.15f);
            invdir = -1;
        }
        if(minX<aliens.size-1){
            if(aliens.get(minX).pos.x - (aliens.get(minX).sprite.getWidth() * aliens.get(minX).sprite.getScaleX()) <= 0){
                invdir = 1;
                offsett.y -=aliens.get(0).sprite.getHeight()*(aliens.get(0).sprite.getScaleY()*0.15f);
                if(invSpeed<200){
                invSpeed+=15;
                }
            }
        }
        for(Cover cover: cover){
            if(cover.life>0){
                cover.Tree.draw(game.batch);
            }
        }
        if(minX<aliens.size){
            if(aliens.get(minY).pos.y<10){
                game.setScreen(new GameOverScreen(game));
                System.out.println("Exit by reaching bottom");
            }
        }
        if(numoflives<0){
            game.setScreen(new GameOverScreen(game));
            System.out.println("Player died");
        }
        for(Invaider alien:aliens){
            if(alien.bullet.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())){
                playerHit.play();
                numoflives--;
                alien.bulletPos.y = -100;
                alien.bulletPos.x = 100;
            }

            for(Cover cover: cover){
                if(alien.bullet.getBoundingRectangle().overlaps(cover.Tree.getBoundingRectangle())){
                    treehit.play();
                    cover.life--;
                    alien.bulletPos.y = -100;
                    alien.bulletPos.x = 100;
                }
            }

            for(Cover cover: cover){
                if(alien.sprite.getBoundingRectangle().overlaps(cover.Tree.getBoundingRectangle())){
                    treehit.play();
                    cover.Tree.setPosition(0,-200);
                    cover.life-=5;
                }
            }

            if (alien.sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())) {
                playerHit.play();
                game.setScreen(new GameOverScreen(game));
            }
        }
        if(points>=5000){
            game.setScreen(new GameWinScreen(game));
        }
        game.batch.end();
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
        alienShoot.dispose();
        playerHit.dispose();
        explosion.dispose();
        bullet.dispose();
        hillbilly.dispose();
        invader.dispose();
    }
}
