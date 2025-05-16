package model;

public class Ocean implements FishingSpot,Location{
    private String name = "ocean";
    private NPC npc;

    public Ocean(String name,NPC npc){
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
