package controller;

import model.*;
import utility.*;

import java.awt.Point;

public class FarmActionController {
    private final Player player;
    private final FarmMap farmMap;
    private final GameState gameState;

    public FarmActionController(Player player, FarmMap farmMap, GameState gameState) {
        this.player = player;
        this.farmMap = farmMap;
        this.gameState = gameState;
    }

    public boolean move(Direction direction) {
        Point current = player.getPosition();
        Point next = switch (direction) {
            case UP -> new Point(current.x, current.y - 1);
            case DOWN -> new Point(current.x, current.y + 1);
            case LEFT -> new Point(current.x - 1, current.y);
            case RIGHT -> new Point(current.x + 1, current.y);
        };

        if (!farmMap.isValidPosition(next)) {
            System.out.println("You can't move outside the map.");
            return false;
        }
        Tile nextTile = farmMap.getTileAt(next);
        if (!nextTile.isWalkable()) {
            System.out.println("You can't walk onto this tile.");
            return false;
        }
        player.setPosition(next);
        System.out.println("Moved to: " + next);
        return true;
    }

    public void till() {
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.TILLABLE) {
            System.out.println("You must stand on tillable land (.) to till.");
            return;
        }
        if (!player.getInventory().hasItem("Hoe")) {
            System.out.println("You need a Hoe to till land.");
            return;
        }
        tile.setType(TileType.TILLED);
        player.reduceEnergy(5);
        gameState.advanceTime(5);
        System.out.println("You tilled the land.");
    }

    public void plant(String seedName) {
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.TILLED) {
            System.out.println("You must stand on tilled land (t) to plant.");
            return;
        }
        if (!player.getInventory().hasItem(seedName)) {
            System.out.println("You don't have " + seedName);
            return;
        }
        Seeds seed = SeedsRegistry.getSeeds(seedName);
        if (seed == null) {
            System.out.println("Invalid seed.");
            return;
        }
        tile.setType(TileType.PLANTED);
        tile.setSeed(seed);
        player.getInventory().removeItem(seedName, 1);
        player.reduceEnergy(5);
        gameState.advanceTime(5);
        System.out.println("You planted: " + seedName);
    }

    public void water() {
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.PLANTED) {
            System.out.println("You must stand on planted land (l) to water.");
            return;
        }
        if (!player.getInventory().hasItem("Watering Can")) {
            System.out.println("You need a Watering Can to water.");
            return;
        }
        tile.startGrowth();
        player.reduceEnergy(5);
        gameState.advanceTime(5);
        System.out.println("You watered the crop.");
    }

    public void harvest() {
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.PLANTED || !tile.isReadyToHarvest()) {
            System.out.println("Nothing is ready to harvest here.");
            return;
        }
        Seeds seed = tile.getSeed();
        Crop crop = CropRegistry.getCropFromSeedName(seed.getItemName());
        if (crop == null) {
            System.out.println("No crop mapping found for seed: " + seed.getItemName());
            return;
        }
        Integer units = crop.getUnitPerHarvest();
        for (int i = 0; i < units; i++) {
            player.getInventory().addItem(crop);
        }
        tile.setType(TileType.TILLABLE);
        tile.clearSeed();
        player.reduceEnergy(5 * units);
        gameState.advanceTime(5 * units);
        System.out.println("You harvested: " + units + " unit(s) of " + crop.getItemName());
    }

    // Debug method
    public void debugShowPlayerStatus() {
        System.out.println("📍 Player Position: " + player.getPosition());
        System.out.println("⚡ Energy: " + player.getEnergy());
        System.out.println("🕒 Time: " + gameState.getTime());
    }

    public void debugShowTileInfo() {
        Tile tile = farmMap.getTileAt(player.getPosition());
        System.out.println("Tile Type: " + tile.getType());
        if (tile.getType() == TileType.PLANTED && tile.getSeed() != null) {
            System.out.println("Planted Seed: " + tile.getSeed().getItemName());
            System.out.println("Remaining Time to Harvest: " + tile.getRemainingHarvestTime());
        }
    }
}

