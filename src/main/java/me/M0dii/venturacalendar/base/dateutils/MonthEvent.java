package me.M0dii.venturacalendar.base.dateutils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MonthEvent
{
    private final String eventName;
    private final String monthName;
    private final List<String> description;
    private final FromTo eventDays;
    private final Material display;
    private final List<String> commands;
    
    public MonthEvent(String eventName, String monthName, Material disp, FromTo eventDays, List<String> description, List<String> commands)
    {
        this.eventName = eventName;
        this.monthName = monthName;
        this.eventDays = eventDays;
        this.description = description;
        this.display = disp;
        this.commands = commands;
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
    
    public Material getDisplay()
    {
        return display;
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
