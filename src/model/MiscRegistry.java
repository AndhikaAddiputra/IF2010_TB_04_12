package model;

import java.util.HashMap;
import java.util.Map;

public class MiscRegistry {
    private static final Map<String, MiscItem> miscMap = new HashMap<>();

    static{
        miscMap.put("Firewood", new MiscItem("Firewood", "Firewood bisa digunakan untuk memasak 1 makanan"));
        miscMap.put("Coal", new MiscItem("Coal", "Coal bisa digunakan untuk memasak 2 makanan"));
        miscMap.put("Proposal Ring", new MiscItem("Proposal Ring", "Proposal Ring bisa digunakan untuk melamar dan menikahi NPC"));
        miscMap.put("Egg", new MiscItem("Egg", "Food item yang tidak terdaftar pada Food, hanya untuk bahan memasak"));
        miscMap.put("Eggplant", new MiscItem("Eggplant", "Food item yang tidak terdaftar pada Food, hanya untuk bahan memasak"));
    }

    public static MiscItem getMisc(String name) {
        return miscMap.get(name);
    }

    public static Map<String, MiscItem> getAll() {
        return miscMap;
    }
}
