package model;

public class ForestRiver implements FishingSpot,Location {
    private String name = "Forest River";
    public NPC npc;

    public ForestRiver(String name,NPC npc){
        this.name = name;
        this.npc = NPCRegistry.get(npcName);
    }

    @Override
    public void visit(Player player){
        System.out.println("Visiting " + name);
    }
    
    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public void fishing(Player player){
        
    }
}
