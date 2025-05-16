package model;

public class MountainLake implements FishingSpot,Location {
    private String name = "Mountain Lake";
    public NPC npc;

    @Override
    public void visit(Player player){
        System.out.println("Visiting " + name);
    }
    
    @Override
    public String getName(){
        this.name = name;
    }

    @Override
    public void fishing(Player player){
        
    }
}
