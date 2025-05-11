package model;

public class Food extends Item {
    private Integer addEnergy;
    private Integer buyPrice;
    private Integer sellPrice;

    public Food(String itemName, Integer addEnergy, Integer buyPrice, Integer sellPrice) {
        super(itemName, true);
        this.addEnergy = addEnergy;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public Integer getAddEnergy() {
        return addEnergy;
    }

    public void setAddEnergy(Integer addEnergy) {
        this.addEnergy = addEnergy;
    }

    public Integer getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Integer buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    @Override
    public void useItem() {
        System.out.println("Using food: " + itemName);
        System.out.println("Add energy: " + addEnergy);
        System.out.println("Buy price: " + buyPrice);
        System.out.println("Sell price: " + sellPrice);
    }
    @Override
    public void displayItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Edible: " + (edible ? "Yes" : "No"));
        System.out.println("Add energy: " + addEnergy);
        System.out.println("Buy price: " + buyPrice);
        System.out.println("Sell price: " + sellPrice);
    }
}
