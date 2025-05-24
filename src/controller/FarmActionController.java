package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.*;
import utility.*;

public class FarmActionController {
    private Player player;
    private final FarmMap farmMap;
    private GameState gameState;
    private MessageListener messageListener;
    private UserInputListener userInputListener;

    public FarmActionController(Player player, FarmMap farmMap, GameState gameState, MessageListener messageListener, UserInputListener userInputListener) {
        this.player = player;
        this.farmMap = farmMap;
        this.gameState = gameState;
        this.messageListener = messageListener;
        this.userInputListener = userInputListener;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private boolean canPerformAction(int energyCost) {
        if (player.getEnergy() - energyCost < -20) {
            notify("You are too exhausted to perform this action.");
            //System.out.println("You are too exhausted to perform this action.");
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
            notify("You can't move outside the map.");
            //System.out.println("You can't move outside the map.");
            return false;
        }
        Tile nextTile = farmMap.getTileAt(next);
        if (!nextTile.isWalkable()) {
            notify("You can't walk onto this tile.");
            //System.out.println("You can't walk onto this tile.");
            return false;
        }
        player.setPosition(next);
        notify("Moved to: " + next);
        //System.out.println("Moved to: " + next);
        return true;
    }

    public void till() {
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.TILLABLE) {
            notify("You must stand on tillable land (.) to till.");
            //System.out.println("You must stand on tillable land (.) to till.");
            return;
        }
        if (!player.getInventory().hasItem("Hoe")) {
            notify("You need a Hoe to till land.");
            //System.out.println("You need a Hoe to till land.");
            return;
        }
        if (canPerformAction(5)){
            player.reduceEnergy(5);
            gameState.advanceTime(5);
            tile.setType(TileType.TILLED);
            notify("You tilled the land.");
        }
        //System.out.println("You tilled the land.");
    }

