package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NPCRegistry {
    private static final Map<String, NPC> npcMap = new HashMap<>();

    static {
                // Handle khusus untuk Mayor Tadi yang banyak mau
        Set<String> allItems = new HashSet<>();
        allItems.addAll(CropRegistry.getCropMap().keySet());
        allItems.addAll(FishRegistry.getAllFishNames());
        allItems.addAll(FoodRegistry.getFoodMap().keySet());
        allItems.addAll(MiscRegistry.getAll().keySet());
        
                // Define Mayor Tadi's loved and liked items
        Set<String> TadiLoved = Set.of("Legend");
        Set<String> TadiLiked = Set.of("Angler", "Crimsonfish", "Glacierfish");

        // Create Mayor Tadi's hated items (all items minus loved and liked)
        Set<String> TadiHated = new HashSet<>(allItems);
        TadiHated.removeAll(TadiLoved);
        TadiHated.removeAll(TadiLiked);

        npcMap.put("Mayor Tadi", new NPC(
            "Mayor Tadi", 
            TadiLoved,
            TadiLiked, 
            TadiHated));

        npcMap.put("Perry", new NPC(
            "Perry", 
            Set.of("Cranberry", "Blueberry"), 
            Set.of("Wine"),
            Set.of("Chub", "Carp", "Bullhead", "Sardine", "Largemouth Bass", "Rainbow Trout", "Sturgeon", "Midnight Carp", "Flounder", "Halibut", "Octopus", "Pufferfish", "Super Cucumber", "Catfish", "Salmon", "Angler", "Crimsonfish", "Glacierfish", "Legend")));
        npcMap.put("Caroline", new NPC(
            "Caroline",
            Set.of("Firewood", "Coal"),
            Set.of("Potato", "Wheat"),
            Set.of("Hot Pepper")));

        npcMap.put("Dasco", new NPC(
            "Dasco", 
            Set.of("The Legends of Spakbor", "Cooked Pig's Head", "Wine", "Fugu", "Spakbor Salad"), 
            Set.of("Fish Sandwich", "Fish Stew", "Baguette", "Fish n' Chips"), 
            Set.of("Legend", "Grape", "Cauliflower", "Wheat", "Pufferfish", "Salmon")));
        npcMap.put("Emily", new NPC(
            "Emily",
            Set.of("Parsnip Seeds", "Cauliflower Seeds", "Potato Seed", "Wheat Seeds", "Blueberry Seeds", "Tomato Seeds", "Hot Pepper Seeds", "Melon Seeds", "Corn Seeds", "Pumpkin Seeds","Grape Seeds"),
            Set.of("Catfish", "Salmon", "Sardine"),
            Set.of("Coal", "Wood")));
        npcMap.put("Abigail", new NPC(
            "Abigail",
            Set.of("Blueberry", "Melon", "Pumpkin", "Grape", "Cranberry"),
            Set.of("Baguette", "Pumpkin Pie", "Wine"),
            Set.of("Hot Pepper", "Cauliflower", "Parsnip", "Wheat")));
    }

    public static NPC get(String name) {
        return npcMap.get(name);
    }

    public static Collection<NPC> getAll() {
        return npcMap.values();
    }

    public static Set<String> getAllNames() {
        return npcMap.keySet();
    }
}
