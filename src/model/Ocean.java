package model;

public class Ocean implements FishingSpot,Location{
    private String name = "ocean";
    public NPC npc;

    public Ocean(String name,NPC npc){
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
        
    }
}
