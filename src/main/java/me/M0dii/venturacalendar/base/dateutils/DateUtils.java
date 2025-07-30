package me.m0dii.venturacalendar.base.dateutils;

import me.m0dii.venturacalendar.VenturaCalendar;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class DateUtils {
    private final VenturaCalendar plugin;

    public DateUtils(VenturaCalendar plugin) {
        this.plugin = plugin;
    }

    /*
     * Puts all date properties from DateEnum into a HashMap.
     */
    public Map<DateEnum, Object> toHashMap(VenturaCalendarDate venturaCalendarDate) {
        venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);

        Map<DateEnum, Object> dateMap = new EnumMap<>(DateEnum.class);

        dateMap.put(DateEnum.TIMESYSTEM, venturaCalendarDate.getTimeSystem());

        dateMap.put(DateEnum.TICK, venturaCalendarDate.getTick());
        dateMap.put(DateEnum.SECOND, venturaCalendarDate.getSecond());
        dateMap.put(DateEnum.MINUTE, venturaCalendarDate.getMinute());
        dateMap.put(DateEnum.HOUR, venturaCalendarDate.getHour());
        dateMap.put(DateEnum.DAY, venturaCalendarDate.getDay());
        dateMap.put(DateEnum.WEEK, venturaCalendarDate.getWeek());
        dateMap.put(DateEnum.MONTH, venturaCalendarDate.getMonth());
        dateMap.put(DateEnum.YEAR, venturaCalendarDate.getYear());
        dateMap.put(DateEnum.ERA, venturaCalendarDate.getEra());

        return dateMap;
    }

    @Nonnull
    public VenturaCalendarDate addZeroPoints(VenturaCalendarDate venturaCalendarDate) {
        venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);

        TimeSystem timeSystem = TimeSystem.of(venturaCalendarDate.getTimeSystem());

        venturaCalendarDate.setTick(venturaCalendarDate.getTick() + timeSystem.getTickZero());
        venturaCalendarDate.setSecond(venturaCalendarDate.getSecond() + timeSystem.getSecondZero());
        venturaCalendarDate.setMinute(venturaCalendarDate.getMinute() + timeSystem.getMinuteZero());
        venturaCalendarDate.setHour(venturaCalendarDate.getHour() + timeSystem.getHourZero());
        venturaCalendarDate.setDay(venturaCalendarDate.getDay() + 1);
        venturaCalendarDate.setWeek(venturaCalendarDate.getWeek() + 1);
        venturaCalendarDate.setMonth(venturaCalendarDate.getMonth() + 1);
        venturaCalendarDate.setYear(venturaCalendarDate.getYear() + timeSystem.getYearZero());
        venturaCalendarDate.setEra(venturaCalendarDate.getEra() + timeSystem.getEraZero());

        return venturaCalendarDate;
    }

    public VenturaCalendarDate removeZeroPoints(VenturaCalendarDate venturaCalendarDate) {
        venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);

        TimeSystem timeSystem = TimeSystem.of(venturaCalendarDate.getTimeSystem());

        venturaCalendarDate.setTick(venturaCalendarDate.getTick() - timeSystem.getTickZero());
        venturaCalendarDate.setSecond(venturaCalendarDate.getSecond() - timeSystem.getSecondZero());
        venturaCalendarDate.setMinute(venturaCalendarDate.getMinute() - timeSystem.getMinuteZero());
        venturaCalendarDate.setHour(venturaCalendarDate.getHour() - timeSystem.getHourZero());
        venturaCalendarDate.setDay(venturaCalendarDate.getDay() - 1);
        venturaCalendarDate.setWeek(venturaCalendarDate.getWeek() - 1);
        venturaCalendarDate.setMonth(venturaCalendarDate.getMonth() - 1);
        venturaCalendarDate.setYear(venturaCalendarDate.getYear() - timeSystem.getYearZero());
        venturaCalendarDate.setEra(venturaCalendarDate.getEra() - timeSystem.getEraZero());

        return venturaCalendarDate;
    }

    /*
     * Methods to calculate up or down a single parameter from a given date, with the date timeSystem.
     * With maximum and minimum check.
     */
    public VenturaCalendarDate up(DateEnum unit, int count, VenturaCalendarDate venturaCalendarDate) {
        return calculate(unit, count, venturaCalendarDate, false);
    }

    public VenturaCalendarDate down(DateEnum unit, int count, VenturaCalendarDate venturaCalendarDate) {
        return calculate(unit, count, venturaCalendarDate, true);
    }

    private VenturaCalendarDate calculate(DateEnum unit, int count, VenturaCalendarDate venturaCalendarDate, boolean down) {
        venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);

        TimeSystem timeSystem = TimeSystem.of(venturaCalendarDate.getTimeSystem());

        long ticks = venturaCalendarDate.getRootTicks();

        long ticksPerSecond = timeSystem.getTicksPerSecond();
        long ticksPerMinute = ticksPerSecond * timeSystem.getSecondsPerMinute();
        long ticksPerHour = ticksPerMinute * timeSystem.getMinutesPerHour();
        long ticksPerDay = ticksPerHour * timeSystem.getHoursPerDay();
        long ticksPerWeek = ticksPerDay * timeSystem.getDaysPerWeek();

        ArrayList<Long> ticksPerMonth = new ArrayList<>();

        for (long daysThisMonth : timeSystem.getDaysPerMonth())
            ticksPerMonth.add(ticksPerDay * daysThisMonth);

        long ticksPerYear = 0;

        for (long ticksThisMonth : ticksPerMonth)
            ticksPerYear = ticksPerYear + ticksThisMonth;

        return switch (unit) {
            case TICK -> {
                if (!down) yield DateCalculator.fromTicks(ticks + count, timeSystem);
                yield DateCalculator.fromTicks(ticks - count, timeSystem);
            }
            case SECOND -> {
                if (!down) yield DateCalculator.fromTicks(ticks + (ticksPerSecond * count), timeSystem);
                yield DateCalculator.fromTicks(ticks - (ticksPerSecond * count), timeSystem);
            }
            case MINUTE -> {
                if (!down) yield DateCalculator.fromTicks(ticks + (ticksPerMinute * count), timeSystem);
                yield DateCalculator.fromTicks(ticks - (ticksPerMinute * count), timeSystem);
            }
            case HOUR -> {
                if (!down) yield DateCalculator.fromTicks(ticks + (ticksPerHour * count), timeSystem);
                yield DateCalculator.fromTicks(ticks - (ticksPerHour * count), timeSystem);
            }
            case DAY -> {
                if (!down) yield DateCalculator.fromTicks(ticks + (ticksPerDay * count), timeSystem);
                yield DateCalculator.fromTicks(ticks - (ticksPerDay * count), timeSystem);
            }
            case WEEK -> {
                if (!down) yield DateCalculator.fromTicks(ticks + (ticksPerWeek * count), timeSystem);
                yield DateCalculator.fromTicks(ticks - (ticksPerWeek * count), timeSystem);
            }
            case MONTH -> {
                if (!down)
                    yield DateCalculator.fromTicks(ticks + ticksPerMonth.get((int) venturaCalendarDate.getMonth() - 1), timeSystem);
                yield DateCalculator.fromTicks(ticks - ticksPerMonth.get((int) venturaCalendarDate.getMonth() - 1), timeSystem);
            }
            case YEAR -> {
                if (!down) yield DateCalculator.fromTicks(ticks + (ticksPerYear * count), timeSystem);
                yield DateCalculator.fromTicks(ticks - (ticksPerYear * count), timeSystem);
            }
            default -> venturaCalendarDate;
        };
    }

    public long getDayOfWeek(VenturaCalendarDate venturaCalendarDate) {
        if (venturaCalendarDate == null) return 0L;

        venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);

        if (venturaCalendarDate.getMonth() != 6) {
            venturaCalendarDate.setDay(venturaCalendarDate.getDay() + 1);
        }

        int cc = (int) (venturaCalendarDate.getYear() / 100);
        int yy = (int) (venturaCalendarDate.getYear() - ((venturaCalendarDate.getYear() / 100) * 100));

        int c = (cc / 4) - 2 * cc - 1;
        int y = 5 * yy / 4;
        int m = (int) (26 * (venturaCalendarDate.getMonth() + 1) / 10);
        int d = (int) venturaCalendarDate.getDay();

        return (int) ((c + y + m + d) % venturaCalendarDate.getTimeSystem().getDaysPerWeek());
    }
}
