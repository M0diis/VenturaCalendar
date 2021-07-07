package me.M0dii.VenturaCalendar;

import me.M0dii.VenturaCalendar.Base.ConfigUitls.TimeConfig;
import me.M0dii.VenturaCalendar.Base.DateUtils.DateCalculator;
import me.M0dii.VenturaCalendar.Base.DateUtils.DateUtils;
import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystemUtils;
import me.M0dii.VenturaCalendar.Base.Utils.MsgUtils;
import me.M0dii.VenturaCalendar.Game.Config.CalendarConfig;
import me.M0dii.VenturaCalendar.Game.Config.CommandConfig;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.Game.GUI.Storage;
import me.M0dii.VenturaCalendar.Game.GUI.StorageUtils;
import me.M0dii.VenturaCalendar.Game.Listeners.Commands.CmdExecutor;
import me.M0dii.VenturaCalendar.Game.Listeners.Inventory.InventoryCaller;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

public class VenturaCalendar extends JavaPlugin {
    
    public static VenturaCalendar instance;
    public static HashMap<Player, Storage> storages = new HashMap<>();
    
    public static String PREFIX = MsgUtils.format("&2[&aVenturaCalendar&2] ");
    
    private static DateCalculator dateCalculator;
    private static DateUtils dateUtils;
    private static TimeSystemUtils timeSystemUtils;
    
    private static StorageUtils storageUtils;
    
    private static TimeConfig timeConfig;
    private static CalendarConfig calendarConfig;
    private static CommandConfig commandConfig;
    
    public void onEnable()
    {
        instance = this;
        
        registerObjects();
        registerCommands();
        registerEvents();
        
        PREFIX = commandConfig.getString("messages.prefix");
        
        this.getLogger().info("VenturaCalendar has been enabled.");
    
        checkNewDay();
    }
    
    boolean newDay = false;
    
    private void checkNewDay()
    {
        TimeSystem ts = getTimeConfig().getTimeSystems().get("default");
        
        World w = Bukkit.getWorld(ts.getWorldName());
        
        if(w != null)
        {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
            {
                long time = w.getTime();
        
                if(time >= 0 && time <= 200 && !newDay)
                {
                    if(commandConfig.getBoolean("new-day.announce"))
                    {
                        w.sendMessage(Component.text(commandConfig.getNewDayMessages().get(Messages.NEW_DAY)));
                        
                        
                    }
                    
                    newDay = true;
                }
                
                if(time > 200)
                    newDay = false;
                
            }, 0L, 100L);
        }
    }
    
    private void registerObjects()
    {
        dateUtils = new DateUtils();
        timeSystemUtils = new TimeSystemUtils();
        storageUtils = new StorageUtils();
        
        dateCalculator = new DateCalculator();
        
        timeConfig = new TimeConfig();
        calendarConfig = new CalendarConfig();
        commandConfig = new CommandConfig();
    }

    private void registerCommands()
    {
        CmdExecutor commandCaller = new CmdExecutor();
    
        for(String cmd : Arrays.asList("calendar", "date", "venturacalendar"))
        {
            PluginCommand pcmd = getCommand(cmd);
            
            if(pcmd != null)
                pcmd.setExecutor(commandCaller);
        }
    
    }

    private void registerEvents()
    {
        Bukkit.getPluginManager().registerEvents(new InventoryCaller(), this);
    }
    
    public void onDisable()
    {
        this.getLogger().info("VenturaCalendar has been disabled.");
    }
    
    public static DateCalculator getDateCalculator()
    {
        return dateCalculator;
    }
    
    public static DateUtils getDateUtils()
    {
        return dateUtils;
    }
    
    public static TimeSystemUtils getTimeSystemUtils()
    {
        return timeSystemUtils;
    }
    
    public static StorageUtils getStorageUtils()
    {
        return storageUtils;
    }
    
    public static TimeConfig getTimeConfig()
    {
        return timeConfig;
    }
    
    public static CalendarConfig getCalendarConfig()
    {
        return calendarConfig;
    }
    
    public static CommandConfig getCommandConfig()
    {
        return commandConfig;
    }
    
}
