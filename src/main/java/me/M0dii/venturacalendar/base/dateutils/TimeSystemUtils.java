package me.m0dii.venturacalendar.base.dateutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TimeSystemUtils {
    public List<Long> getMonthTPU(TimeSystem timeSystem) {
        long ticksPerSecond = timeSystem.getTicksPerSecond();
        long ticksPerMinute = ticksPerSecond * timeSystem.getSecondsPerMinute();
        long ticksPerHour = ticksPerMinute * timeSystem.getMinutesPerHour();
        long ticksPerDay = ticksPerHour * timeSystem.getHoursPerDay();

        return timeSystem.getDaysPerMonth().stream()
                .mapToLong(daysThisMonth -> daysThisMonth)
                .mapToObj(daysThisMonth -> ticksPerDay * daysThisMonth)
                .collect(Collectors.toList());
    }

    public long getTPU(DateEnum unit, TimeSystem timeSystem) {
        long ticksPerSecond = timeSystem.getTicksPerSecond();
        long ticksPerMinute = ticksPerSecond * timeSystem.getSecondsPerMinute();
        long ticksPerHour = ticksPerMinute * timeSystem.getMinutesPerHour();
        long ticksPerDay = ticksPerHour * timeSystem.getHoursPerDay();
        long ticksPerWeek = ticksPerDay * timeSystem.getDaysPerWeek();

        List<Long> ticksPerMonth = new ArrayList<>();

        for (long daysThisMonth : timeSystem.getDaysPerMonth())
            ticksPerMonth.add(ticksPerDay * daysThisMonth);

        long ticksPerYear = ticksPerMonth.stream().mapToLong(ticksThisMonth -> ticksThisMonth).sum();

        return switch (unit) {
            case TICK -> 1;
            case SECOND -> ticksPerSecond;
            case MINUTE -> ticksPerMinute;
            case HOUR -> ticksPerHour;
            case DAY -> ticksPerDay;
            case WEEK -> ticksPerWeek;
            case YEAR -> ticksPerYear;
            default -> 0;
        };
    }
}
