package controller;

import model.*;
import utility.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FarmActionController {
    private Player player;
    private final FarmMap farmMap;
    private GameState gameState;

    public FarmActionController(Player player, FarmMap farmMap, GameState gameState) {
        this.player = player;
        this.farmMap = farmMap;
        this.gameState = gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private boolean canPerformAction(int energyCost) {
        if (player.getEnergy() - energyCost < -20) {
            System.out.println("You are too exhausted to perform this action.");
            return false;
        }
        return true;
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
        if (canPerformAction(5)){
            player.reduceEnergy(5);
            gameState.advanceTime(5);
        }
        tile.setType(TileType.TILLED);
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
        if (canPerformAction(5)){
            player.reduceEnergy(5);
            gameState.advanceTime(5);
        }
        tile.setType(TileType.PLANTED);
        tile.setSeed(seed);
        player.getInventory().removeItem(seedName, 1);
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
        if (canPerformAction(5)){
            player.reduceEnergy(5);
            gameState.advanceTime(5);
        }
        tile.startGrowth();
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
        if (canPerformAction(5 * units)){
            player.reduceEnergy(5 * units);
            gameState.advanceTime(5 * units);
        }
        tile.setType(TileType.TILLABLE);
        tile.clearSeed();
        System.out.println("You harvested: " + units + " unit(s) of " + crop.getItemName());
    }

    public void recoverLand(){
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.TILLED) {
            System.out.println("You must stand on tilled land (t) to recover.");
            return;
        }
        if (canPerformAction(5)){
            player.reduceEnergy(5);
            gameState.advanceTime(5);
        }
        tile.setType(TileType.TILLABLE);
        System.out.println("You recovered the land.");
    }

    public void eat(String itemName) {
        Item item = player.getInventory().getItem(itemName);
        if (item == null || !item.isEdible()) {
            System.out.println("This item is not edible or not found.");
            return;
        }
        int restore = item.getAddEnergy();
        player.addEnergy(restore);
        player.getInventory().removeItem(itemName, 1);
        gameState.advanceTime(5);
        System.out.println("You ate " + itemName + " and restored " + restore + " energy.");
        System.out.println("Current energy: " + player.getEnergy());
    }

    public void enterHouse() {
        Point pos = player.getPosition();
        boolean nearHouse = farmMap.isNearHouse(pos);
        if (!nearHouse) {
            System.out.println("You must be next to the house to enter.");
            return;
        }
        player.setInsideHouse(true);
        System.out.println("🏠 You entered the house.");
    }

    public void exitHouse() {
        player.setInsideHouse(false);
        System.out.println("🚪 You exited the house.");
    }

    public void watchWeather() {
        if (!player.isInsideHouse()) {
            System.out.println("📺 You need to be inside the house to check the weather.");
            return;
        }
        player.reduceEnergy(5);
        gameState.advanceTime(15);
        System.out.println("☁️ Today’s weather is: " + gameState.getWeather());
    }
    

    public void sleep(){
        int currentEnergy = player.getEnergy();

        if (!player.isInsideHouse()) {
            System.out.println("You must be inside the house to do this.");
            return;
        }
        else {
            if (currentEnergy >= 10) {
                player.setEnergy(100);
                System.out.println("You slept well in the house. Energy fully restored.");
            } else {
                player.setEnergy(50);
                System.out.println("You slept poorly due to low energy. Energy set to 50.");
            }
        }
        gameState.setTime(new Time(6, 0));
        gameState.advanceDay();
    }


    public void sell() {
        if (!farmMap.isNearShippingBin(player.getPosition())) {
            System.out.println("❌ You must be near the Shipping Bin to sell.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        Inventory inventory = player.getInventory();

        System.out.println("📦 What do you want to sell?");
        List<Item> items = new ArrayList<>(inventory.getAllItemsAsList());

        if (items.isEmpty()) {
            System.out.println("⚠️ You have nothing to sell.");
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getItemName();
            int qty = inventory.getItemQuantity(name);
            System.out.println((i + 1) + ". " + name + " x" + qty);
        }

        System.out.print("Select item number: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (Exception e) {
            System.out.println("❌ Invalid input.");
            return;
        }

        if (choice < 0 || choice >= items.size()) {
            System.out.println("❌ Invalid choice.");
            return;
        }

        Item selected = items.get(choice);
        int maxQty = inventory.getItemQuantity(selected.getItemName());

        System.out.print("Enter quantity to sell (1-" + maxQty + "): ");
        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("❌ Invalid quantity.");
            return;
        }

        if (qty < 1 || qty > maxQty) {
            System.out.println("❌ Invalid quantity range.");
            return;
        }

        int total = 0;

        if (selected instanceof Crop) {
            total = ((Crop) selected).getSellPrice() * qty;
        } else if (selected instanceof Fish) {
            total = ((Fish) selected).getPrice() * qty;
        } else if (selected instanceof Food) {
            total = ((Food) selected).getSellPrice() * qty;
        } else {
            System.out.println("⚠️ This item cannot be sold.");
            return;
        }

        System.out.println("🛒 Selling " + qty + " x " + selected.getItemName() + " for " + total + " gold...");

        // Proses jual: pause waktu, kurangi item, tambahkan gold
        inventory.removeItem(selected.getItemName(), qty);
        player.setGold(player.getGold() + total);

        gameState.advanceTime(15); // Tambahkan waktu 15 menit
        System.out.println("💰 You received " + total + " gold.");
    }

    // Debug method
    public void debugShowPlayerStatus() {
        System.out.println("📍 Player Position: " + player.getPosition());
        System.out.println("⚡ Energy: " + player.getEnergy());
        System.out.println("🕒 Time: " + gameState.getTime());
        System.out.println("Weather : " + gameState.getWeather());
        System.out.println("Season : " + gameState.getSeason());
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

