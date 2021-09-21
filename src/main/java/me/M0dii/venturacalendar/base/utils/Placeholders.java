package me.M0dii.venturacalendar.base.utils;

import me.M0dii.venturacalendar.base.dateutils.Date;
import me.M0dii.venturacalendar.base.dateutils.DateCalculator;
import me.M0dii.venturacalendar.base.dateutils.DateUtils;
import me.M0dii.venturacalendar.base.dateutils.TimeSystem;
import me.M0dii.venturacalendar.VenturaCalendar;
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
        TimeSystem ts = plugin.getTimeConfig().getTimeSystems().get("default");
        
        String worldName = ts.getWorldName();
        
        World w = Bukkit.getWorld(worldName);
        
        if(!worldName.equalsIgnoreCase("real-time") && w == null)
            return null;
        
        Date date = null;
    
        DateCalculator dateCalc = plugin.getDateCalculator();
        
        if(worldName.equalsIgnoreCase("real-time"))
            date = dateCalc.fromMillis(ts);
        else if (w != null)
            date = dateCalc.fromTicks(w.getFullTime(), ts);
        
        long dow = du.getDayOfWeek(date);
        
        date = du.addZeroPoints(date);
    
        switch(id.toLowerCase())
        {
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
                return String.valueOf(date.getMonthName());
            case "date_era_name":
                return String.valueOf(date.getEraName());
            case "date_day_of_week":
                return String.valueOf(dow);
        }
        
        return null;
    }
}