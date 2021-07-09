package me.M0dii.VenturaCalendar.Base.Utils;

import me.M0dii.VenturaCalendar.Base.DateUtils.Date;
import me.M0dii.VenturaCalendar.Base.DateUtils.DateUtils;
import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
    
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
    
    DateUtils du = VenturaCalendar.getDateUtils();
    
    @Override
    public String onRequest(OfflinePlayer player, String id)
    {
        TimeSystem ts = VenturaCalendar.getTimeConfig().getTimeSystems().get("default");
        World w = Bukkit.getWorld(ts.getWorldName());
        
        if(w == null)
            return null;
    
        Date date = VenturaCalendar.getDateCalculator().fromTicks(w.getFullTime(), ts);
        long dow = du.getDayOfWeek(date);
    
        switch(id)
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
            case "date_era":
                return String.valueOf(date.getEra());
            case "date_day_name":
                return String.valueOf(ts.getDayNames().get((int)dow));
            case "date_month_name":
                return String.valueOf(ts.getMonthNames().get((int)date.getMonth()));
            case "date_era_name":
                return String.valueOf(ts.getMonthNames().get((int)date.getEra()));
            case "date_day_of_week":
                return String.valueOf(dow);
        }
       
        
        return null;
    }
}