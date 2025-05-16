package model;

public class ForestRiver implements FishingSpot,Location {
    private String name = "Forest River";
    public NPC npc;

    public ForestRiver(String name,NPC npc){
        this.name = name;
        this.npc = npc;
    }

    @Override
    public void visit(Player player){

    }
    
    @Override
    public String getName(){
        return name = name;
    }

    @Override
    public void fishing(Player player){
        System.out.println("Visiting " + name);
    }
}
