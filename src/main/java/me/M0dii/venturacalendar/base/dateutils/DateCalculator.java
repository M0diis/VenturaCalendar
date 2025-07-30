package me.m0dii.venturacalendar.base.dateutils;

import me.m0dii.venturacalendar.VenturaCalendar;

import java.time.LocalDateTime;
import java.util.List;

public class DateCalculator {
    private static final VenturaCalendar instance = VenturaCalendar.getInstance();
    private static final TimeSystemUtils timeSystemUtils = instance.getTimeSystemUtils();
    private DateCalculator() {
        // Prevent instantiation
    }

    public static VenturaCalendarDate fromTicks(long ticks, TimeSystem timeSystem) {
        long tick = 0;
        long second = 0;
        long minute = 0;
        long hour = 0;
        long day = 0;
        long week = 0;
        long month = 0;
        long year = 0;
        long era = 0;

        List<Long> erasBegin = timeSystem.getErasBegin();
        List<Long> erasEnd = timeSystem.getErasEnd();

        long ticksPerSecond = timeSystemUtils.getTPU(DateEnum.SECOND, timeSystem);
        long ticksPerMinute = timeSystemUtils.getTPU(DateEnum.MINUTE, timeSystem);
        long ticksPerHour = timeSystemUtils.getTPU(DateEnum.HOUR, timeSystem);
        long ticksPerDay = timeSystemUtils.getTPU(DateEnum.DAY, timeSystem);
        long ticksPerWeek = timeSystemUtils.getTPU(DateEnum.WEEK, timeSystem);
        List<Long> ticksPerMonth = timeSystemUtils.getMonthTPU(timeSystem);
        long ticksPerYear = timeSystemUtils.getTPU(DateEnum.YEAR, timeSystem);
        long rootTicks = ticks;

        long dayTicks = 24000;

        ticks += (timeSystem.getDayZero() * dayTicks) + (timeSystem.getWeekZero() * (7 * dayTicks));

        for (int i = 0, m = 0; i < timeSystem.getMonthZero(); i++, m++) {
            if (m > timeSystem.getMonths().size() - 1) {
                m = 0;
            }

            ticks += (timeSystem.getMonths().get(m).getDays() * dayTicks);
        }

        year = ticks / ticksPerYear;
        ticks = ticks - year * ticksPerYear;

        for (long ticksThisMonth : ticksPerMonth) {
            if (ticks / ticksThisMonth > 0) {
                month++;
                ticks = ticks - ticksThisMonth;
            }
        }

        week = ticks / ticksPerWeek;

        day = ticks / ticksPerDay;
        ticks = ticks - day * ticksPerDay;

        hour = ticks / ticksPerHour;
        ticks = ticks - hour * ticksPerHour;

        minute = ticks / ticksPerMinute;
        ticks = ticks - minute * ticksPerMinute;

        second = ticks / ticksPerSecond;
        ticks = ticks - second * ticksPerSecond;

        tick = ticks;
        ticks = ticks - tick;

        tick = ticks;

        for (long eraBegin : erasBegin) {
            int index = erasBegin.indexOf(eraBegin);
            eraBegin = eraBegin - timeSystem.getYearZero();
            long eraEnd = erasEnd.get(index) - timeSystem.getYearZero();

            if (year >= eraBegin && year <= eraEnd)
                era = index;
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

    public static RealTimeDate realTimeNow() {
        return realTimeNow(0);
    }

    public static RealTimeDate realTimeNow(int monthOffsetVar) {
        LocalDateTime date = LocalDateTime.now();

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
            if (monthOffsetVar > 0) {
                date = date.plusMonths(monthOffsetVar);
            } else {
                date = date.minusMonths(Math.abs(monthOffsetVar));
            }
        }

        long era = 0;

        List<String> eras = instance.getTimeConfig().getListString("main-time-system.eras");

        for (String e : eras) {
            String[] split = e.split(", ");

            if (split.length != 3) {
                instance.getLogger().warning("Invalid era format: " + e);
                instance.getLogger().warning("Expected format: 'Era Name, Begin Year, End Year'");
                continue;
            }

            long begin = Long.parseLong(split[1].trim());
            long end = Long.parseLong(split[2].trim());

            if (date.getYear() >= begin && date.getYear() <= end) {
                era = eras.indexOf(e);
            }
        }

        return new RealTimeDate(era, date);
    }
}
