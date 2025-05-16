package model;

public class Store implements Location{
    private String name = "Store";
    private NPC npc;

    public Store(String name, NPC npc){
        this.name = name;
        this.npc = NPCRegistry.get(npcName);
    }

    @Override
    public void visit(Player player){

    }
    
    @Override
    public String getName() {
        return name;
    }
}
