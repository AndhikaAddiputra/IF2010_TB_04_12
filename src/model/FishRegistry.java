package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import utility.Season;
import utility.Weather;

public class FishRegistry {
    private static final Map<String, Fish> fishMap = new HashMap<>();

    static{
        Range<Time> everytime = new Range<>(new Time(0, 0), new Time(23, 59));
        Range<Time> normalDay = new Range<>(new Time(6, 0), new Time(18, 0));
        Range<Time> extraDay = new Range<>(new Time(6,0), new Time(22, 0));
        Range<Time> legendaryTime = new Range<>(new Time(8, 0), new Time(20, 0));

        // Common Fish
        fishMap.put("Bullhead", new Fish(
            "Bullhead",
            Set.of(Season.SPRING, Season.SUMMER, Season.FALL,Season.WINTER), 
            Set.of(everytime),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new MountainLake()),
            "Common Fish", 40));

        fishMap.put("Carp", new Fish(
            "Carp",
            Set.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 
            Set.of(everytime),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new MountainLake(), new PondLocation()),
            "Common Fish", 20));

        fishMap.put("Chub", new Fish(
            "Chub",
            Set.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 
            Set.of(everytime),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new MountainLake(), new ForestRiver()),
            "Common Fish", 20));

        // Reguler Fish
        fishMap.put("Largemouth Bass", new Fish(
            "Largemouth Bass",
            Set.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 
            Set.of(normalDay),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new MountainLake()),
            "Reguler Fish", 40));

        fishMap.put("Rainbow Trout", new Fish(
            "Rainbow Trout",
            Set.of(Season.SUMMER), 
            Set.of(normalDay),
            Set.of(Weather.SUNNY), 
            Set.of(new MountainLake(), new ForestRiver()),
            "Reguler Fish", 160));

        fishMap.put("Sturgeon", new Fish(
            "Sturgeon",
            Set.of(Season.SUMMER, Season.WINTER), 
            Set.of(normalDay),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new MountainLake()),
            "Reguler Fish", 40));

        fishMap.put("Midnight Carp", new Fish(
            "Midnight Carp",
            Set.of(Season.FALL, Season.WINTER), 
            Set.of(new Range<>(new Time(20, 0), new Time(2, 0))),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new MountainLake(), new PondLocation()),
            "Reguler Fish", 80));

        fishMap.put("Flounder", new Fish(
            "Flounder",
            Set.of(Season.SUMMER, Season.SPRING), 
            Set.of(extraDay),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new Ocean()), "Reguler Fish", 60));

        fishMap.put("Halibut", new Fish(
            "Halibut",
            Set.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
            Set.of(new Range<>(new Time(6,0), new Time(11,0)), new Range<>(new Time(19,0), new Time(2,0))),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new Ocean()), "Reguler Fish", 40));
        
        fishMap.put("Octopus", new Fish(
            "Octopus",
            Set.of(Season.SUMMER), 
            Set.of(extraDay),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new Ocean()), "Reguler Fish", 120));

        fishMap.put("Pufferfish", new Fish(
            "Pufferfish",
            Set.of(Season.SUMMER), 
            Set.of(new Range<>(new Time(0,0), new Time(16,0))),
            Set.of(Weather.SUNNY), 
            Set.of(new Ocean()), "Reguler Fish", 240));

        fishMap.put("Sardine", new Fish(
            "Sardine",
            Set.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 
            Set.of(normalDay),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new Ocean()), "Reguler Fish", 40));

        fishMap.put("Super Cucumber", new Fish(
            "Super Cucumber", 
            Set.of(Season.SUMMER, Season.FALL, Season.WINTER), 
            Set.of(new Range<>(new Time(18,0), new Time(2,0))),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new Ocean()), "Reguler Fish", 80));

        fishMap.put("Catfish", new Fish(
            "Catfish",
            Set.of(Season.SPRING, Season.SUMMER, Season.FALL), 
            Set.of(extraDay),
            Set.of(Weather.RAINY), 
            Set.of(new ForestRiver(), new PondLocation()),
            "Reguler Fish", 40));

        fishMap.put("Salmon", new Fish(
            "Salmon",
            Set.of(Season.FALL), 
            Set.of(normalDay),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new ForestRiver()), "Reguler Fish", 160));

        // Legendry Fish
        fishMap.put("Angler", new Fish(
            "Angler",
            Set.of(Season.FALL), 
            Set.of(legendaryTime),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new PondLocation()), "Legendary Fish", 800));

        fishMap.put("Crimsonfish", new Fish(
            "Crimsonfish",
            Set.of(Season.SUMMER), 
            Set.of(legendaryTime),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new Ocean()), "Legendary Fish", 800));

        fishMap.put("Glacierfish", new Fish(
            "Glacierfish", 
            Set.of(Season.WINTER), 
            Set.of(legendaryTime),
            Set.of(Weather.SUNNY, Weather.RAINY), 
            Set.of(new ForestRiver()), "Legendary Fish", 800));

        fishMap.put("Legend", new Fish(
            "Legend",
            Set.of(Season.SPRING), 
            Set.of(legendaryTime),
            Set.of(Weather.RAINY), 
            Set.of(new MountainLake()), "Legendary Fish", 1600));
    }

    public static Fish getFish(String fishName) {
        return fishMap.get(fishName);
    }
    public static Set<String> getAllFishNames() {
        return fishMap.keySet();
    }
    public static Set<Fish> getAllFish() {
        return Set.copyOf(fishMap.values());
    }
}
