package model;

import java.util.HashMap;
import java.util.Map;

public class CropRegistry {
    private static final Map<String, Crop> cropMap = new HashMap<>();

    static {
        cropMap.put("Parsnip", new Crop("Parsnip", 50, 35, 1, "Parsnip Seeds"));
        cropMap.put("Cauliflower", new Crop("Cauliflower", 200, 150, 1, "Cauliflower Seeds"));
        cropMap.put("Potato", new Crop("Potato", 0, 80, 1, "Potato Seeds"));
        cropMap.put("Wheat", new Crop("Wheat", 50, 30, 3, "Wheat Seeds"));
        cropMap.put("Blueberry", new Crop("Blueberry", 150, 40, 3, "Blueberry Seeds"));
        cropMap.put("Tomato", new Crop("Tomato", 90, 60, 1, "Tomato Seeds"));
        cropMap.put("Hot Pepper", new Crop("Hot Pepper", 0, 40, 1, "Hot Pepper Seeds"));
        cropMap.put("Melon", new Crop("Melon", 0, 250, 1, "Melon Seeds"));
        cropMap.put("Cranberry", new Crop("Cranberry", 0, 25, 10, "Cranberry Seeds"));
        cropMap.put("Pumpkin", new Crop("Pumpkin", 300, 250, 1, "Pumpkin Seeds"));
        cropMap.put("Grape", new Crop("Grape", 100, 10, 20, "Grape Seeds"));
    }
    public static Crop getCrop(String itemName) {
        return cropMap.get(itemName);
    }
    public static Map<String, Crop> getCropMap() {
        return cropMap;
    }

    public static Crop getCropFromSeedName(String seedName) {
        for (Crop crop : cropMap.values()) {
            if (crop.getSourceSeedName().equals(seedName)) {
                return crop;
            }
        }
        return null;
    }
}
