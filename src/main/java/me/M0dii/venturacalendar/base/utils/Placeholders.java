package me.m0dii.venturacalendar.base.utils;

import me.m0dii.venturacalendar.base.dateutils.*;
import me.m0dii.venturacalendar.VenturaCalendar;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
    
    final VenturaCalendar plugin;
    
    public Placeholders(VenturaCalendar plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public boolean canRegister()
    {
        return true;
    }
    
    @Override
    public @NotNull String getAuthor()
    {
        return "m0dii";
    }
    
    @Override
    public @NotNull String getIdentifier()
    {
        return "venturacalendar";
    }

    @Override
    public @NotNull String getVersion()
    {
        return "1.0.0";
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String id)
    {
        DateUtils du = plugin.getDateUtils();
        TimeSystem ts = plugin.getTimeConfig().getTimeSystem();
        
        String worldName = ts.getWorldName();
        
        World w = Bukkit.getWorld(worldName);
        
        if(w == null)
        {
            return "";
        }
        
        Date date = null;
    
        DateCalculator dateCalc = plugin.getDateCalculator();
        
        if(ts.isRealTime())
            date = dateCalc.fromMillis(ts);
        else
            date = dateCalc.fromTicks(w.getFullTime(), ts);
        
        long dow = du.getDayOfWeek(date);
        
        date = du.addZeroPoints(date);
        
        if(date == null)
        {
            return "";
        }
        
        id = id.toLowerCase();
        
        if(id.startsWith("event_"))
        {
            String eventName = id.split("_")[1];
            
            MonthEvent event = plugin.getEventConfig().getEvent(eventName);
            
            if(event == null)
            {
                return "";
            }
            
            if(id.endsWith("_start"))
            {
                if(event.hasFromTo())
                {
                    return String.valueOf(event.getStartDay());
                }
            }
            
            if(id.endsWith("_end"))
            {
                if(event.hasFromTo())
                {
                    return String.valueOf(event.getEndDay());
                }
            }
            
            if(id.endsWith("_description"))
            {
                return String.join("\n", event.getDescription());
            }
        }
        
        if(id.startsWith("month_") )
        {
            if(id.endsWith("_season"))
            {
                String month = id.split("_")[1];
    
                Month m = ts.getMonth(month);
                
                return m.getSeasonName() == null ? "" : m.getSeasonName();
            }
    
            if(id.endsWith("_days"))
            {
                String month = id.split("_")[1];
                
                Month m = ts.getMonth(month);
                
                return m == null ? "" : String.valueOf(m.getDays());
            }
        }

        switch(id)
        {
            case "newday_message":
                return plugin.getBaseConfig().getNewDayMessage().orElse("");
            case "actionbar_message":
                return plugin.getBaseConfig().getActionBarMessage().orElse("");
            case "date_event_name":
                date = du.removeZeroPoints(date);
                for(MonthEvent event : this.plugin.getEventConfig().getEvents())
                {
                    Messenger.log(Messenger.Level.INFO, event.getMonth() + " " + date.getMonthName());
                    if(event.includesDate(date))
                    {
                        return event.getDisplayName();
                    }
                }
                return "";
            case "date_event_description":
                date = du.removeZeroPoints(date);
                for(MonthEvent event : this.plugin.getEventConfig().getEvents())
                {
                    if(event.includesDate(date))
                    {
                        return String.join("\n", event.getDescription());
                    }
                }
                return "";
            case "date_tick":
                return String.valueOf(date.getTick());
            case "date_second":
                return String.valueOf(date.getSecond());
            case "date_minute":
                return String.valueOf(date.getMinute());
            case "date_hour":
                return String.valueOf(date.getHour());
            case "date_day":
                return String.valueOf(date.getDay());
            case "date_week":
                return String.valueOf(date.getWeek());
            case "date_month":
                return String.valueOf(date.getMonth());
            case "date_year":
                return String.valueOf(date.getYear());
            case "date_years_passed":
                date = du.removeZeroPoints(date);
                return String.valueOf(date.getYear());
            case "date_era":
                return String.valueOf(date.getEra());
            case "date_day_name":
                return String.valueOf(date.getDayName());
            case "date_month_name":
                date = du.removeZeroPoints(date);
                return String.valueOf(date.getMonthName());
            case "date_season_name":
                return String.valueOf(date.getSeasonName());
            case "date_era_name":
                return String.valueOf(date.getEraName());
            case "date_day_of_week":
                return String.valueOf(dow);
        }
        
        return "";
    }
}