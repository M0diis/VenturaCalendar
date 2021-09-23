package me.M0dii.venturacalendar.base.dateutils;

import org.bukkit.Material;

import java.util.List;

public class MonthEvent
{
    private String eventName;
    private String monthName;
    private List<String> description;
    private FromTo eventDays;
    private Material display;
    
    public MonthEvent(String eventName, String monthName, Material disp, FromTo eventDays, List<String> description)
    {
        this.eventName = eventName;
        this.monthName = monthName;
        this.eventDays = eventDays;
        this.description = description;
        this.display = disp;
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
