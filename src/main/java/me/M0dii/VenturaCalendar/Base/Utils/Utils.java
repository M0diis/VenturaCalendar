package me.M0dii.VenturaCalendar.Base.Utils;

import me.M0dii.VenturaCalendar.Base.DateUtils.Date;
import me.M0dii.VenturaCalendar.Base.DateUtils.DateUtils;
import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils
{
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
        sender.sendMessage(VenturaCalendar.getBaseConfig().getMessage(msg));
    }
    
    public static String replacePlaceholder(String message, Date date, boolean papi)
    {
        DateUtils du = VenturaCalendar.getDateUtils();
        
        date = new Date(date);
        TimeSystem timeSystem = new TimeSystem(date.getTimeSystem());
        date = du.addZero(date);
        
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
        
        date = du.removeZero(date);
        long dayOfWeek = du.getDayOfWeek(date);
        
        message = message
            .replaceAll("%[dD]ay[nN]ame%", timeSystem.getDayNames().get((int) dayOfWeek))
            .replaceAll("%[mM]onth[nN]ame%", date.getMonthName())
            .replaceAll("%[eE]ra[nN]ame%", date.getEraName());
        
        if(papi) PlaceholderAPI.setPlaceholders(null, message);
        
        return message;
    }
}
