package model;

public class Time implements Comparable<Time> {
    private final int hour;
    private final int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time addMinutes(int minutesToAdd) {
        int totalMinutes = hour * 60 + minute + minutesToAdd;
        int newHour = (totalMinutes / 60) % 24;
        int newMinute = totalMinutes % 60;
        return new Time(newHour, newMinute);
    }

    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

    @Override
    public int compareTo(Time other) {
        if (this.hour != other.hour) return this.hour - other.hour;
        return this.minute - other.minute;
    }
}
