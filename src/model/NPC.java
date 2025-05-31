package model;

import java.util.Set;
import utility.RelationshipStatus;

public class NPC extends Character {
    private Integer heartPoints;
    private RelationshipStatus status;
    private Set<String> lovedItems;
    private Set<String> likedItems;
    private Set<String> hateItems;

    public NPC(String name, Set<String> lovedItems, Set<String> likedItems, Set<String> hateItems) {
        super(name);
        this.heartPoints = 0;
        this.status = RelationshipStatus.SINGLE;
        this.lovedItems = lovedItems;
        this.likedItems = likedItems;
        this.hateItems = hateItems;
    }

    public int getHeartPoints() {
        return heartPoints;
    }
    public void setHeartPoints(int heartPoints) {
        this.heartPoints = heartPoints;
    }
    public RelationshipStatus getStatus() {
        return status;
    }
    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }

    public Set<String> getLovedItems() {
        return lovedItems;
    }
    public void setLovedItems(Set<String> lovedItems) {
        this.lovedItems = lovedItems;
    }
    public void addLovedItem(String item) {
        this.lovedItems.add(item);
    }

    public Set<String> getLikedItems() {
        return likedItems;
    }
    public void setLikedItems(Set<String> likedItems) {
        this.likedItems = likedItems;
    }
    public void addLikedItem(String item) {
        this.likedItems.add(item);
    }

    public Set<String> getHateItems() {
        return hateItems;
    }
    public void setHateItems(Set<String> hateItems) {
        this.hateItems = hateItems;
    }
    public void addHateItem(String item) {
        this.hateItems.add(item);
    }
}
