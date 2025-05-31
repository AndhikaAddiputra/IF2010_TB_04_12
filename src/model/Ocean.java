package model;

public class Ocean implements FishingSpot{
    private String name = "Ocean";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void visit(Player player) {
        System.out.println("Visiting " + name);
    }
}
