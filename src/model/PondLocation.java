package model;

public class PondLocation implements FishingSpot {
    private String name = "Pond";

    public PondLocation() {
        // Constructor logic if needed
    }
    @Override
    public String getName() {
        return "Farm Pond";
    }

    @Override
    public void visit(Player player) {
        System.out.println("Visiting the pond...");
    }
}