    public void plant(String seedName) {
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.TILLED) {
            notify("You must stand on tilled land (t) to plant.");
            //System.out.println("You must stand on tilled land (t) to plant.");
            return;
        }
        if (!player.getInventory().hasItem(seedName)) {
            notify("You don't have " + seedName);
            //System.out.println("You don't have " + seedName);
            return;
        }
        Seeds seed = SeedsRegistry.getSeeds(seedName);
        if (seed == null) {
            notify("Invalid seed.");
            //System.out.println("Invalid seed.");
            return;
        }
        if (canPerformAction(5)){
            player.reduceEnergy(5);
            gameState.advanceTime(5);
            tile.setType(TileType.PLANTED);
            tile.setSeed(seed);
            player.getInventory().removeItem(seedName, 1);
            notify("You planted: " + seedName);
        }
        //System.out.println("You planted: " + seedName);
    }

    public void water() {
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.PLANTED) {
            notify("You must stand on planted land (l) to water.");
            //System.out.println("You must stand on planted land (l) to water.");
            return;
        }
        if (!player.getInventory().hasItem("Watering Can")) {
            notify("You need a Watering Can to water.");
            //System.out.println("You need a Watering Can to water.");
            return;
        }
        if (canPerformAction(5)){
            player.reduceEnergy(5);
            gameState.advanceTime(5);
            tile.startGrowth();
            notify("You watered the crop.");
        }
        //System.out.println("You watered the crop.");
    }

    public void harvest() {
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.PLANTED || !tile.isReadyToHarvest()) {
            notify("Nothing is ready to harvest here.");
            //System.out.println("Nothing is ready to harvest here.");
            return;
        }
        Seeds seed = tile.getSeed();
        Crop crop = CropRegistry.getCropFromSeedName(seed.getItemName());
        if (crop == null) {
            notify("No crop mapping found for seed: " + seed.getItemName());
            //System.out.println("No crop mapping found for seed: " + seed.getItemName());
            return;
        }
        Integer units = crop.getUnitPerHarvest();
        for (int i = 0; i < units; i++) {
            player.getInventory().addItem(crop);
        }
        if (canPerformAction(5 * units)){
            player.reduceEnergy(5 * units);
            gameState.advanceTime(5 * units);
            tile.setType(TileType.TILLABLE);
            tile.clearSeed();
            notify("You harvested: " + units + " unit(s) of " + crop.getItemName());
        }
        //System.out.println("You harvested: " + units + " unit(s) of " + crop.getItemName());
    }

    public void recoverLand(){
        Tile tile = farmMap.getTileAt(player.getPosition());
        if (tile.getType() != TileType.TILLED) {
            notify("You must stand on tilled land (t) to recover.");
            //System.out.println("You must stand on tilled land (t) to recover.");
            return;
        }
        if (canPerformAction(5)){
            player.reduceEnergy(5);
            gameState.advanceTime(5);
            tile.setType(TileType.TILLABLE);
            notify("You recovered the land.");
        }
        //System.out.println("You recovered the land.");
    }

    public void eat(String itemName) {
        Item item = player.getInventory().getItem(itemName);
        if (item == null || !item.isEdible()) {
            notify("This item is not edible or not found.");
            //System.out.println("This item is not edible or not found.");
            return;
        }
        int restore = item.getAddEnergy();
        player.addEnergy(restore);
        player.getInventory().removeItem(itemName, 1);
        gameState.advanceTime(5);
        notify("You ate " + itemName + " and restored " + restore + " energy.");
        //System.out.println("You ate " + itemName + " and restored " + restore + " energy.");
        notify("Current energy: " + player.getEnergy());
        //System.out.println("Current energy: " + player.getEnergy());
    }

    public void enterHouse() {
        Point pos = player.getPosition();
        boolean nearHouse = farmMap.isNearHouse(pos);
        if (!nearHouse) {
            notify("You must be next to the house to enter.");
            //System.out.println("You must be next to the house to enter.");
            return;
        }
        player.setInsideHouse(true);
        notify("🏠 You entered the house.");
        //System.out.println("🏠 You entered the house.");
    }

    public void exitHouse() {
        player.setInsideHouse(false);
        notify("🚪 You exited the house.");
        //System.out.println("🚪 You exited the house.");
    }

    public void watchWeather() {
        if (!player.isInsideHouse()) {
            notify("You need to be inside the house to check the weather.");
            //System.out.println("📺 You need to be inside the house to check the weather.");
            return;
        }
        player.reduceEnergy(5);
        gameState.advanceTime(15);
        notify("☁️ Today's weather is: " + gameState.getWeather());
        //System.out.println("☁️ Today’s weather is: " + gameState.getWeather());
    }
    

    public void sleep(){
        int currentEnergy = player.getEnergy();
        if (!player.isInsideHouse()) {
            notify("You must be inside the house to sleep.");
            //System.out.println("You must be inside the house to do this.");
            return;
        }
        else {
            if (currentEnergy >= 10) {
                player.setEnergy(100);
                notify("You slept well in the house. Energy fully restored.");
                gameState.setTime(new Time(6, 0));
                gameState.advanceDay();
                //System.out.println("You slept well in the house. Energy fully restored.");
            } else {
                player.setEnergy(50);
                notify("You slept poorly due to low energy. Energy set to 50.");
                gameState.setTime(new Time(6, 0));
                gameState.advanceDay();
                //System.out.println("You slept poorly due to low energy. Energy set to 50.");
            }
        }
    }


    public void sell() {
        if (!farmMap.isNearShippingBin(player.getPosition())) {
            notify("❌ You must be near the Shipping Bin to sell.");
            return;
        }
    
        Inventory inventory = player.getInventory();
        List<Item> items = new ArrayList<>(inventory.getAllItemsAsList());
    
        if (items.isEmpty()) {
            notify("⚠️ You have nothing to sell.");
            return;
        }
    
        notify("📦 What do you want to sell?");
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getItemName();
            int qty = inventory.getItemQuantity(name);
            notify((i + 1) + ". " + name + " x" + qty);
        }
    
        userInputListener.requestInput("Select item number (1–" + items.size() + "):", input1 -> {
            int choice;
            try {
                choice = Integer.parseInt(input1) - 1;
            } catch (Exception e) {
                notify("❌ Invalid input.");
                return;
            }
    
            if (choice < 0 || choice >= items.size()) {
                notify("❌ Invalid choice.");
                return;
            }
    
            Item selected = items.get(choice);
            int maxQty = inventory.getItemQuantity(selected.getItemName());
    
            userInputListener.requestInput("Enter quantity to sell (1–" + maxQty + "):", input2 -> {
                int qty;
                try {
                    qty = Integer.parseInt(input2);
                } catch (Exception e) {
                    notify("❌ Invalid quantity.");
                    return;
                }
    
                if (qty < 1 || qty > maxQty) {
                    notify("❌ Invalid quantity range.");
                    return;
                }
    
                int total = 0;
                if (selected instanceof Crop crop) {
                    total = crop.getSellPrice() * qty;
                } else if (selected instanceof Fish fish) {
                    total = fish.getPrice() * qty;
                } else if (selected instanceof Food food) {
                    total = food.getSellPrice() * qty;
                } else if (selected instanceof Equipment equip) {
                    total = equip.getSellPrice() * qty;
                } else {
                    notify("⚠️ This item cannot be sold.");
                    return;
                }
    
                notify("🛒 Selling " + qty + " x " + selected.getItemName() + " for " + total + " gold...");
                inventory.removeItem(selected.getItemName(), qty);
                player.setGold(player.getGold() + total);
                gameState.advanceTime(15);
                notify("💰 You received " + total + " gold.");
            });
        });
    }
    

    // helper message listener
    private void notify(String msg) {
        if (messageListener != null) {
            messageListener.onMessage(msg);
        } else {
            System.out.println(msg); // fallback CLI
        }
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

