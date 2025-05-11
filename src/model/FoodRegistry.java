package model;

import java.util.HashMap;
import java.util.Map;

public class FoodRegistry {
    private static final Map<String, Food> foodMap = new HashMap<>();

    static {
        foodMap.put("Fish n' Chips", new Food("Fish n' Chips", 50, 150, 135));
        foodMap.put("Baguette", new Food("Baguette", 25, 100, 80));
        foodMap.put("Sashimi", new Food("Sashimi", 70, 300, 275));
        foodMap.put("Fugu", new Food("Fugu", 50, 0, 180));
        foodMap.put("Wine", new Food("Wine", 20, 100, 90));
        foodMap.put("Pumpkin Pie", new Food("Pumpkin Pie", 35, 120, 100));
        foodMap.put("Veggie Soup", new Food("Veggie Soup", 40, 140, 120));
        foodMap.put("Fish Stew", new Food("Fish Stew", 70, 280, 260));
        foodMap.put("Spakbor Salad", new Food("Spakbor Salad", 70, 0, 250));
        foodMap.put("Fish Sandwich", new Food("Fish Sandwich", 50, 200, 180));
        foodMap.put("The Legends of Spakbor", new Food("The Legends of Spakbor", 100, 0, 2000));
        foodMap.put("Cookde Pig's Head", new Food("Cooked Pig's Head", 100, 1000, 0));
    }

    public static Food getFood(String itemName) {
        return foodMap.get(itemName);
    }

    public static Map<String, Food> getFoodMap() {
        return foodMap;
    }
}
