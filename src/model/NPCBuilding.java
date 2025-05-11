package model;

public class NPCBuilding implements Location {
    private NPC npc;
    private String name;

    public NPCBuilding(String npcName) {
        this.npc = NPCRegistry.get(npcName);
        this.name = npcName + "'s House";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void visit(Player player) {
        System.out.println("You visit " + npc.getName() + " at home.");
    }
}
