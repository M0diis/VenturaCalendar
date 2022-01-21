package me.m0dii.venturacalendar.base.dateutils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthEvent
{
    public enum DisplayType
    {
        PASSED,
        CURRENT,
        FUTURE
    }
    
    private final String eventName;
    private final String eventDisplayName;
    private final String monthName;
    
    private final List<String> description;
    private final FromTo eventDays;
    
    private final List<String> dayNames;
    
    private final Map<DisplayType, Material> display;
    private final List<String> commands;
    
    public MonthEvent(String eventDisplayName, String monthName, String eventName, FromTo eventDays, List<String> description,
                      List<String> commands)
    {
        this.eventDisplayName = eventDisplayName;
        this.eventName = eventName;
        this.monthName = monthName;
        this.eventDays = eventDays;
        this.description = description;
        this.commands = commands;
        
        this.display = new HashMap<>();
        this.dayNames = new ArrayList<>();
    }
    
    public void putDisplay(DisplayType type, Material material)
    {
        this.display.put(type, material);
    }
    
    public boolean includesDate(Date date)
    {
        return includesMonth(date.getMonthName()) && includesDay((int)date.getDay() + 1);
    }
    
    public boolean includesMonth(String monthName)
    {
        return this.monthName.equalsIgnoreCase(monthName);
    }
    
    public boolean includesDay(int day)
    {
        return eventDays.includes(day);
    }
    
    public Material getDisplay(DisplayType type)
    {
        return this.display.get(type);
    }
    
    public List<String> getCommands()
    {
        return commands;
    }
    
    public List<String> getDescription()
    {
        return description;
    }
    
    public String getMonth()
    {
        return monthName;
    }
    
    public int getStartDay()
    {
        return eventDays.getFrom();
    }
    
    public int getEndDay()
    {
        return eventDays.getTo();
    }
    
    public String getDisplayName()
    {
        return this.eventDisplayName;
    }
    
    public String getName()
    {
        return this.eventName;
    }
    
    public void addDayName(String dayName)
    {
        this.dayNames.add(dayName);
    }
    
    public List<String> getDayNames()
    {
        return dayNames;
    }
    
    public boolean hasDayNames()
    {
        return !dayNames.isEmpty();
    }
    
    public boolean hasFromTo()
    {
        return this.eventDays != null;
    }
    
    public boolean includesDayName(Date date)
    {
        return dayNames.stream().anyMatch(name -> name.equalsIgnoreCase(date.getDayName()))
                && date.getMonthName().equalsIgnoreCase(monthName);
    }
}
