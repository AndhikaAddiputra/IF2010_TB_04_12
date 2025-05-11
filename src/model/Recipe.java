package model;

import java.util.Map;
import java.util.HashMap;

public class Recipe {
    private String recipeName;
    private HashMap<String, Integer> ingredients;
    private boolean unlockCondition;

    public Recipe(String recipeName, boolean unlockCondition) {
        this.recipeName = recipeName;
        this.ingredients = new HashMap<String, Integer>();
        this.unlockCondition = unlockCondition;
    }

    public Recipe() {
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean getUnlockCondition() {
        return unlockCondition;
    }

    public void setUnlockCondition(boolean unlockCondition) {
        this.unlockCondition = unlockCondition;
    }

    public void addIngredient(String itemName, int quantity) {
        if (ingredients.containsKey(itemName)) {
            ingredients.put(itemName, ingredients.get(itemName) + quantity);
        } else {
            ingredients.put(itemName, quantity);
        }
    }
    public void removeIngredient(String itemName, Integer quantity) {
        if (ingredients.containsKey(itemName)) {
            int currentQuantity = ingredients.get(itemName);
            if (currentQuantity > quantity) {
                ingredients.put(itemName, currentQuantity - quantity);
            } else {
                ingredients.remove(itemName);
            }
        }
    }
    public boolean hasIngredient(String itemName) {
        return ingredients.containsKey(itemName);
    }


}
