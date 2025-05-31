package model;

import java.util.Random;
import utility.MessageListener;
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
    private boolean isPaused = false;

    private boolean playerSlept = false;
    private Runnable autoSleepHandler;
    private Integer totalDay;
    private Integer totalSeasons;

    private MessageListener messageListener;

    public void pauseTimeThread() {
        isPaused = true;
    }

    public void resumeTimeThread() {
        isPaused = false;
    }

    public GameState(Weather weather, Season season, 
    FarmMap farmMap, Player player, boolean showTime, MessageListener messageListener) {
        this.weather = weather;
        this.season = season;
        this.time = new Time(6, 0); // Start at 06:00
        this.farmMap = farmMap;
        this.player = player;
        this.showTime = showTime;
        this.totalDay = 1; // Start at day 1
        this.messageListener = messageListener;
        this.totalSeasons = 1; 

        Thread timeThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); 
                    if (!isPaused){
                        time = time.addMinutes(5); 
                        farmMap.tick(5); 
                        checkAndUpdateCooking(5);
                        if (showTime) {
                            //notify("Current Game Time: " + time);
                            System.out.println("Current Game Time: " + time);
                        }

                        // Case handle for sleeping
                        if (time.getHour() == 0 || time.getHour() == 1 && !playerSlept && autoSleepHandler != null) {
                            //notify("It's midnight. You should sleep soon...");
                            System.out.println("It's midnight. You should sleep soon...");
                        }
                        if (time.getHour() == 2 && !playerSlept && autoSleepHandler != null) {
                            if (!player.isInsideHouse()) {
                                //notify("You passed out outside after 2 AM!");
                                System.out.println("You passed out outside after 2 AM!");
                                player.setEnergy(10);
                                playerSlept = true;
                                setTime(new Time(6, 0)); // langsung skip ke pagi
                            } else {
                                //notify("🛏️ It's 2 AM. You automatically fall asleep.");
                                System.out.println("🛏️ It's 2 AM. You automatically fall asleep.");
                                autoSleepHandler.run(); // menjalankan sleep() normal
                                playerSlept = true;
                            }
                        }
                        if (time.getHour() == 6 && time.getMinute() == 0) {
                            playerSlept = false;
                        }
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

    public Time getTime() { 
        return time; 
    }
    public void setTime(Time time) { 
        this.time = time; 
    }
    public Weather getWeather() { 
        return weather; 
    }
    public Season getSeason() { 
        return season; 
    }

    public void advanceTime(int minutes) {
        pauseTimeThread();  // Pause automatic ticking
        time = time.addMinutes(minutes);
        farmMap.tick(minutes); 
        checkAndUpdateCooking(minutes);
        if (time.compareTo(new Time(24, 0)) >= 0) {
            time = new Time(0, 0);
            advanceDay();
        }
        resumeTimeThread();  // Resume automatic ticking
    }

    public void advanceDay() {
        totalDay++;
        farmMap.tick(24 * 60); // Simulate a full day tick
        if (totalDay % 10 == 0) {
            totalSeasons++;
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

    public FarmMap getFarmMap() {
        return farmMap;
    }

    public void checkAndUpdateCooking(int minutes) {
        CookingTask currentCooking = player.getCurrentCooking();
        if (currentCooking != null) {
            currentCooking.advanceTime(minutes);
            
            if (currentCooking.isDone()) {
                // Cooking is complete, add food to inventory
                Recipe recipe = currentCooking.getRecipe();
                Food cookedFood = recipe.getFood();
                player.getInventory().addItem(cookedFood);
                
                if (messageListener != null) {
                    messageListener.onMessage("✅ " + cookedFood.getItemName() + " is ready!");
                }
                
                // Clear current cooking task
                player.setCurrentCooking(null);
            }
        }
    }

    public Integer getTotalDay() {
        return totalDay;
    }

    public Integer getTotalSeason() {
        return totalSeasons;
    }


}
