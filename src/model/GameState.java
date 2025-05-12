package model;

import utility.Season;
import utility.Weather;

public class GameState {
    private Time time;
    private Weather weather;
    private Season season;
    private final FarmMap farmMap;
    private boolean showTime;
    private Player player;

    private boolean playerSlept = false;
    private Runnable autoSleepHandler;

    public GameState(Weather weather, Season season, FarmMap farmMap, Player player, boolean showTime) {
        this.weather = weather;
        this.season = season;
        this.time = new Time(1, 0); // Start at 06:00
        this.farmMap = farmMap;
        this.player = player;
        this.showTime = showTime;

        Thread timeThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // 1 second real time
                    time = time.addMinutes(5); // 5 minutes game time
                    farmMap.tick(5); 
                    if (showTime) {
                        System.out.println("Current Game Time: " + time);
                    }
                    if (time.getHour() == 0 || time.getHour() == 1 && !playerSlept && autoSleepHandler != null) {
                        System.out.println("It's midnight. You should sleep soon...");
                    }
                    if (time.getHour() == 2 && !playerSlept && autoSleepHandler != null) {
                        System.out.println("It's 2 AM. You pass out from exhaustion...");
                        autoSleepHandler.run();
                        playerSlept = true;
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
            // Logic to change season or weather can be added here
        }
    }
}
