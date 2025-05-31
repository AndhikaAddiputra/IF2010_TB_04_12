package model;

public class MountainLake implements FishingSpot{
    private String name = "Mountain Lake";

    @Override
    public String getName() {
        return name;
    }

    
    @Override
    public void visit(Player player) {
        System.out.println("Visiting " + name);
    }
}
