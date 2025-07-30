package me.m0dii.venturacalendar.base.dateutils;

import lombok.Getter;

@Getter
public class Month {
    private final String name;
    private final long days;
    private final String seasonName;

    public Month(String name, long days, String season) {
        this.name = name;
        this.days = days;
        this.seasonName = season;
    }

}
