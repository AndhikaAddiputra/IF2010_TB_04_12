public class ForestRiver implements FishingSpot,Location {
    String name;
    NPC npc;

    public ForestRiver(String name,NPC npc){
        this.name = name;
        this.npc = npc;
    }

    @Override
    public void visit(Player player){

    }
    
    @Override
    public String getName(){
        this.name = name;
    }

    @Override
    public void fishing(Player player){
        
    }
}