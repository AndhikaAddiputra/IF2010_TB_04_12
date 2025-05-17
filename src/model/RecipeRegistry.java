package model;

import java.util.HashMap;
import java.util.Map;

public class RecipeRegistry {
    private static final Map<String, Recipe> recipeMap = new HashMap<>();

    static {
        // Default recipes
        Recipe baguette = new Recipe("Baguette Recipe", true, FoodRegistry.getFood("Baguette"));
        baguette.addIngredient(new IngredientRequirement("Crop", "Wheat"), 3);
        recipeMap.put("Baguette Recipe", baguette);

        Recipe wine = new Recipe("Wine Recipe", true, FoodRegistry.getFood("Wine"));
        wine.addIngredient(new IngredientRequirement("Crop", "Grape"), 2);
        recipeMap.put("Wine Recipe", wine);

        Recipe pumpkin_pie = new Recipe("Pumpkin Pie Recipe", true, FoodRegistry.getFood("Pumpkin Pie"));
        pumpkin_pie.addIngredient(new IngredientRequirement("Crop", "Pumpkin"), 1);
        pumpkin_pie.addIngredient(new IngredientRequirement("Crop", "Wheat"), 1);
        pumpkin_pie.addIngredient(new IngredientRequirement("Misc", "Egg"), 1);
        recipeMap.put("Pumpkin Pie Recipe", pumpkin_pie);

        Recipe spakbor_salad = new Recipe("Spakbor Salad Recipe", true, FoodRegistry.getFood("Spakbor Salad"));
        spakbor_salad.addIngredient(new IngredientRequirement("Crop", "Melon"), 1);
        spakbor_salad.addIngredient(new IngredientRequirement("Crop", "Cranberry"), 1);
        spakbor_salad.addIngredient(new IngredientRequirement("Crop", "Tomato"), 1);
        spakbor_salad.addIngredient(new IngredientRequirement("Crop", "Blueberry"), 1);
        recipeMap.put("Spakbor Salad Recipe", spakbor_salad);

        // In strore recipes
        Recipe fish_n_chips = new Recipe("Fish n' Chips Recipe", true, FoodRegistry.getFood("Fish n' Chips"));
        fish_n_chips.addIngredient(new IngredientRequirement("Fish", "Any"), 2);
        fish_n_chips.addIngredient(new IngredientRequirement("Crop", "Wheat"), 1);
        fish_n_chips.addIngredient(new IngredientRequirement("Crop", "Potato"), 1);
        recipeMap.put("Fish n' Chips Recipe", fish_n_chips);

        Recipe fish_sandwich = new Recipe("Fish Sandwich Recipe", true, FoodRegistry.getFood("Fish Sandwich"));
        fish_sandwich.addIngredient(new IngredientRequirement("Fish", "Any"), 1);
        fish_sandwich.addIngredient(new IngredientRequirement("Crop", "Wheat"), 2);
        fish_sandwich.addIngredient(new IngredientRequirement("Crop", "Tomato"), 1);
        fish_sandwich.addIngredient(new IngredientRequirement("Misc", "Hot Pepper"), 1);
        recipeMap.put("Fish Sandwich Recipe", fish_sandwich);

        // Syarat unlock : Memanen untuk pertama kali
        Recipe veggie_soup = new Recipe("Veggie Soup Recipe", false, FoodRegistry.getFood("Veggie Soup"));
        veggie_soup.addIngredient(new IngredientRequirement("Crop", "Potato"), 1);
        veggie_soup.addIngredient(new IngredientRequirement("Crop", "Cauliflower"), 1);
        veggie_soup.addIngredient(new IngredientRequirement("Crop", "Tomato"), 1);
        veggie_soup.addIngredient(new IngredientRequirement("Crop", "Parsnip"), 1);
        recipeMap.put("Veggie Soup Recipe", veggie_soup);

        // Syarat unlock : Memanen Hot pepper untuk pertama kali
        Recipe fish_stew = new Recipe("Fish Stew", false, FoodRegistry.getFood("Fish Stew"));
        fish_stew.addIngredient(new IngredientRequirement("Fish", "Any"), 2);
        fish_stew.addIngredient(new IngredientRequirement("Crop", "Hot Pepper"), 1);
        fish_stew.addIngredient(new IngredientRequirement("Crop", "Cauliflower"), 1);
        recipeMap.put("Fish Stew Recipe", fish_stew);

        // Syarat unlock : Mendapatkan fish tipe Legend pertama kali
        Recipe legend = new Recipe("The Legends of Spakbor", false, FoodRegistry.getFood("The Legends of Spakbor"));
        legend.addIngredient(new IngredientRequirement("Fish", "Legend"), 1);
        legend.addIngredient(new IngredientRequirement("Crop", "Potato"), 2);
        legend.addIngredient(new IngredientRequirement("Crop", "Parsnip"), 1);
        legend.addIngredient(new IngredientRequirement("Crop", "Tomato"), 1);
        legend.addIngredient(new IngredientRequirement("Misc", "Eggplant"), 1);
        recipeMap.put("The Legends of Spakbor Recipe", legend);
    }

    public static Recipe getRecipe(String recipeName) {
        return recipeMap.get(recipeName);
    }

    public static Map<String, Recipe> getAllRecipes() {
        return recipeMap;
    }
}
