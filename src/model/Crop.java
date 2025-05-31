package model;

public class Crop extends Item{
    private Integer buyPrice;
    private Integer sellPrice;
    private Integer unitPerHarvest;
    private String sourceSeedName;
    private final Integer addEnergy = 3;

    public Crop(String itemName, Integer buyPrice, Integer sellPrice, Integer unitPerHarvest, String sourceSeedName) {
        super(itemName, true);
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.unitPerHarvest = unitPerHarvest;
        this.sourceSeedName = sourceSeedName;
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

    public Integer getUnitPerHarvest() {
        return unitPerHarvest;
    }

    public void setUnitPerHarvest(Integer unitPerHarvest) {
        this.unitPerHarvest = unitPerHarvest;
    }

    public String getSourceSeedName() {
        return sourceSeedName;
    }

    public void setSourceSeedName(String sourceSeedName) {
        this.sourceSeedName = sourceSeedName;
    }

    @Override
    public Integer getAddEnergy() {
        return addEnergy;
    }

    @Override
    public void useItem() {
        System.out.println("Using corps: " + itemName);
        System.out.println("Buy price: " + buyPrice);
        System.out.println("Sell price: " + sellPrice);
        System.out.println("Unit per harvest: " + unitPerHarvest);
        System.out.println("Source seed name: " + sourceSeedName);
    }

    @Override
    public void displayItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Edible: " + (edible ? "Yes" : "No"));
        System.out.println("Buy price: " + buyPrice);
        System.out.println("Sell price: " + sellPrice);
        System.out.println("Unit per harvest: " + unitPerHarvest);
        System.out.println("Source seed name: " + sourceSeedName);
    }
}
