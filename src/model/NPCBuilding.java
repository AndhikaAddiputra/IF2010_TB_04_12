package model;

public class NPCBuilding implements Location{
    private String name;
    private NPC npc;

    public NPCBuilding(String name, NPC npc){
        this.name = npcName + "'s House";
        this.npc = NPCRegistry.get(npcName);;
    }

    @Override
    public void visit(Player player){

    }

    @Override
    public String getName(){
        return this.name;
    }
}
