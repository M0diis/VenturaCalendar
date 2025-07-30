package me.m0dii.venturacalendar.base.dateutils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.*;

@Getter
@Setter
public class MonthEvent {
    public enum DisplayType {
        PASSED,
        CURRENT,
        FUTURE
    }
    private int priority = -1;

    private final String eventName;
    private final String eventDisplayName;
    private final String monthName;

    private final List<String> description;
    private final EventDays eventDays;

    private final List<String> dayNames;

    private int year = -1;

    private final Map<DisplayType, Material> display;
    private final List<String> commands;

    public MonthEvent(String eventDisplayName,
                      String monthName,
                      String eventName,
                      EventDays eventDays,
                      int year,
                      List<String> description,
                      List<String> commands) {
        this.eventDisplayName = eventDisplayName;
        this.eventName = eventName;
        this.monthName = monthName;
        this.eventDays = eventDays;
        this.year = year;
        this.description = description;
        this.commands = commands;

        this.display = new EnumMap<>(DisplayType.class);
        this.dayNames = new ArrayList<>();
    }

    public void putDisplay(DisplayType type, Material material) {
        this.display.put(type, material);
    }

    public boolean includesDate(VenturaCalendarDate venturaCalendarDate) {
        boolean includesMonth = (monthName.equalsIgnoreCase("any") || monthName.equalsIgnoreCase("all"))
                || this.monthName.equalsIgnoreCase(venturaCalendarDate.getMonthName());

        boolean includesDay = !hasFromTo() || includesDay((int) venturaCalendarDate.getDay() + 1);

        boolean includesYear = this.year == -1 || this.year == venturaCalendarDate.getYear();

        boolean includesDayName = this.dayNames.isEmpty() || this.dayNames.stream()
                .anyMatch(dayName -> dayName.equalsIgnoreCase(venturaCalendarDate.getDayName()));

        return includesMonth && includesDay && includesYear && includesDayName;
    }

    public boolean includesDate(RealTimeDate date) {
        boolean includesMonth = (monthName.equalsIgnoreCase("any") || monthName.equalsIgnoreCase("all"))
                || this.monthName.equalsIgnoreCase(date.getLocalDateTime().getMonth().name());

        boolean includesDay = !hasFromTo() || includesDay(date.getLocalDateTime().getDayOfMonth());

        boolean includesYear = this.year == -1 || this.year == date.getLocalDateTime().getYear();

        boolean includesDayName = this.dayNames.isEmpty() || this.dayNames.stream()
                .anyMatch(dayName -> dayName.equalsIgnoreCase(date.getLocalDateTime().getDayOfWeek().name()));

        return includesMonth && includesDay && includesYear && includesDayName;
    }

    public boolean includesDay(int day) {
        return eventDays.includes(day);
    }

    public Material getDisplay(DisplayType type) {
        return this.display.get(type);
    }

    public String getMonth() {
        return monthName;
    }

    public int getStartDay() {
        return eventDays.getFrom();
    }

    public int getEndDay() {
        return eventDays.getTo();
    }

    public String getDisplayName() {
        return this.eventDisplayName;
    }

    public String getName() {
        return this.eventName;
    }

    public void addDayName(String dayName) {
        this.dayNames.add(dayName);
    }

    public boolean hasFromTo() {
        return this.eventDays != null;
    }
}
