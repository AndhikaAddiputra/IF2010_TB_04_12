public class Store implements Location{
    String name;
    NPC npc;

    public Store(String name, NPC npc){
        this.name = name;
        this.npc = npc;
    }

    @Override
    public void visit(Player player){

    }
    
    @Override
    public String getName() {
        return name;
    }
}