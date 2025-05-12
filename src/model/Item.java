package model;

public class Item {
    protected String itemName;
    protected boolean edible;

    public Item(String itemName, boolean edible) {
        this.itemName = itemName;
        this.edible = edible;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isEdible() {
        return edible;
    }
    
    public void setEdible(boolean edible) {
        this.edible = edible;
    }

    public void useItem() {
        System.out.println("Using item: " + itemName);
    }

    public void displayItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Edible: " + (edible ? "Yes" : "No"));
    }

    public Integer getAddEnergy() {
        return 0;
    }
}
