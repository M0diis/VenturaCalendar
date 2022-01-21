package me.m0dii.venturacalendar.game.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.Date;
import me.m0dii.venturacalendar.base.dateutils.MonthEvent;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.base.events.MonthEventDayEvent;
import me.m0dii.venturacalendar.base.events.NewDayEvent;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewDayListener implements Listener
{
    private final VenturaCalendar plugin;
    
    public NewDayListener(VenturaCalendar plugin)
    {
        this.plugin = plugin;
    }
    
    private static final List<UUID> redeemed = new ArrayList<>();
    
    public static boolean redeem(UUID uuid)
    {
        if(redeemed.contains(uuid))
            return false;
        
        redeemed.add(uuid);
        
        return true;
    }
    
    @EventHandler
    public void onNewDay(NewDayEvent e)
    {
        if(e.isCancelled())
        {
            return;
        }
        
        TimeSystem ts = e.getTimeSystem();
        
        World w = Bukkit.getWorld(ts.getWorldName());
        
        if(w == null)
        {
            return;
        }
    
        Date date = plugin.getDateCalculator().fromTicks(w.getFullTime(), ts);
    
        if(plugin.getBaseConfig().rewardsEnabled())
            redeemed.clear();
    
        for(MonthEvent event : plugin.getEventConfig().getEvents())
        {
            if(event.hasFromTo())
            {
                if(event.includesDate(date))
                {
                    Bukkit.getPluginManager().callEvent(new MonthEventDayEvent(ts, event));
                }
            }
            else if(event.hasDayNames())
            {
                if(event.includesDayName(date))
                {
                    Bukkit.getPluginManager().callEvent(new MonthEventDayEvent(ts, event));
                }
            }
        }
    
        for(Player p : Bukkit.getOnlinePlayers())
        {
            for(String cmd : plugin.getBaseConfig().getNewDayCommands())
            {
                Utils.sendCommand(p, cmd);
            }
        
            if(plugin.getBaseConfig().getNewDayMessage().isPresent())
            {
                String base = plugin.getBaseConfig().getNewDayMessage().get();
                String msg = Utils.setPlaceholders(base, date, p);
            
                p.sendMessage(msg);
            }
        
            if(plugin.getBaseConfig().titleEnabled())
            {
                String title = plugin.getBaseConfig().getMessage(Messages.TITLE_TEXT);
                String subtitle = plugin.getBaseConfig().getMessage(Messages.SUBTITLE_TEXT);
            
                int fadein = plugin.getBaseConfig().getInteger("new-day.title.fade-in");
                int stay = plugin.getBaseConfig().getInteger("new-day.title.stay");
                int fadeout = plugin.getBaseConfig().getInteger("new-day.title.fade-out");
            
                title = Utils.setPlaceholders(title, date, false);
                subtitle = Utils.setPlaceholders(subtitle, date, false);
            
                if(plugin.papiEnabled())
                {
                    title = PlaceholderAPI.setPlaceholders(p, title);
                    subtitle = PlaceholderAPI.setPlaceholders(p, subtitle);
                }
            
                p.sendTitle(title, subtitle, fadein, stay, fadeout);
            }
        }
    }
}
