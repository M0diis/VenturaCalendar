package me.m0dii.venturacalendar.base.dateutils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.m0dii.venturacalendar.VenturaCalendar;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class VenturaCalendarDate {
    private final VenturaCalendar plugin = VenturaCalendar.getInstance();

    private final TimeSystem timeSystem;

    private long rootTicks;
    private long tick;
    private long second;
    private long minute;
    private long hour;
    private long day;
    private long week;
    private long month;
    private long year;
    private long era;

    public VenturaCalendarDate(VenturaCalendarDate venturaCalendarDate) {
        this.timeSystem = venturaCalendarDate.getTimeSystem();
        this.rootTicks = venturaCalendarDate.getRootTicks();

        this.tick = venturaCalendarDate.getTick();
        this.second = venturaCalendarDate.getSecond();
        this.minute = venturaCalendarDate.getMinute();
        this.hour = venturaCalendarDate.getHour();
        this.day = venturaCalendarDate.getDay();
        this.week = venturaCalendarDate.getWeek();
        this.month = venturaCalendarDate.getMonth();
        this.year = venturaCalendarDate.getYear();
        this.era = venturaCalendarDate.getEra();
    }

    public String getMonthName() {
        return this.getTimeSystem().getMonths().get(((int) this.month)).getName();
    }

    public String getSeasonName() {
        return this.getTimeSystem().getMonths().get(((int) this.month)).getSeasonName();
    }

    public String getDayName() {
        long dow = plugin.getDateUtils().getDayOfWeek(this);

        return this.getTimeSystem().getDayNames().get((int) dow);
    }

    public String getEraName() {
        return this.getTimeSystem().getEraNames().get((int) this.era);
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of((int) this.year, (int) this.month, (int) this.day, (int) this.hour, (int) this.minute, (int) this.second);
    }

    public Date toDate() {
        return new Date((int) this.year, (int) this.month, (int) this.day, (int) this.hour, (int) this.minute, (int) this.second);
    }
}

