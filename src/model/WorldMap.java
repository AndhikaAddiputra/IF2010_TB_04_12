package model;

import java.util.HashMap;
import java.util.Map;

public class WorldMap {
    private Map<String, Location> locations;

    public WorldMap(){
        locations = new HashMap<>();
        // NPC Houses
        locations.put("Store", new Store("Emily's Store"));
        locations.put("Mayor Tadi's House", new NPCBuilding("Mayor Tadi"));
        locations.put("Caroline's House", new NPCBuilding("Caroline"));
        locations.put("Perry's House", new NPCBuilding("Perry"));
        locations.put("Dasco's House", new NPCBuilding("Dasco"));
        locations.put("Abigail's House", new NPCBuilding("Abigail"));

        // Other World Locations
        locations.put("MountainLake", new MountainLake());
        locations.put("ForestRiver", new ForestRiver());
        locations.put("Ocean", new Ocean());
    }

    public Location getLocation(String name) {
        return locations.get(name);
    }

    public Map<String, Location> getLocations() {
        return locations;
    }
}
