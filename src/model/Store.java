package model;

public class Store implements Location {
    private String name;
    private Inventory inventory;

    private static final Inventory storeInventory = new Inventory();

    static {
        // Initialize the store with some default items if needed
        // This could be done in a more complex way, such as loading from a file or database
        storeInventory.addItem(SeedRegistry.getSeeds("Parsnip Seeds"), 999);
    }

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
