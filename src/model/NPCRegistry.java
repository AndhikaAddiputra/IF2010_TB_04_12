package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NPCRegistry {
    private static final Map<String, NPC> npcMap = new HashMap<>();

    static {
        npcMap.put("Mayor Tadi", new NPC(
            "Mayor Tadi", 
            Set.of("Legend"), 
            Set.of("Angler", "Crimsonfish", "Glacierfish"), 
            Set.of("Trash")));
        npcMap.put("Caroline", new NPC(
            "Caroline",
            Set.of("Firewood", "Coal"),
            Set.of("Potato", "Wheat"),
            Set.of("Hot Pepper")));

        // Handle khusus untuk Perry yang banyak mau
        Set<String> allItems = new HashSet<>();
        allItems.addAll(CropRegistry.getCropMap().keySet());
        allItems.addAll(FishRegistry.getAllFishNames());
        allItems.addAll(FoodRegistry.getFoodMap().keySet());
        allItems.addAll(MiscRegistry.getAll().keySet());

        // Define Perry's loved and liked items
        Set<String> perryLoved = Set.of("Cranberry", "Blueberry");
        Set<String> perryLiked = Set.of("Wine");

        // Create Perry's hated items (all items minus loved and liked)
        Set<String> perryHated = new HashSet<>(allItems);
        perryHated.removeAll(perryLoved);
        perryHated.removeAll(perryLiked);

        npcMap.put("Perry", new NPC(
            "Perry", 
            perryLoved,
            perryLiked, 
            perryHated));

        npcMap.put("Dasco", new NPC(
            "Dasco", 
            Set.of("The Legends of Spakbor", "Cooked Pig's Head", "Wine", "Fugu", "Spakbor Salad"), 
            Set.of("Fish Sandwich", "Fish Stew", "Baguette", "Fish n' Chips"), 
            Set.of("Legend", "Grape", "Cauliflower", "Wheat", "Pufferfish", "Salmon")));
        npcMap.put("Emily", new NPC(
            "Emily",
            null,
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
