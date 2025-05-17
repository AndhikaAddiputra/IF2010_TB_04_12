package model;

import java.util.HashMap;
import java.util.Map;

public class Recipe extends Item {
    private String recipeName;
    private HashMap<IngredientRequirement, Integer> ingredients;
    private boolean unlockCondition;
    private Food food;

    public Recipe(String recipeName, boolean unlockCondition, Food food) {
        super(recipeName, false); // Memanggil konstruktor Item
        this.recipeName = recipeName;
        this.ingredients = new HashMap<>();
        this.unlockCondition = unlockCondition;
        this.food = food;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public Map<IngredientRequirement, Integer> getIngredients() {
        return ingredients;
    }

    public boolean getUnlockCondition() {
        return unlockCondition;
    }

    public void setUnlockCondition(boolean unlockCondition) {
        this.unlockCondition = unlockCondition;
    }

    public void addIngredient(IngredientRequirement requirement, int quantity) {
        ingredients.put(requirement, ingredients.getOrDefault(requirement, 0) + quantity);
    }

    public void removeIngredient(IngredientRequirement requirement, int quantity) {
        if (ingredients.containsKey(requirement)) {
            int currentQuantity = ingredients.get(requirement);
            if (currentQuantity > quantity) {
                ingredients.put(requirement, currentQuantity - quantity);
            } else {
                ingredients.remove(requirement);
            }
        }
    }

    public boolean hasIngredient(IngredientRequirement requirement) {
        return ingredients.containsKey(requirement);
    }

    public Food getFood() {
        return food;
    }
}  
