package me.M0dii.VenturaCalendar;

import me.M0dii.VenturaCalendar.Base.ConfigUtils.TimeConfig;
import me.M0dii.VenturaCalendar.Base.DateUtils.Date;
import me.M0dii.VenturaCalendar.Base.DateUtils.*;
import me.M0dii.VenturaCalendar.Base.Utils.Placeholders;
import me.M0dii.VenturaCalendar.Base.Utils.UpdateChecker;
import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.Game.Config.BaseConfig;
import me.M0dii.VenturaCalendar.Game.Config.CalendarConfig;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.Game.GUI.Storage;
import me.M0dii.VenturaCalendar.Game.GUI.StorageUtils;
import me.M0dii.VenturaCalendar.Game.Listeners.Commands.CmdExecutor;
import me.M0dii.VenturaCalendar.Game.Listeners.Inventory.InventoryCaller;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.CustomChart;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class VenturaCalendar extends JavaPlugin
{
    
    public static VenturaCalendar instance;
    
    public String getSpigotLink()
    {
        return "https://www.spigotmc.org/resources/venturacalendar-your-own-custom-calendar.94096/";
    }
    
    public static HashMap<Player, Storage> storages = new HashMap<>();
    
    private Placeholders placeholders;
    
    private static DateCalculator dateCalculator;
    private static DateUtils dateUtils;
    private static TimeSystemUtils timeSystemUtils;
    
    private static StorageUtils storageUtils;
    
    private static TimeConfig timeConfig;
    private static CalendarConfig calendarConfig;
    private static BaseConfig cconfig;
    
    private static List<UUID> redeemed;
    
    public static boolean redeem(UUID uuid)
    {
        if(redeemed.contains(uuid))
            return false;
        
        redeemed.add(uuid);
        
        return true;
    }
    
    public static String PREFIX;
    
    public void onEnable()
    {
        redeemed = new ArrayList<>();
        instance = this;
        
        registerObjects();
        registerCommands();
        registerEvents();
        
        setupMetrics();
        
        checkNewDay();
    
        this.getLogger().info("VenturaCalendar has been enabled.");
        
        checkForUpdates();
    }
    
    private void checkForUpdates()
    {
        new UpdateChecker(this, 94096).getVersion(ver ->
        {
            String curr = this.getDescription().getVersion();
            
            if (!curr.equalsIgnoreCase(
                    ver.replace("v", "")))
            {
                getLogger().info("You are running an outdated version of VenturaCalendar.");
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
    
    static boolean newDay = false;
    
    private void checkNewDay()
    {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
        {
            TimeSystem ts = getTimeConfig().getTimeSystems().get("default");

            World w = Bukkit.getWorld(ts.getWorldName());
            
            if(w != null && w.getTime() >= 0 && w.getTime() <= 200 && !newDay)
            {
                Date date = getDateCalculator().fromTicks(w.getFullTime(), ts);
    
                if(cconfig.rewardsEnabled())
                    redeemed.clear();
                
                if(cconfig.newDayMessageEnabled())
                {
                    String msg = Utils.replacePlaceholder(
                            cconfig.getMessage(Messages.NEW_DAY_TEXT), date, false);

                    for(Player p : Bukkit.getOnlinePlayers())
                    {
                        msg = PlaceholderAPI.setPlaceholders(p, msg);
                        
                        p.sendMessage(msg);
    
                        if(cconfig.titleEnabled())
                        {
                            String title = cconfig.getMessage(Messages.TITLE_TEXT);
                            String subtitle = cconfig.getMessage(Messages.SUBTITLE_TEXT);
        
                            int fadein = cconfig.getInteger("new-day.title.fade-in");
                            int stay = cconfig.getInteger("new-day.title.stay");
                            int fadeout = cconfig.getInteger("new-day.title.fade-out");
        
                            title = Utils.replacePlaceholder(title, date, false);
                            subtitle = Utils.replacePlaceholder(subtitle, date, false);
                            
                            title = PlaceholderAPI.setPlaceholders(p, title);
                            subtitle = PlaceholderAPI.setPlaceholders(p, subtitle);
        
                            p.sendTitle(title, subtitle, fadein, stay, fadeout);
                        }
                    }
                }
                
                newDay = true;
            }
            
            if(w != null && w.getTime() > 200)
                newDay = false;
            
        }, 0L, 90L);
    }
    
    private void registerObjects()
    {
        cconfig = new BaseConfig(this);
        
        placeholders = new Placeholders();
        placeholders.register();
        
        dateUtils = new DateUtils();
        timeSystemUtils = new TimeSystemUtils();
        storageUtils = new StorageUtils();
        
        dateCalculator = new DateCalculator();
        
        timeConfig = new TimeConfig();
        calendarConfig = new CalendarConfig(this);
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
        if(dateCalculator == null)
            dateCalculator = new DateCalculator();
        
        return dateCalculator;
    }
    
    public static DateUtils getDateUtils()
    {
        if(dateUtils == null)
            dateUtils = new DateUtils();
        
        return dateUtils;
    }
    
    public static TimeSystemUtils getTimeSystemUtils()
    {
        if(timeSystemUtils == null)
            timeSystemUtils = new TimeSystemUtils();
        
        return timeSystemUtils;
    }
    
    public static StorageUtils getStorageUtils()
    {
        if(storageUtils == null)
            storageUtils = new StorageUtils();
        
        return storageUtils;
    }
    
    public static TimeConfig getTimeConfig()
    {
        if(timeConfig == null)
            timeConfig = new TimeConfig();
        
        return timeConfig;
    }
    
    public static CalendarConfig getCalendarConfig()
    {
        if(calendarConfig == null)
            calendarConfig = new CalendarConfig(instance);
        
        return calendarConfig;
    }
    
    public static BaseConfig getBaseConfig()
    {
        if(cconfig == null)
            cconfig = new BaseConfig(instance);
        
        return cconfig;
    }
    
}
