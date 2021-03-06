package me.m0dii.venturacalendar.base.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.Date;
import me.m0dii.venturacalendar.base.dateutils.DateUtils;
import me.m0dii.venturacalendar.base.dateutils.MonthEvent;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
    
    public static String setPlaceholders(String message, Date date, boolean papi)
    {
        return setPlaceholders(message, date, papi, null);
    }
    
    public static String setPlaceholders(String message, Date date, Player p)
    {
        return setPlaceholders(message, date, true, p);
    }
    
    public static String setPlaceholders(String message, Date date, boolean papi, Player p)
    {
        DateUtils du = plugin.getDateUtils();
        
        date = new Date(date);
        TimeSystem timeSystem = new TimeSystem(date.getTimeSystem());
        date = du.addZeroPoints(date);
        
        message = message
            .replaceAll("%[tT]ick(|s)%", String.valueOf(date.getTick()))
            .replaceAll("%[sS]econd(|s)%", String.valueOf(date.getSecond()))
            .replaceAll("%[mM]inute(|s)%", String.valueOf(date.getMinute()))
            .replaceAll("%[hH]our(|s)%", String.valueOf(date.getHour()))
            .replaceAll("%[dD]ay(|s)%", String.valueOf(date.getDay()))
            .replaceAll("%[wW]eek(|s)%", String.valueOf(date.getWeek()))
            .replaceAll("%[mM]onth(|s)%", String.valueOf(date.getMonth()))
            .replaceAll("%[yY]ear(|s)%", String.valueOf(date.getYear()))
            .replaceAll("%[eE]ra(|s)%", String.valueOf(date.getEra()));
        
        date = du.removeZeroPoints(date);
    
        String eventName = "", eventDesc = "";
        
        for(MonthEvent event : plugin.getEventConfig().getEvents())
        {
            if(event.hasFromTo())
            {
                if(event.includesDate(date))
                {
                    eventName = event.getDisplayName();
                    eventDesc = String.join("\n", event.getDescription());
                }
            }
            
            if(event.hasDayNames())
            {
                if(event.includesDayName(date))
                {
                    eventName = event.getDisplayName();
                    eventDesc = String.join("\n", event.getDescription());
                }
            }

        }
        
        message = message
            .replaceAll("%[dD]ay(_|)[nN]ame%", date.getDayName())
            .replaceAll("%[eE]vent(_|)[nN]ame%", eventName)
            .replaceAll("%[eE]vent(_|)[dD]escription%", eventDesc)
            .replaceAll("%[mM]onth(_|)[nN]ame%", date.getMonthName())
            .replaceAll("%[sS]eason(_|)[nN]ame%", date.getSeasonName())
            .replaceAll("%[eE]ra(_|)[nN]ame%", date.getEraName())
            .replaceAll("%[tT]ime[sS]ystem(_|)[nN]ame%", timeSystem.getName())
            .replaceAll("%[tT]ime[sS]ystem(_|)[wW]orld%", timeSystem.getWorldName())
            .replaceAll("%[yY]ears(_|)[pP]assed%", String.valueOf(date.getYear()));
        
        if(papi && plugin.papiEnabled())
            PlaceholderAPI.setPlaceholders(p, message);
        
        return message;
    }
    
    public static void sendCommand(Player player, String cmd)
    {
        cmd = cmd.replaceAll("%([pP]layer|[pP]layer(_|.*)[nN]ame)%", player.getName());
        
        if(plugin.papiEnabled())
        {
            cmd = PlaceholderAPI.setPlaceholders(player, cmd);
        }
        
        cmd = format(cmd);
        
        if(cmd.startsWith("["))
        {
            String sendAs = cmd.substring(cmd.indexOf("["), cmd.indexOf("]") + 1);
    
            cmd = cmd.substring(cmd.indexOf("]") + 2);
    
            if(sendAs.equalsIgnoreCase("[MESSAGE]") || sendAs.equalsIgnoreCase("[TEXT]"))
            {
                player.sendMessage(cmd);
            }
            else if(sendAs.equalsIgnoreCase("[TITLE]"))
            {
                String[] split = cmd.split(", ");
    
                int fadeIn = 20;
                int stay = 60;
                int fadeOut = 20;
                
                if(split.length == 1)
                {
                    String title = split[0];
                    
                    player.sendTitle(title, "", fadeIn, stay, fadeOut);
                    
                    return;
                }
    
                if(split.length == 2)
                {
                    String title = split[0];
                    String subtitle = split[1];
        
                    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        
                    return;
                }
                
                if(split.length == 4)
                {
                    try
                    {
                        fadeIn = Integer.parseInt(split[1]);
                        stay = Integer.parseInt(split[2]);
                        fadeOut = Integer.parseInt(split[3]);
                    }
                    catch(NumberFormatException ex)
                    {
                        Messenger.log(Messenger.Level.WARN, "Invalid fade-in, stay, or fade-out time for title action.");
                        Messenger.log(Messenger.Level.DEBUG, ex.getMessage());
                    }
        
                    player.sendTitle(split[0], "", fadeIn, stay, fadeOut);
        
                    return;
                }

                if(split.length == 5)
                {
                    String subtitle = split[1];
                    
                    try
                    {
                        fadeIn = Integer.parseInt(split[2]);
                        stay = Integer.parseInt(split[3]);
                        fadeOut = Integer.parseInt(split[4]);
                    }
                    catch(NumberFormatException ex)
                    {
                        Messenger.log(Messenger.Level.WARN, "Invalid fadeIn, stay, or fadeOut time for title action.");
                        Messenger.log(Messenger.Level.DEBUG, ex.getMessage());
                    }
                    
                    player.sendTitle(split[0], subtitle, fadeIn, stay, fadeOut);
                }
            }
            else if(sendAs.equalsIgnoreCase("[CHAT]"))
            {
                player.chat(cmd);
            }
            else if(sendAs.equalsIgnoreCase("[SOUND]"))
            {
                String[] split = cmd.split(", ");
                
                if(split.length == 2)
                {
                    try
                    {
                        player.playSound(player.getLocation(), Sound.valueOf(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[1]));
                    }
                    catch (Exception ex)
                    {
                        Messenger.log(Messenger.Level.WARN, "Invalid sound format: " + cmd);
                        Messenger.log(Messenger.Level.DEBUG, ex.getMessage());
                    }
                }
            }
            else if(sendAs.equalsIgnoreCase("[PLAYER]"))
            {
                Bukkit.dispatchCommand(player, cmd);
            }
            else if(sendAs.equalsIgnoreCase("[CONSOLE]"))
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }
        else Bukkit.dispatchCommand(player, cmd);
    }

    public static int getTicksFromTime(String time)
    {
        int value = 0;
        
        try
        {
            value = Integer.parseInt(time.substring(0, time.length() - 1));
        }
        catch(NumberFormatException ex)
        {
            Messenger.log(Messenger.Level.DEBUG, "Invalid time format: " + time);
        }
        
        int ticksPerSecond = (int)plugin.getTimeConfig().getTimeSystem().getTicksPerSecond();
    
        return switch(time.charAt(time.length() - 1))
        {
            case 's' -> value * ticksPerSecond;
            case 'm' -> value * 60 * ticksPerSecond;
            case 'h' -> value * 3600 * ticksPerSecond;
            case 'd' -> value * 86400 * ticksPerSecond;
            default -> value;
        };
    }
}
