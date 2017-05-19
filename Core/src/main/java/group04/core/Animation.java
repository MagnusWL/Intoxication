package group04.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;

public class Animation {
    private ArrayList<Sprite> sprites;
    private int width, height;
    private float animationSpeed;
    
    public Animation(ArrayList<Sprite> sprites, int width, int height, float animationSpeed)
    {
        this.animationSpeed = animationSpeed;
        this.sprites = sprites;
        this.width = width; 
        this.height = height;   
    }
    
    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }
    
    
}
