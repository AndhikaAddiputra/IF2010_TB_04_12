package model;

import java.util.HashMap;
import java.util.Map;

public class Player extends Character {
    private Integer energy;
    private char gender;
    private Integer gold;
    private Inventory inventory;
    private Map<NPC, String> partner;
    private String farmName;
    private Item[] favoriteItems;
    private boolean isInsideHouse;

    public Player(String name, char gender, String farmName) {
        super(name);
        this.energy = 100;
        this.gender = gender;
        this.gold = 100; // Initialize gold
        this.inventory = new Inventory();
        this.partner = new HashMap<>();
        this.farmName = farmName;
        this.favoriteItems = new Item[100]; 
        this.isInsideHouse = false; // Spawn di farmMap
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
    public Map<NPC, String> getPartner() {
        return partner;
    }
    public void setPartner(Map<NPC, String> partner) {
        this.partner = partner;
    }
    public void setPartner(NPC partner, String status) {
        this.partner.put(partner, status);
    }
    public String getPartnerStatus(NPC partner) {
        return this.partner.get(partner);
    }
    public void removePartner(NPC partner) {
        this.partner.remove(partner);
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
    public boolean isInsideHouse() {
        return isInsideHouse;
    }
    public void setInsideHouse(boolean insideHouse) {
        isInsideHouse = insideHouse;
    }

}
