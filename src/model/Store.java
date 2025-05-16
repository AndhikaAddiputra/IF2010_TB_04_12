package model;

public class Store implements Location{
    private String name;
    private NPC npc;

    public Store(String name, NPC npc){
        this.name = name;
        this.npc = NPCRegistry.get(npcName);
    }

    @Override
    public void visit(Player player){
        System.out.println(player.getName() + " is visiting " + name);
    }
    
    @Override
    public String getName() {
        return name;
    }
}
