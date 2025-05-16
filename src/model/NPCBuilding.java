package model;

public class NPCBuilding implements Location{
    String name;
    NPC npc;

    public NPCBuilding(String name, NPC npc){
        this.name = name;
        this.npc = npc;
    }

    @Override
    public void visit(Player player){

    }

    @Override
    public String getName(){
        return this.name;
    }
}
