package model;

import utility.TileType;

public class Tile {
    private TileType type;
    private boolean walkable;
    private Seeds seed;
    private int remainingHarvestMinutes = -1; // -1 = belum disiram

    public Tile(TileType type, boolean walkable) {
        this.type = type;
        this.walkable = walkable;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public void setSeed(Seeds seed) {
        this.seed = seed;
    }

    public Seeds getSeed() {
        return seed;
    }

    public void startGrowth() {
        if (seed != null && remainingHarvestMinutes == -1) {
            remainingHarvestMinutes = seed.getDaysToHarvest() * 24 * 60;
        }
    }

    public void reduceHarvestTime(int minutes) {
        if (remainingHarvestMinutes > 0) {
            remainingHarvestMinutes = Math.max(0, remainingHarvestMinutes - minutes);
        }
    }

    public boolean isReadyToHarvest() {
        return remainingHarvestMinutes == 0;
    }

    public int getRemainingHarvestTime() {
        return remainingHarvestMinutes;
    }

    public void clearSeed() {
        this.seed = null;
        this.remainingHarvestMinutes = -1;
    }

}