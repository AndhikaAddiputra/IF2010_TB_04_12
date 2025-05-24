package model;

import java.util.HashMap;
import java.util.Map;

public class Store implements Location {
    private String name;
    private Inventory inventory;

    private static final Inventory storeInventory = new Inventory();
    private static final Map<String, Integer> buyPriceMap = new HashMap<>();

    static {
        // Initialize the store with some default items if needed
        // This could be done in a more complex way, such as loading from a file or database
        // seeds
        storeInventory.addItem(SeedsRegistry.getSeeds("Parsnip Seeds"), 999);
        buyPriceMap.put("Parsnip Seeds", 20);

        storeInventory.addItem(SeedsRegistry.getSeeds("Cauliflower Seeds"), 999);
        buyPriceMap.put("Cauliflower Seeds", 80);

        storeInventory.addItem(SeedsRegistry.getSeeds("Potato Seeds"), 999);
        buyPriceMap.put("Potato Seeds", 50);

        storeInventory.addItem(SeedsRegistry.getSeeds("Wheat Seeds"), 999);
        buyPriceMap.put("Wheat Seeds", 60);

        storeInventory.addItem(SeedsRegistry.getSeeds("Blueberry Seeds"), 999);
        buyPriceMap.put("Blueberry Seeds", 80);

        storeInventory.addItem(SeedsRegistry.getSeeds("Tomato Seeds"), 999);
        buyPriceMap.put("Tomato Seeds", 50);

        storeInventory.addItem(SeedsRegistry.getSeeds("Hot Pepper Seeds"), 999);
        buyPriceMap.put("Hot Pepper Seeds", 40);

        storeInventory.addItem(SeedsRegistry.getSeeds("Melon Seeds"), 999);
        buyPriceMap.put("Melon Seeds", 80);

        storeInventory.addItem(SeedsRegistry.getSeeds("Cranberry Seeds"), 999);
        buyPriceMap.put("Cranberry Seeds", 100);

        storeInventory.addItem(SeedsRegistry.getSeeds("Pumpkin Seeds"), 999);
        buyPriceMap.put("Pumpkin Seeds", 150);

        storeInventory.addItem(SeedsRegistry.getSeeds("Grape Seeds"), 999);
        buyPriceMap.put("Grape Seeds", 60);

        // equipment
        storeInventory.addItem(EquipmentRegistry.getEquipment("Hoe"), 100);
        buyPriceMap.put("Hoe", 250);
        
        storeInventory.addItem(EquipmentRegistry.getEquipment("Watering Can"), 100);
        buyPriceMap.put("Watering Can", 200); // Asumsi harga alat

        storeInventory.addItem(EquipmentRegistry.getEquipment("Pickaxe"), 100);
        buyPriceMap.put("Pickaxe", 400); // Asumsi harga alat

        storeInventory.addItem(EquipmentRegistry.getEquipment("Fishing Rod"), 100);
        buyPriceMap.put("Fishing Rod", 500); // Asumsi harga alat
        
        // foodss
        storeInventory.addItem(FoodRegistry.getFood("Fish n' Chips"), 100);
        buyPriceMap.put("Fish n' Chips", 150);

        storeInventory.addItem(FoodRegistry.getFood("Baguette"), 100);
        buyPriceMap.put("Baguette", 100);

        storeInventory.addItem(FoodRegistry.getFood("Sashimi"), 100);
        buyPriceMap.put("Sashimi", 300);

        storeInventory.addItem(FoodRegistry.getFood("Wine"), 100);
        buyPriceMap.put("Wine", 100);

        storeInventory.addItem(FoodRegistry.getFood("Pumpkin Pie"), 100);
        buyPriceMap.put("Pumpkin Pie", 120);

        storeInventory.addItem(FoodRegistry.getFood("Veggie Soup"), 100);
        buyPriceMap.put("Veggie Soup", 140);

        storeInventory.addItem(FoodRegistry.getFood("Fish Stew"), 100);
        buyPriceMap.put("Fish Stew", 280);

        storeInventory.addItem(FoodRegistry.getFood("Fish Sandwich"), 100);
        buyPriceMap.put("Fish Sandwich", 200);

        storeInventory.addItem(FoodRegistry.getFood("Cooked Pig's Head"), 100);
        buyPriceMap.put("Cooked Pig's Head", 1000);    

        // recipes
        storeInventory.addItem(RecipeRegistry.getRecipe("Fish n' Chips Recipe"), 100);
        buyPriceMap.put("Fish n' Chips Recipe", 250); 

        storeInventory.addItem(RecipeRegistry.getRecipe("Fish Sandwich Recipe"), 100);
        buyPriceMap.put("Fish Sandwich Recipe", 300); 
    }

    public Store(String name) {
        this.name = name;
        this.inventory = new Inventory();
        for (InventoryEntry entry : storeInventory.getItems().values()) {
        this.inventory.addItem(entry.getItem(), entry.getQuantity());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void visit(Player player) {
        // Logic for visiting the store
        System.out.println(player.getName() + " is visiting " + name);
        // Show inventory, allow buying/selling items, etc.
    }

    public Inventory getInventory() {
        return inventory;
    }

    public static int getBuyPrice(String itemName) {
        return buyPriceMap.getOrDefault(itemName, -1);
    }
}
