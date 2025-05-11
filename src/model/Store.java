package model;

public class Store implements Location {
    private String name;
    private Inventory inventory;

    public Store(String name) {
        this.name = name;
        this.inventory = new Inventory();
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

}
