package group04.datacontainers;

public class AnimationContainer {
    private String currentAnimation;
    private boolean animateable = false;
    private double currentFrame;    

    public String getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(String currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public boolean isAnimateable() {
        return animateable;
    }

    public void setAnimateable(boolean animateable) {
        this.animateable = animateable;
    }

    public double getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(double currentFrame) {
        this.currentFrame = currentFrame;
    }
}
