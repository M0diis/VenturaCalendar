package me.M0dii.venturacalendar.base.utils;

import me.M0dii.venturacalendar.base.dateutils.Date;
import me.M0dii.venturacalendar.base.dateutils.DateUtils;
import me.M0dii.venturacalendar.base.dateutils.TimeSystem;
import me.M0dii.venturacalendar.game.config.Messages;
import me.M0dii.venturacalendar.VenturaCalendar;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Utils
{
    private static final VenturaCalendar plugin = VenturaCalendar.getInstance();
    
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])");
    
    public static String format(String text)
    {
        if(text == null || text.isEmpty())
            return "";
        
        return ChatColor.translateAlternateColorCodes('&',
                HEX_PATTERN.matcher(text).replaceAll("&x&$1&$2&$3&$4&$5&$6"));
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
        return replacePlaceholder(message, date, papi, null);
    }
    
    public static String replacePlaceholder(String message, Date date, Player p)
    {
        return replacePlaceholder(message, date, true, p);
    }
    
    public static String replacePlaceholder(String message, Date date, boolean papi, Player p)
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
            .replaceAll("%[dD]ay(_|)[nN]ame%", date.getDayName())
            .replaceAll("%[mM]onth(_|)[nN]ame%", date.getMonthName())
            .replaceAll("%[sS]eason(_|)[nN]ame%", date.getSeasonName())
            .replaceAll("%[eE]ra(_|)[nN]ame%", date.getEraName())
            .replaceAll("%[tT]ime[sS]ystem(_|)[nN]ame%", timeSystem.getName())
            .replaceAll("%[tT]ime[sS]ystem(_|)[wW]orld%", timeSystem.getWorldName())
            .replaceAll("%[yY]ears(_|)[pP]assed%", String.valueOf(date.getYear()));
        
        if(papi)
            PlaceholderAPI.setPlaceholders(p, message);
        
        return message;
    }
}
