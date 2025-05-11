package model;

public class Player extends Character {
    private Integer energy;
    private char gender;
    private Integer gold;
    private Inventory inventory;
    private NPC partner;
    private String farmName;
    private Item[] favoriteItems;

    public Player(String name, char gender, String farmName) {
        super(name);
        this.energy = 100;
        this.gender = gender;
        this.gold = 100; // Initialize gold
        this.inventory = new Inventory();
        this.partner = null; // No partner initially
        this.farmName = farmName;
        this.favoriteItems = new Item[100]; 
    }

    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public char getGender() {
        return gender;
    }
    public void setGender(char gender){
        this.gender = gender;
    }
    public int getGold() {
        return gold;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public NPC getPartner() {
        return partner;
    }
    public void setPartner(NPC partner) {
        this.partner = partner;
    }
    public String getFarmName() {
        return farmName;
    }
    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }
    public void addGold(int amount) {
        this.gold += amount;
    }
    public void subtractGold(int amount) {
        this.gold -= amount;
    }
    public void addEnergy(int amount) {
        this.energy += amount;
    }
    public void reduceEnergy(int amount) {
        this.energy -= amount;
    }

}
