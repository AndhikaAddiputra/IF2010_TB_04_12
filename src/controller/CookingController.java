package controller;

import java.util.List;
import java.util.Map;
import model.*;
import utility.*;


public class CookingController {
    private final Player player;
    private final GameState gameState;
    private final FarmMap farmMap;
    private final MessageListener messageListener;
    private final UserInputListener inputListener;

    public CookingController(Player player, GameState gameState, FarmMap farmMap,
                         MessageListener messageListener, UserInputListener inputListener) {
        this.player = player;
        this.gameState = gameState;
        this.farmMap = farmMap;
        this.messageListener = messageListener;
        this.inputListener = inputListener;
    }

    private boolean canPerformAction(int energyCost) {
        if (player.getEnergy() - energyCost < -20) {
            notify("You are too exhausted to perform this action.");
            //System.out.println("You are too exhausted to perform this action.");
            return false;
        }
        return true;
    }

    public void cook(String recipeName, String fuelName) {
        if (!farmMap.isNearHouse(player.getPosition())) {
            notify("❌ You must be near the house to cook.");
            return;
        }
        if (!canPerformAction(10)) {
            notify("❌ Not enough energy to cook. You need at least 10 energy.");
            return;
        }
    
        Recipe recipe = RecipeRegistry.getRecipe(recipeName);
        if (recipe == null || !recipe.getUnlockCondition()) {
            notify("❌ Invalid or locked recipe.");
            return;
        }
    
        boolean warnedAboutEnergy = false;
        boolean warnedAboutFuel = false;
        boolean warnedAboutIngredients = false;
    
        // Show warning for low energy but continue
        if (player.getEnergy() < 10) {
            notify("⚠️ Warning: Low energy to cook.");
            warnedAboutEnergy = true;
        }
    
        // Check fuel but only warn if invalid
        int fuelCapacity = switch (fuelName.toLowerCase()) {
            case "firewood" -> 1;
            case "coal" -> 2;
            default -> 0;
        };
    
        if (fuelCapacity == 0 || !player.getInventory().hasItem(fuelName, 1)) {
            notify("⚠️ Warning: Invalid or missing fuel.");
            warnedAboutFuel = true;
        }
    
        // Check ingredients
        for (Map.Entry<IngredientRequirement, Integer> entry : recipe.getIngredients().entrySet()) {
            int found = 0;
            for (InventoryEntry invEntry : player.getInventory().getEntries()) {
                if (entry.getKey().matches(invEntry.getItem())) {
                    found += invEntry.getQuantity();
                }
            }
            if (found < entry.getValue()) {
                notify("⚠️ Warning: Missing ingredient: " + entry.getKey());
                warnedAboutIngredients = true;
            }
        }
    
        // Continue with cooking despite warnings
        if (warnedAboutEnergy || warnedAboutFuel || warnedAboutIngredients) {
            notify("❌ Cooking failed due to missing fuel or ingredients.");
            return;
        }
    
        // Process cooking
        player.reduceEnergy(10);
        gameState.advanceTime(5);
        player.setCurrentCooking(new CookingTask(recipe));

        notify("👩‍🍳 Cooking started: " + recipeName + ". It will be ready in 60 minutes.");
    
        if (!warnedAboutIngredients) {
            // Remove ingredients only if we have them all
            for (Map.Entry<IngredientRequirement, Integer> entry : recipe.getIngredients().entrySet()) {
                int qtyToRemove = entry.getValue();
                for (Item item : List.copyOf(player.getInventory().getAllItems())) {
                    if (entry.getKey().matches(item)) {
                        int removed = Math.min(qtyToRemove, player.getInventory().getItemQuantity(item.getItemName()));
                        player.getInventory().removeItem(item.getItemName(), removed);
                        qtyToRemove -= removed;
                        if (qtyToRemove == 0) break;
                    }
                }
            }
        }
    
        if (!warnedAboutFuel) {
            player.getInventory().removeItem(fuelName, 1);
        }
    
        /*Food cooked = recipe.getFood();
        player.getInventory().addItem(cooked);
        notify("✅ You cooked " + cooked.getItemName() + " successfully!");*/
    }

    public void startCookingFlow() {
        if (!farmMap.isNearHouse(player.getPosition())) {
            notify("You must be near the house to cook.");
            return;
        }
    
        notify("Available Recipes:");
        // Fix: Get recipes from RecipeRegistry instead of inventory
        List<Recipe> available = RecipeRegistry.getAll().stream()
            .filter(Recipe::getUnlockCondition)
            .filter(recipe -> player.getInventory().hasItem(recipe.getRecipeName()))
            .toList();
    
        if (available.isEmpty()) {
            notify("❌ You don't have any recipes.");
            return;
        }
    
        // Rest of the method remains the same
        for (int i = 0; i < available.size(); i++) {
            notify((i + 1) + ". " + available.get(i).getRecipeName());
        }
        
        requestInput("Enter recipe number:", input1 -> {
            int choice;
            try {
                choice = Integer.parseInt(input1.trim()) - 1;
            } catch (NumberFormatException e) {
                notify("❌ Invalid number.");
                return;
            }
    
            if (choice < 0 || choice >= available.size()) {
                notify("❌ Invalid recipe selection.");
                return;
            }
    
            Recipe selected = available.get(choice);
            requestInput("Enter fuel (Firewood / Coal):", fuelInput -> {
                cook(selected.getRecipeName(), fuelInput.trim());
            });
        });
    }

    private void notify(String msg) {
        if (messageListener != null) {
            messageListener.onMessage(msg);
        } else {
            System.out.println(msg);
        }
    }
    
    private void requestInput(String prompt, java.util.function.Consumer<String> callback) {
        if (inputListener != null) {
            inputListener.requestInput(prompt, callback);
        } else {
            System.out.print(prompt + " ");
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String input = scanner.nextLine();
            callback.accept(input);
        }
    }
    
}
