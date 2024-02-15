package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Cover {

    Sprite Tree;
    Texture tree;
    int life = 5;

    public Cover(Texture tree){
        Tree = new Sprite(tree);
    }
}
