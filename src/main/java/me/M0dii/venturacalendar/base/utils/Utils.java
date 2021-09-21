package me.M0dii.venturacalendar.base.utils;

import me.M0dii.venturacalendar.base.dateutils.Date;
import me.M0dii.venturacalendar.base.dateutils.DateUtils;
import me.M0dii.venturacalendar.base.dateutils.TimeSystem;
import me.M0dii.venturacalendar.game.config.Messages;
import me.M0dii.venturacalendar.VenturaCalendar;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils
{
    static final VenturaCalendar plugin = VenturaCalendar.getInstance();
    
    public static String format(String text)
    {
        if(text == null || text.isEmpty())
            return "";
        
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public static void sendFormat(CommandSender sender, String msg)
    {
        sender.sendMessage(format(msg));
    }
    
    public static void sendMsg(CommandSender sender, Messages msg)
    {
        sender.sendMessage(plugin.getBaseConfig().getMessage(msg));
    }
    
    public static String replacePlaceholder(String message, Date date, boolean papi)
    {
        DateUtils du = plugin.getDateUtils();
        
        date = new Date(date);
        TimeSystem timeSystem = new TimeSystem(date.getTimeSystem());
        date = du.addZeroPoints(date);
        
        message = message
            .replaceAll("%[tT]ick%", String.valueOf(date.getTick()))
            .replaceAll("%[sS]econd%", String.valueOf(date.getSecond()))
            .replaceAll("%[mM]inute%", String.valueOf(date.getMinute()))
            .replaceAll("%[hH]our%", String.valueOf(date.getHour()))
            .replaceAll("%[dD]ay%", String.valueOf(date.getDay()))
            .replaceAll("%[wW]eek%", String.valueOf(date.getWeek()))
            .replaceAll("%[mM]onth%", String.valueOf(date.getMonth()))
            .replaceAll("%[yY]ear%", String.valueOf(date.getYear()))
            .replaceAll("%[eE]ra%", String.valueOf(date.getEra()));
        
        date = du.removeZeroPoints(date);
        
        message = message
            .replaceAll("%[dD]ay[nN]ame%", date.getDayName())
            .replaceAll("%[mM]onth[nN]ame%", date.getMonthName())
            .replaceAll("%[eE]ra[nN]ame%", date.getEraName())
            .replaceAll("%[tT]ime[sS]ystem[nN]ame", timeSystem.getName())
            .replaceAll("%[tT]ime[sS]ystem[wW]orld", timeSystem.getWorldName())
            .replaceAll("%[yY]ears[pP]assed", String.valueOf(date.getYear()));
        
        if(papi)
            PlaceholderAPI.setPlaceholders(null, message);
        
        return message;
    }
}
