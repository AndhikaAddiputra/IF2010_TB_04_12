package model;

public class PondLocation implements FishingSpot {
    private String name = "Pond";
    @Override
    public String getName() {
        return "Farm Pond";
    }

    @Override
    public void visit(Player player) {
        System.out.println("Visiting the pond...");
    }
}
