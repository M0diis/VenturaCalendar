import me.m0dii.venturacalendar.base.dateutils.DateCalculator;
import me.m0dii.venturacalendar.base.dateutils.Month;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.base.dateutils.VenturaCalendarDate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Main {
    @Test
    public void calculatesConfiguredDayLength() {
        TimeSystem timeSystem = timeSystem(1, 1, 1, 10, List.of(3L, 2L));

        VenturaCalendarDate date = DateCalculator.fromTicks(30, timeSystem);

        assertEquals(1, date.getMonth());
        assertEquals(0, date.getDay());
        assertEquals(0, date.getYear());
    }

    @Test
    public void handlesVariableMonthBoundaries() {
        TimeSystem timeSystem = timeSystem(20, 5, 10, 24, List.of(31L, 28L));
        long ticksPerDay = 20L * 5L * 10L * 24L;

        VenturaCalendarDate date = DateCalculator.fromTicks(ticksPerDay * 31, timeSystem);

        assertEquals(1, date.getMonth());
        assertEquals(0, date.getDay());
    }

    @Test
    public void handlesTicksBeforeTheEpoch() {
        TimeSystem timeSystem = timeSystem(1, 1, 1, 1, List.of(3L, 2L));

        VenturaCalendarDate date = DateCalculator.fromTicks(-1, timeSystem);

        assertEquals(-1, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDay());
    }

    private TimeSystem timeSystem(long ticksPerSecond,
                                  long secondsPerMinute,
                                  long minutesPerHour,
                                  long hoursPerDay,
                                  List<Long> daysPerMonth) {
        List<Month> months = daysPerMonth.stream()
                .map(days -> new Month("Month" + days, days, "Season"))
                .toList();

        return TimeSystem.builder()
                .worldName("world")
                .name("test")
                .ticksPerSecond(ticksPerSecond)
                .secondsPerMinute(secondsPerMinute)
                .minutesPerHour(minutesPerHour)
                .hoursPerDay(hoursPerDay)
                .daysPerWeek(7)
                .daysPerMonth(daysPerMonth)
                .monthsPerYear(months.size())
                .erasBegin(List.of(-100L))
                .erasEnd(List.of(100L))
                .tickZero(0)
                .secondZero(0)
                .minuteZero(0)
                .hourZero(0)
                .dayZero(0)
                .weekZero(0)
                .monthZero(0)
                .yearZero(0)
                .eraZero(0)
                .months(months)
                .dayNames(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"))
                .eraNames(List.of("Test"))
                .build();
    }
}
