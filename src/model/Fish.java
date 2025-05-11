package model;

import utility.Season;
import utility.Weather;
import java.util.Set;

public class Fish extends Item{
    private Set<Season> season;
    private Set<Range<Time>> availableTime;
    private Set<Weather> weather;
    private Set<Location> location;
    private String type;
    private Integer price;

    public Fish(String itemName, Set<Season> season, Set<Range<Time>> availableTime, Set<Weather> weather, Set<Location> location, String type, Integer price) {
        super(itemName, true);
        this.season = season;
        this.availableTime = availableTime;
        this.weather = weather;
        this.location = location;
        this.type = type;
        this.price = price;
    }

    public Set<Season> getSeason() {
        return season;
    }
    public void setSeason(Set<Season> season) {
        this.season = season;
    }
    public Set<Range<Time>> getAvailableTime() {
        return availableTime;
    }
    public Set<Weather> getWeather() {
        return weather;
    }
    public void setWeather(Set<Weather> weather) {
        this.weather = weather;
    }
    public Set<Location> getLocation() {
        return location;
    }
    public void setLocation(Set<Location> location) {
        this.location = location;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    @Override
    public void useItem() {
        System.out.println("Using fish: " + itemName);
        System.out.println("Season: " + season);
        System.out.println("Time: " + availableTime);
        System.out.println("Weather: " + weather);
        System.out.println("Location: " + location);
    }
    @Override
    public void displayItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Edible: " + (edible ? "Yes" : "No"));
        System.out.println("Season: " + season);
        System.out.println("Time: " + availableTime);
        System.out.println("Weather: " + weather);
        System.out.println("Location: " + location);
    }
    @Override
    public String toString() {
        return "Fish{" +
                "itemName='" + itemName + '\'' +
                ", edible=" + edible +
                ", season=" + season +
                ", time=" + availableTime +
                ", weather=" + weather +
                ", location=" + location +
                '}';
    }
}
