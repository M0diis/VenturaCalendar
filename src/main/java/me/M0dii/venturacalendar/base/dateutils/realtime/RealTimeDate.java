package me.m0dii.venturacalendar.base.dateutils.realtime;

import java.time.LocalDateTime;

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

    public long getSecond() {
        return this.second;
    }

    public long getMinute() {
        return this.minute;
    }

    public long getHour() {
        return this.hour;
    }

    public long getDay() {
        return this.day;
    }

    public long getWeek() {
        return this.week;
    }

    public long getMonth() {
        return this.month;
    }

    public long getYear() {
        return this.year;
    }

    public long getEra() {
        return this.era;
    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public void setWeek(long week) {
        this.week = week;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public void setEra(long era) {
        this.era = era;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
