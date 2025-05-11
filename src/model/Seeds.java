package model;

import java.util.Set;
import utility.Season;

public class Seeds extends Item {
    private Integer daysToHarvest;
    private Set<Season> season;
    private Integer buyPrice;

    public Seeds(String itemName, Integer daysToHarvest, Set<Season> season, Integer buyPrice) {
        super(itemName, false);
        this.daysToHarvest = daysToHarvest;
        this.season = season;
        this.buyPrice = buyPrice;
    }

    public Integer getDaysToHarvest() {
        return daysToHarvest;
    }

    public void setDaysToHarvest(Integer daysToHarvest) {
        this.daysToHarvest = daysToHarvest;
    }

    public Set<Season> getSeason() {
        return season;
    }
    public Integer getBuyPrice() {
        return buyPrice;
    } 
    public void setBuyPrice(Integer buyPrice) {
        this.buyPrice = buyPrice;
    }
    @Override
    public void useItem() {
        System.out.println("Using seeds: " + itemName);
        System.out.println("Days to harvest: " + daysToHarvest);
        System.out.println("Season: " + season);
        System.out.println("Buy price: " + buyPrice);
    }
    @Override
    public void displayItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Edible: " + (edible ? "Yes" : "No"));
        System.out.println("Days to harvest: " + daysToHarvest);
        System.out.println("Season: " + season);
        System.out.println("Buy price: " + buyPrice);
    }
    @Override
    public String toString() {
        return "Seeds{" +
                "itemName='" + itemName + '\'' +
                ", edible=" + edible +
                ", daysToHarvest=" + daysToHarvest +
                ", season=" + season +
                ", buyPrice=" + buyPrice +
                '}';
    }

}
