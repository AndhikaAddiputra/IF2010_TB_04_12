package model;

import java.util.Random;

import utility.Season;
import utility.Weather;

public class GameState {
    private Time time;
    private Weather weather;
    private Season season;
    private final FarmMap farmMap;
    private boolean showTime;
    private Player player;
    private Location currentWorldLocation = null; // Lokasi saat di worldMap
    private boolean isInWorldMap = false;   

    private boolean playerSlept = false;
    private Runnable autoSleepHandler;
    private Integer day;

    public GameState(Weather weather, Season season, FarmMap farmMap, Player player, boolean showTime) {
        this.weather = weather;
        this.season = season;
        this.time = new Time(6, 0); // Start at 06:00
        this.farmMap = farmMap;
        this.player = player;
        this.showTime = showTime;
        this.day = 1; // Start at day 1

        Thread timeThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); 
                    time = time.addMinutes(5); 
                    farmMap.tick(5); 
                    if (showTime) {
                        System.out.println("Current Game Time: " + time);
                    }

                    // Case handle for sleeping
                    if (time.getHour() == 0 || time.getHour() == 1 && !playerSlept && autoSleepHandler != null) {
                        System.out.println("It's midnight. You should sleep soon...");
                    }
                    if (time.getHour() == 2 && !playerSlept && autoSleepHandler != null) {
                        if (!player.isInsideHouse()) {
                            System.out.println("You passed out outside after 2 AM!");
                            player.setEnergy(10);
                            playerSlept = true;
                            setTime(new Time(6, 0)); // langsung skip ke pagi
                        } else {
                            System.out.println("🛏️ It's 2 AM. You automatically fall asleep.");
                            autoSleepHandler.run(); // menjalankan sleep() normal
                            playerSlept = true;
                        }
                    }
                    if (time.getHour() == 6 && time.getMinute() == 0) {
                        playerSlept = false;
                    }

                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        timeThread.setDaemon(true);
        timeThread.start();
    }

    public void setAutoSleepHandler(Runnable autoSleepHandler) {
        this.autoSleepHandler = autoSleepHandler;
    }

    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }
    public Weather getWeather() { return weather; }
    public Season getSeason() { return season; }

    public void advanceTime(int minutes) {
        time = time.addMinutes(minutes);
        if (time.compareTo(new Time(24, 0)) >= 0) {
            time = new Time(0, 0); // Reset to 00:00
            advanceDay(); // Pindah ke hari berikutnya
        }
    }

    public void advanceDay() {
        day++;
        if (day > 10) {
            day = 1;
            season = season.next();
            farmMap.killOutOfSeasonCrops(season);
        }
    
        weather = determineWeatherForToday();
    }

    private Weather determineWeatherForToday() {
    Random rand = new Random();
    int chance = rand.nextInt(100);
    if (chance < 30) {
        return Weather.RAINY;
        } else {
        return Weather.SUNNY;
        }
    }

    public boolean isInWorldMap() {
        return isInWorldMap;
    }
    public void setInWorldMap(boolean inWorldMap) {
        this.isInWorldMap = inWorldMap;
    }
    
    public Location getCurrentWorldLocation() {
        return currentWorldLocation;
    }
    public void setCurrentWorldLocation(Location location) {
        this.currentWorldLocation = location;
    }
}
