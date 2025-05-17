package controller;

import model.*;
import utility.*;

import java.util.List;
import java.util.Map;
import java.awt.Point;


public class CookingController {
    private final Player player;
    private final GameState gameState;
    private final FarmMap farmMap;

    public CookingController(Player player, GameState gameState, FarmMap farmMap) {
        this.player = player;
        this.gameState = gameState;
        this.farmMap = farmMap;
    }

    public void cook(String recipeName, String fuelName) {
        if (!isNearHouse(player.getPosition())) {
            System.out.println("You must be near the house to cook.");
            return;
        }

        Recipe recipe = RecipeRegistry.getRecipe(recipeName);
        if (recipe == null || !recipe.getUnlockCondition()) {
            System.out.println("Invalid or locked recipe.");
            return;
        }

        if (player.getEnergy() < 10) {
            System.out.println("Not enough energy to start cooking.");
            return;
        }

        int fuelCapacity = switch (fuelName.toLowerCase()) {
            case "firewood" -> 1;
            case "coal" -> 2;
            default -> 0;
        };

        if (fuelCapacity == 0 || !player.getInventory().hasItem(fuelName, 1)) {
            System.out.println("Invalid or missing fuel.");
            return;
        }

        // Cek bahan
        for (Map.Entry<IngredientRequirement, Integer> entry : recipe.getIngredients().entrySet()) {
            int found = 0;
            for (InventoryEntry invEntry : player.getInventory().getEntries()) {
                Item item = invEntry.getItem();
                if (entry.getKey().matches(item)) {
                    found += invEntry.getQuantity();
                }
            }
            if (found < entry.getValue()) {
                System.out.println("Missing ingredient: " + entry.getKey());
                return;
            }
        }

        // Kurangi bahan
        for (Map.Entry<IngredientRequirement, Integer> entry : recipe.getIngredients().entrySet()) {
            int quantityToRemove = entry.getValue();
            for (Item item : List.copyOf(player.getInventory().getAllItems())) {
                if (entry.getKey().matches(item)) {
                    int remove = Math.min(quantityToRemove, player.getInventory().getItemQuantity(item.getItemName()));
                    player.getInventory().removeItem(item.getItemName(), remove);
                    quantityToRemove -= remove;
                    if (quantityToRemove == 0) break;
                }
            }
        }

        player.getInventory().removeItem(fuelName, 1);
        player.reduceEnergy(10);
        gameState.advanceTime(60);

        Food cooked = recipe.getFood();
        player.getInventory().addItem(cooked);
        System.out.println("You cooked " + recipeName + " successfully!");
    }

    private boolean isNearHouse(Point pos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Point neighbor = new Point(pos.x + dx, pos.y + dy);
                if (farmMap.isValidPosition(neighbor)) {
                    Tile tile = farmMap.getTileAt(neighbor);
                    if (tile.getType() == TileType.HOUSE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
