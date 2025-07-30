package me.m0dii.venturacalendar.base.dateutils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
@Builder
public class TimeSystem {
    private final String worldName;
    private final String name;
    private boolean realTime = false;

    // Date parameters.
    private final long ticksPerSecond;
    private final long secondsPerMinute;
    private final long minutesPerHour;
    private final long hoursPerDay;
    private final long daysPerWeek;
    private final List<Long> daysPerMonth;
    private final long monthsPerYear;
    private final List<Long> erasBegin;
    private final List<Long> erasEnd;

    // Zero points
    private final long tickZero;
    private final long secondZero;
    private final long minuteZero;
    private final long hourZero;
    private final long dayZero;
    private final long weekZero;
    private final long monthZero;
    private final long yearZero;
    private final long eraZero;

    // Date parameter names.
    private final List<Month> months;
    private final List<String> dayNames;
    private final List<String> eraNames;

    public static TimeSystem of(TimeSystem timeSystem) {
        return TimeSystem.builder()
                .worldName(timeSystem.getWorldName())
                .name(timeSystem.getName())
                .ticksPerSecond(timeSystem.getTicksPerSecond())
                .secondsPerMinute(timeSystem.getSecondsPerMinute())
                .minutesPerHour(timeSystem.getMinutesPerHour())
                .hoursPerDay(timeSystem.getHoursPerDay())
                .daysPerWeek(timeSystem.getDaysPerWeek())
                .daysPerMonth(timeSystem.getDaysPerMonth())
                .monthsPerYear(timeSystem.getMonthsPerYear())
                .erasBegin(timeSystem.getErasBegin())
                .erasEnd(timeSystem.getErasEnd())
                .tickZero(timeSystem.getTickZero())
                .secondZero(timeSystem.getSecondZero())
                .minuteZero(timeSystem.getMinuteZero())
                .hourZero(timeSystem.getHourZero())
                .dayZero(timeSystem.getDayZero())
                .weekZero(timeSystem.getWeekZero())
                .monthZero(timeSystem.getMonthZero())
                .yearZero(timeSystem.getYearZero())
                .eraZero(timeSystem.getEraZero())
                .dayNames(timeSystem.getDayNames())
                .months(timeSystem.getMonths())
                .eraNames(timeSystem.getEraNames())
                .realTime(timeSystem.isRealTime())
                .build();
    }

    public long getDaysPerWeek() {
        if (daysPerWeek > 8)
            return 8;

        return daysPerWeek;
    }

    public Month getMonth(@NotNull String name) {
        return months.stream().filter(month -> month.getName().equals(name)).findFirst().orElse(null);

    }

}
