package me.m0dii.venturacalendar.base.dateutils;

import me.m0dii.venturacalendar.VenturaCalendar;

import java.time.LocalDateTime;
import java.util.List;

public class DateCalculator {
    private static final VenturaCalendar instance = VenturaCalendar.getInstance();
    private static final TimeSystemUtils timeSystemUtils = instance.getTimeSystemUtils();

    public static Date fromTicks(long ticks, TimeSystem timeSystem) {
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

        return new Date(timeSystem, rootTicks, tick, second, minute, hour, day, week, month, year, era);
    }

    public static RealTimeDate realTimeNow() {
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

        long era = 0;

        return new RealTimeDate(era, date);
    }
}
