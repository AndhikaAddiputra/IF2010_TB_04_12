package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import utility.Season;

public class SeedsRegistry {
    private static final Map<String, Seeds> seedsMap = new HashMap<>();

    static {
        seedsMap.put("Parsnip Seeds", new Seeds("Parsnip Seeds", 1, Set.of(Season.SPRING), 20));
        seedsMap.put("Cauliflower Seeds", new Seeds("Cauliflower Seeds", 5, Set.of(Season.SPRING), 80));
        seedsMap.put("Potato Seeds", new Seeds("Potato Seeds", 3, Set.of(Season.SPRING), 50));
        seedsMap.put("Wheat Seeds", new Seeds("Wheat Seeds", 1, Set.of(Season.SPRING, Season.FALL), 60));
        seedsMap.put("Blueberry Seeds", new Seeds("Blueberry Seeds", 7, Set.of(Season.SUMMER), 80));
        seedsMap.put("Tomato Seeds", new Seeds("Tomato Seeds", 3,Set.of(Season.SUMMER), 50));
        seedsMap.put("Hot Pepper Seeds", new Seeds("Hot Pepper Seeds", 1, Set.of(Season.SUMMER), 40));
        seedsMap.put("Melon Seeds", new Seeds("Melon Seeds", 4, Set.of(Season.SUMMER), 80));
        seedsMap.put("Cranberry Seeds", new Seeds("Corn Seeds", 2, Set.of(Season.FALL), 100));
        seedsMap.put("Pumpkin Seeds", new Seeds("Pumpkin Seeds", 7, Set.of(Season.FALL), 150));
        seedsMap.put("Grape Seeds", new Seeds("Grape Seeds", 7, Set.of(Season.FALL), 60));
    }  
    public static Seeds getSeeds(String itemName) {
        return seedsMap.get(itemName);
    }
    public static Map<String, Seeds> getSeedsMap() {
        return seedsMap;
    }
}
