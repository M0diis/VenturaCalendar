package me.m0dii.venturacalendar.base.dateutils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.m0dii.venturacalendar.VenturaCalendar;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
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

    public static VenturaCalendarDate clone(VenturaCalendarDate venturaCalendarDate) {
        return VenturaCalendarDate.builder()
                .timeSystem(venturaCalendarDate.getTimeSystem())
                .rootTicks(venturaCalendarDate.getRootTicks())
                .tick(venturaCalendarDate.getTick())
                .second(venturaCalendarDate.getSecond())
                .minute(venturaCalendarDate.getMinute())
                .hour(venturaCalendarDate.getHour())
                .day(venturaCalendarDate.getDay())
                .week(venturaCalendarDate.getWeek())
                .month(venturaCalendarDate.getMonth())
                .year(venturaCalendarDate.getYear())
                .era(venturaCalendarDate.getEra())
                .build();
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
}

