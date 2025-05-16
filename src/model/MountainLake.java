package model;

public class MountainLake implements FishingSpot,Location {
    private String name = "Mountain Lake";
    public NPC npc;

    public MountainLake(String name,NPC npc){
        this.name = name;
        this.npc = npc;
    }
    
    @Override
    public void visit(Player player){
        System.out.println("Visiting " + name);
    }
    
    @Override
    public String getName(){
        return name = name;
    }

    @Override
    public void fishing(Player player){
        
    }
}
