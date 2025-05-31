package model;

public class ForestRiver implements FishingSpot{
    private String name = "Forest River";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void visit(Player player) {
        System.out.println("Visiting " + name);
    }
}
