package model;

import java.util.HashMap;
import java.util.Map;

public class EquipmentRegistry {
    private static final Map<String, Equipment> equipmentMap = new HashMap<>();

    static {
        equipmentMap.put("Fishing Rod", new Equipment("Fishing Rod", "Fishing", 50, 25));
        equipmentMap.put("Pickaxe", new Equipment("Pickaxe", "Recover Land", 20, 10));
        equipmentMap.put("Hoe", new Equipment("Hoe", "Tilling", 50, 25));
        equipmentMap.put("Watering Can", new Equipment("Watering Can", "Watering", 80, 40));
    }

    public static Equipment getEquipment(String itemName) {
        return equipmentMap.get(itemName);
    }

    public static Map<String, Equipment> getEquipmentMap() {
        return equipmentMap;
    }
}
