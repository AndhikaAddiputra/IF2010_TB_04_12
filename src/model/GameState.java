package model;

import utility.Season;
import utility.Weather;

public class GameState {
    private Time time;
    private Weather weather;
    private Season season;
    private final FarmMap farmMap;
    private boolean showTime;

    public GameState(Weather weather, Season season, FarmMap farmMap, boolean showTime) {
        this.weather = weather;
        this.season = season;
        this.time = new Time(0, 1); // Start at 00:01
        this.farmMap = farmMap;
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
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        timeThread.setDaemon(true);
        timeThread.start();
    }

    public Time getTime() { return time; }
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
