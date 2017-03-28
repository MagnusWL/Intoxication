package group04.datacontainers;

public class WaveSpawnerContainer  implements DataContainer{

    private int spawnTimer;
    private int spawnTimerMax;
    private int mobsSpawned;
    private int mobsSpawnedMax;
    private int spawnDuration;
    private boolean waveAlive = false;

    public boolean isWaveAlive() {
        return waveAlive;
    }

    public void setWaveAlive(boolean waveAlive) {
        this.waveAlive = waveAlive;
    }

    public int getSpawnTimer() {
        return spawnTimer;
    }

    public void setSpawnTimer(int spawnTimer) {
        this.spawnTimer = spawnTimer;
    }

    public int getSpawnTimerMax() {
        return spawnTimerMax;
    }

    public void setSpawnTimerMax(int spawnTimerMax) {
        this.spawnTimerMax = spawnTimerMax;
    }

    public int getMobsSpawned() {
        return mobsSpawned;
    }

    public void setMobsSpawned(int mobsSpawned) {
        this.mobsSpawned = mobsSpawned;
    }

    public int getMobsSpawnedMax() {
        return mobsSpawnedMax;
    }

    public void setMobsSpawnedMax(int mobsSpawnedMax) {
        this.mobsSpawnedMax = mobsSpawnedMax;
    }

    public int getSpawnDuration() {
        return spawnDuration;
    }

    public void setSpawnDuration(int spawnDuration) {
        this.spawnDuration = spawnDuration;
    }

}
