package me.m0dii.venturacalendar.base.dateutils;

import me.m0dii.venturacalendar.VenturaCalendar;

import java.time.LocalDateTime;
import java.util.List;

public class DateCalculator {
    private static final TimeSystemUtils timeSystemUtils = new TimeSystemUtils();
    private DateCalculator() {
        // Prevent instantiation
    }

    public static VenturaCalendarDate fromTicks(long ticks, TimeSystem timeSystem) {
        if (timeSystem == null || timeSystem.getDaysPerMonth() == null || timeSystem.getDaysPerMonth().isEmpty()) {
            throw new IllegalArgumentException("A time system with at least one month is required.");
        }

        long ticksPerSecond = timeSystemUtils.getTPU(DateEnum.SECOND, timeSystem);
        long ticksPerMinute = timeSystemUtils.getTPU(DateEnum.MINUTE, timeSystem);
        long ticksPerHour = timeSystemUtils.getTPU(DateEnum.HOUR, timeSystem);
        long ticksPerDay = timeSystemUtils.getTPU(DateEnum.DAY, timeSystem);
        long ticksPerWeek = timeSystemUtils.getTPU(DateEnum.WEEK, timeSystem);
        List<Long> ticksPerMonth = timeSystemUtils.getMonthTPU(timeSystem);
        long ticksPerYear = timeSystemUtils.getTPU(DateEnum.YEAR, timeSystem);
        long rootTicks = ticks;

        if (ticksPerSecond <= 0 || ticksPerMinute <= 0 || ticksPerHour <= 0
                || ticksPerDay <= 0 || ticksPerWeek <= 0 || ticksPerYear <= 0) {
            throw new IllegalArgumentException("Time-system units must all be greater than zero.");
        }

        long adjustedTicks;

        try {
            adjustedTicks = Math.addExact(ticks, Math.multiplyExact(timeSystem.getDayZero(), ticksPerDay));
            adjustedTicks = Math.addExact(adjustedTicks, Math.multiplyExact(timeSystem.getWeekZero(), ticksPerWeek));
            adjustedTicks = Math.addExact(adjustedTicks,
                    getMonthOffsetTicks(timeSystem.getMonthZero(), ticksPerMonth, ticksPerYear));
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Time-system offsets overflow the supported tick range.", ex);
        }

        long year = Math.floorDiv(adjustedTicks, ticksPerYear);
        long remainingTicks = Math.floorMod(adjustedTicks, ticksPerYear);
        long month = 0;

        for (int i = 0; i < ticksPerMonth.size(); i++) {
            long ticksThisMonth = ticksPerMonth.get(i);

            if (remainingTicks >= ticksThisMonth && i < ticksPerMonth.size() - 1) {
                month++;
                remainingTicks -= ticksThisMonth;
            } else {
                break;
            }
        }

        long week = remainingTicks / ticksPerWeek;
        long day = remainingTicks / ticksPerDay;
        remainingTicks -= day * ticksPerDay;

        long hour = remainingTicks / ticksPerHour;
        remainingTicks -= hour * ticksPerHour;

        long minute = remainingTicks / ticksPerMinute;
        remainingTicks -= minute * ticksPerMinute;

        long second = remainingTicks / ticksPerSecond;
        remainingTicks -= second * ticksPerSecond;

        long tick = remainingTicks;
        long era = 0;
        List<Long> erasBegin = timeSystem.getErasBegin();
        List<Long> erasEnd = timeSystem.getErasEnd();
        long displayYear = year + timeSystem.getYearZero();

        for (int index = 0; index < Math.min(erasBegin.size(), erasEnd.size()); index++) {
            long eraBegin = erasBegin.get(index);
            long eraEnd = erasEnd.get(index);

            if (displayYear >= eraBegin && displayYear <= eraEnd) {
                era = index;
            }
        }

        return VenturaCalendarDate.builder()
                .timeSystem(timeSystem)
                .rootTicks(rootTicks)
                .tick(tick)
                .second(second)
                .minute(minute)
                .hour(hour)
                .day(day)
                .week(week)
                .month(month)
                .year(year)
                .era(era)
                .build();
    }

    private static long getMonthOffsetTicks(long monthOffset, List<Long> ticksPerMonth, long ticksPerYear) {
        int monthCount = ticksPerMonth.size();
        long fullYears = Math.floorDiv(monthOffset, monthCount);
        int remainingMonths = Math.floorMod(monthOffset, monthCount);
        long offset = Math.multiplyExact(fullYears, ticksPerYear);

        for (int i = 0; i < remainingMonths; i++) {
            offset = Math.addExact(offset, ticksPerMonth.get(i));
        }

        return offset;
    }

    public static RealTimeDate realTimeNow() {
        return realTimeNow(0);
    }

    public static RealTimeDate realTimeNow(int monthOffsetVar) {
        VenturaCalendar instance = VenturaCalendar.getInstance();
        LocalDateTime date = LocalDateTime.now();

        if (instance == null) {
            return new RealTimeDate(0, date);
        }

        final String path = "main-time-system.real-time.offsets";

        long secondOffset = instance.getTimeConfig().getLong(path + ".second");
        long minuteOffset = instance.getTimeConfig().getLong(path + ".minute");
        long hourOffset = instance.getTimeConfig().getLong(path + ".hour");
        long dayOffset = instance.getTimeConfig().getLong(path + ".day");
        long weekOffset = instance.getTimeConfig().getLong(path + ".week");
        long monthOffset = instance.getTimeConfig().getLong(path + ".month");
        long yearOffset = instance.getTimeConfig().getLong(path + ".year");

        date = date.plusSeconds(secondOffset)
                .plusMinutes(minuteOffset)
                .plusHours(hourOffset)
                .plusDays(dayOffset)
                .plusWeeks(weekOffset)
                .plusMonths(monthOffset)
                .plusYears(yearOffset);

        if (monthOffsetVar != 0) {
            date = date.plusMonths(monthOffsetVar);
        }

        long era = 0;

        List<String> eras = instance.getTimeConfig().getListString("main-time-system.eras");

        for (int index = 0; index < eras.size(); index++) {
            String e = eras.get(index);
            String[] split = e.split(",", -1);

            if (split.length != 3) {
                instance.getLogger().warning("Invalid era format: " + e);
                instance.getLogger().warning("Expected format: 'Era Name, Begin Year, End Year'");
                continue;
            }

            long begin;
            long end;

            try {
                begin = Long.parseLong(split[1].trim());
                end = Long.parseLong(split[2].trim());
            } catch (NumberFormatException ex) {
                instance.getLogger().warning("Invalid era years: " + e);
                continue;
            }

            if (date.getYear() >= begin && date.getYear() <= end) {
                era = index;
            }
        }

        return new RealTimeDate(era, date);
    }
}
