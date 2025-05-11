package model;

public class Equipment extends Item{
    private String whatAction;
    private Integer buyPrice;
    private Integer sellPrice;

    public Equipment(String itemName, String whatAction, Integer buyPrice, Integer sellPrice){
        super(itemName, false);
        this.whatAction = whatAction;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }
    public String getWhatAction() {
        return whatAction;
    }
    public void setWhatAction(String whatAction) {
        this.whatAction = whatAction;
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
        System.out.println("Using equipment: " + itemName);
        System.out.println("Action: " + whatAction);
        System.out.println("Buy price: " + buyPrice);
        System.out.println("Sell price: " + sellPrice);
    }
    @Override
    public void displayItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Edible: " + (edible ? "Yes" : "No"));
        System.out.println("Action: " + whatAction);
        System.out.println("Buy price: " + buyPrice);
        System.out.println("Sell price: " + sellPrice);
    }
}
