package me.m0dii.venturacalendar.base.dateutils;

import org.bukkit.Material;

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
    private final String monthName;
    private final List<String> description;
    private final FromTo eventDays;
    private final Map<DisplayType, Material> display;
    private final List<String> commands;
    
    public MonthEvent(String eventName, String monthName, FromTo eventDays, List<String> description, List<String> commands)
    {
        this.eventName = eventName;
        this.monthName = monthName;
        this.eventDays = eventDays;
        this.description = description;
        this.commands = commands;
        
        this.display = new HashMap<>();
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
    
    public String getName()
    {
        return this.eventName;
    }
}
