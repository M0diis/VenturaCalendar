package me.M0dii.VenturaCalendar;

import me.M0dii.VenturaCalendar.Base.ConfigUitls.TimeConfig;
import me.M0dii.VenturaCalendar.Base.DateUtils.*;
import me.M0dii.VenturaCalendar.Base.Utils.Placeholders;
import me.M0dii.VenturaCalendar.Base.Utils.UpdateChecker;
import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.Game.Config.CalendarConfig;
import me.M0dii.VenturaCalendar.Game.Config.CommandConfig;
import me.M0dii.VenturaCalendar.Game.GUI.Storage;
import me.M0dii.VenturaCalendar.Game.GUI.StorageUtils;
import me.M0dii.VenturaCalendar.Game.Listeners.Commands.CmdExecutor;
import me.M0dii.VenturaCalendar.Game.Listeners.Inventory.InventoryCaller;
import net.kyori.adventure.text.Component;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.CustomChart;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VenturaCalendar extends JavaPlugin {
    
    public static VenturaCalendar instance;
    
    public String getSpigotLink()
    {
        return "https://www.spigotmc.org/resources/venturacalendar-your-own-custom-calendar.94096/";
    }
    
    public static HashMap<Player, Storage> storages = new HashMap<>();
    
    private static Placeholders placeholders;
    
    private static DateCalculator dateCalculator;
    private static DateUtils dateUtils;
    private static TimeSystemUtils timeSystemUtils;
    
    private static StorageUtils storageUtils;
    
    private static TimeConfig timeConfig;
    private static CalendarConfig calendarConfig;
    private static CommandConfig cconfig;
    
    public static String PREFIX;
    
    public void onEnable()
    {
        instance = this;
        
        registerObjects();
        registerCommands();
        registerEvents();
        
        setupMetrics();
    
        checkForUpdates();
        
        PREFIX = cconfig.getString("messages.prefix");
        
        this.getLogger().info("VenturaCalendar has been enabled.");
    
        checkNewDay();
    }
    
    private void checkForUpdates()
    {
        new UpdateChecker(this, 94096).getVersion(ver ->
        {
            String curr = this.getDescription().getVersion();
            
            if (!curr.equalsIgnoreCase(
                    ver.replace("v", "")))
            {
                getLogger().info("You are running an outdated version of M0-CoreCord.");
                getLogger().info("Latest version: " + ver + ", you are using: " + curr);
                getLogger().info("You can download the latest version on Spigot:");
                getLogger().info(getSpigotLink());
            }
        });
    }
    
    private void setupMetrics()
    {
        Metrics metrics = new Metrics(this, 11985);
        
        CustomChart c = new MultiLineChart("players_and_servers", () ->
        {
            Map<String, Integer> valueMap = new HashMap<>();
            
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            
            return valueMap;
        });
        
        metrics.addCustomChart(c);
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
                
                Date date = getDateCalculator().fromTicks(w.getFullTime(), ts);
        
                if(time >= 0 && time <= 200 && !newDay)
                {
                    if(cconfig.getBoolean("new-day.message.enabled"))
                    {
                        String msg = Utils.replacePlaceholder(cconfig.getNewDayMessage(), date);
                        
                        w.sendMessage(Component.text(msg));
                    }
                    
                    if(cconfig.getBoolean("new-day.title.enabled"))
                    {
                        String title = cconfig.getString("new-day.title.text");
                        String subtitle = cconfig.getString("new-day.title.subtitle");
                        
                        int fadein = cconfig.getInteger("new-day.title.fade-in");
                        int stay = cconfig.getInteger("new-day.title.stay");
                        int fadeout = cconfig.getInteger("new-day.title.fade-out");
                        
                        title = Utils.replacePlaceholder(title, date);
                        
                        subtitle = Utils.replacePlaceholder(subtitle, date);
                        
                        for(Player p : Bukkit.getOnlinePlayers())
                            p.sendTitle(title, subtitle, fadein, stay, fadeout);
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
        placeholders = new Placeholders();
        placeholders.register();
        
        dateUtils = new DateUtils();
        timeSystemUtils = new TimeSystemUtils();
        storageUtils = new StorageUtils();
        
        dateCalculator = new DateCalculator();
        
        timeConfig = new TimeConfig();
        calendarConfig = new CalendarConfig();
        cconfig = new CommandConfig(this);
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
    
    public static CommandConfig getCConfig()
    {
        if(cconfig == null)
            cconfig = new CommandConfig(instance);
        
        return cconfig;
    }
    
}
