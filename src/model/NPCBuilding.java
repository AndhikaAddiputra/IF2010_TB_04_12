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
        System.out.println("You visit " + npc.getName() + " at home.");
    }

    @Override
    public String getName(){
        return this.name;
    }
}
