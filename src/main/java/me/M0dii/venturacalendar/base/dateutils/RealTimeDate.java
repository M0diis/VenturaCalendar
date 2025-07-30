package me.m0dii.venturacalendar.base.dateutils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RealTimeDate {
    private long second;
    private long minute;
    private long hour;
    private long day;
    private long week;
    private long month;
    private long year;
    private long era;

    private LocalDateTime localDateTime;

    public RealTimeDate(long era, LocalDateTime localDateTime) {
        this.second = localDateTime.getSecond();
        this.minute = localDateTime.getMinute();
        this.hour = localDateTime.getHour();
        this.day = localDateTime.getDayOfMonth();
        this.week = localDateTime.getDayOfWeek().getValue();
        this.month = localDateTime.getMonthValue();
        this.year = localDateTime.getYear();
        this.era = era;
        this.localDateTime = localDateTime;
    }

    public RealTimeDate(RealTimeDate date) {
        this.second = date.getSecond();
        this.minute = date.getMinute();
        this.hour = date.getHour();
        this.day = date.getDay();
        this.week = date.getWeek();
        this.month = date.getMonth();
        this.year = date.getYear();
        this.era = date.getEra();
        this.localDateTime = date.getLocalDateTime();
    }


}
