package group04.common;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import group04.common.events.Event;

public class GameData {

    private float delta;
    private int displayWidth;
    private int displayHeight;
    private int mapWidth;
    private int tileSize;
    private int cameraX;
    private int cameraY;
    private int mouseX;
    private int mouseY;
    private final float gravityConstant = -1000f;
    private List<Event> events = new CopyOnWriteArrayList<>();

    public List<Event> getAllEvents() {
        return events;
    }
    
    public void removeEvent(Event e)
    {
        events.remove(e);
    }
    
    public void addEvent(Event e)
    {
        events.add(e);
    }

    public float getGravityConstant() {
        return gravityConstant;
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getCameraX() {
        return cameraX;
    }

    public void setCameraX(int cameraX) {
        this.cameraX = cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public void setCameraY(int cameraY) {
        this.cameraY = cameraY;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
    private int mapHeight;
    private final GameKeys keys = new GameKeys();

    public GameKeys getKeys() {
        return keys;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }
}
