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
        int index = (int) this.month;

        if (index < 0 || index >= this.getTimeSystem().getMonths().size()) {
            return "";
        }

        return this.getTimeSystem().getMonths().get(index).getName();
    }

    public String getSeasonName() {
        int index = (int) this.month;

        if (index < 0 || index >= this.getTimeSystem().getMonths().size()) {
            return "";
        }

        String seasonName = this.getTimeSystem().getMonths().get(index).getSeasonName();
        return seasonName == null ? "" : seasonName;
    }

    public String getDayName() {
        VenturaCalendar plugin = VenturaCalendar.getInstance();

        if (plugin == null) {
            return "";
        }

        long dow = plugin.getDateUtils().getDayOfWeek(this);

        int index = (int) dow;

        if (index < 0 || index >= this.getTimeSystem().getDayNames().size()) {
            return "";
        }

        return this.getTimeSystem().getDayNames().get(index);
    }

    public String getEraName() {
        int index = (int) this.era;

        if (index < 0 || index >= this.getTimeSystem().getEraNames().size()) {
            return "";
        }

        return this.getTimeSystem().getEraNames().get(index);
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of((int) this.year, (int) this.month, (int) this.day, (int) this.hour, (int) this.minute, (int) this.second);
    }
}

