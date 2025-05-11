package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Inventory {
    private Map<String, InventoryEntry> items;

    public Inventory() {
        items = new HashMap<>();
    }

    public void addItem(Item item) {
        String name = item.getItemName();
        if (items.containsKey(name)) {
            items.get(name).addQuantity(1);
        } else {
            items.put(name, new InventoryEntry(item, 1));
        }
    }

    public void addItem(Item item, int quantity) {
        String name = item.getItemName();
        if (items.containsKey(name)) {
            items.get(name).addQuantity(quantity);
        } else {
            items.put(name, new InventoryEntry(item, quantity));
        }
    }

    public boolean removeItem(String itemName, int quantity) {
        if (!items.containsKey(itemName)) return false;

        InventoryEntry entry = items.get(itemName);
        if (entry.getQuantity() < quantity) return false;

        entry.subtractQuantity(quantity);
        if (entry.getQuantity() == 0) {
            items.remove(itemName);
        }
        return true;
    }

    public int getItemQuantity(String itemName) {
        return items.containsKey(itemName) ? items.get(itemName).getQuantity() : 0;
    }

    public boolean hasItem(String itemName) {
        return items.containsKey(itemName);
    }

    public Set<String> getAllItemNames() {
        return items.keySet();
    }

    public Map<String, InventoryEntry> getItems() {
        return items;
    }

    public void displayInventory() {
        System.out.println("===== Inventory =====");
        for (InventoryEntry entry : items.values()) {
            entry.getItem().displayItem();
            System.out.println("Quantity: " + entry.getQuantity());
            System.out.println("---------------------");
        }
    }
}
