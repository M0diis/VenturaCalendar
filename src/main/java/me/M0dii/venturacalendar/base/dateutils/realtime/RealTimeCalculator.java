package me.m0dii.venturacalendar.base.dateutils.realtime;

import me.m0dii.venturacalendar.VenturaCalendar;

import java.time.LocalDateTime;

public class RealTimeCalculator {
    private static final VenturaCalendar instance = VenturaCalendar.getInstance();

    public static RealTimeDate now() {
        LocalDateTime date = LocalDateTime.now();

        final String path = "main-time-system.real-time";

        long secondOffset = instance.getTimeConfig().getLong(path + ".second-offset");
        long minuteOffset = instance.getTimeConfig().getLong(path + ".minute-offset");
        long hourOffset = instance.getTimeConfig().getLong(path + ".hour-offset");
        long dayOffset = instance.getTimeConfig().getLong(path + ".day-offset");
        long weekOffset = instance.getTimeConfig().getLong(path + ".week-offset");
        long monthOffset = instance.getTimeConfig().getLong(path + ".month-offset");
        long yearOffset = instance.getTimeConfig().getLong(path + ".year-offset");

        date = date.plusSeconds(secondOffset)
                   .plusMinutes(minuteOffset)
                   .plusHours(hourOffset)
                   .plusDays(dayOffset)
                   .plusWeeks(weekOffset)
                   .plusMonths(monthOffset)
                   .plusYears(yearOffset);

        long era = 0;

        return new RealTimeDate(era, date);
    }
}
