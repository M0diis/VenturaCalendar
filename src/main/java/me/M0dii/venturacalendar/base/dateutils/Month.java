package me.m0dii.venturacalendar.base.dateutils;

public class Month {
    private final String name;
    private final long days;
    private final String seasonName;

    public Month(String name, long days, String season) {
        this.name = name;
        this.days = days;
        this.seasonName = season;
    }

    public String getName() {
        return name;
    }

    public long getDays() {
        return days;
    }

    public String getSeasonName() {
        return seasonName;
    }
}
