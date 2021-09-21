package me.M0dii.venturacalendar;

import me.M0dii.venturacalendar.base.configutils.TimeConfig;
import me.M0dii.venturacalendar.base.dateutils.Date;
import me.M0dii.venturacalendar.base.dateutils.*;
import me.M0dii.venturacalendar.base.utils.Placeholders;
import me.M0dii.venturacalendar.base.utils.UpdateChecker;
import me.M0dii.venturacalendar.base.utils.Utils;
import me.M0dii.venturacalendar.game.config.BaseConfig;
import me.M0dii.venturacalendar.game.config.CalendarConfig;
import me.M0dii.venturacalendar.game.config.Messages;
import me.M0dii.venturacalendar.game.gui.Storage;
import me.M0dii.venturacalendar.game.gui.StorageUtils;
import me.M0dii.venturacalendar.game.listeners.Commands.CmdExecutor;
import me.M0dii.venturacalendar.game.listeners.Inventory.InventoryCaller;
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
    private static VenturaCalendar instance;
    
    public static VenturaCalendar getInstance()
    {
        return instance;
    }
    
    public String getSpigotLink()
    {
        return "https://www.spigotmc.org/resources/94096/";
    }
    
    public static HashMap<Player, Storage> storages = new HashMap<>();
    
    public static String PREFIX;
    
    private DateCalculator dateCalculator;
    private DateUtils dateUtils;
    private TimeSystemUtils timeSystemUtils;
    
    private StorageUtils storageUtils;
    
    private TimeConfig timeConfig;
    private CalendarConfig calendarConfig;
    private BaseConfig baseConfig;
    
    private List<UUID> redeemed;
    
    public boolean redeem(UUID uuid)
    {
        if(redeemed.contains(uuid))
            return false;
        
        redeemed.add(uuid);
        
        return true;
    }
    
    public void onEnable()
    {
        instance = this;
        
        redeemed = new ArrayList<>();
        
        registerObjects();
        registerCommands();
        registerEvents();
        
        setupMetrics();
        
        checkNewDay();
    
        getLogger().info("VenturaCalendar has been enabled.");
        
        checkForUpdates();
    
        actionbar();
    }
    
    private void actionbar()
    {
        if(getBaseConfig().actionBar().isEmpty())
            return;
    
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
    
            if(getBaseConfig().actionBar().isEmpty())
                return;
            
            String msg = getBaseConfig().actionBar().get();
            
            TimeSystem timeSystem = getTimeConfig().getTimeSystems().get("default");
    
            String wname = timeSystem.getWorldName();
            World world = Bukkit.getWorld(wname);
    
            if(wname.equalsIgnoreCase("real-time"))
            {
                Date date = getDateCalculator().fromMillis(timeSystem);
                
                Bukkit.getOnlinePlayers().forEach(p -> {
                    
                    p.sendActionBar(Utils.replacePlaceholder(msg, date, true));
                    
                });
            }
            else if(world != null)
            {
                Date date = getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
                
                
            }
    
            
    
    
        }, 0L, 20L);
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
    
                if(baseConfig.rewardsEnabled())
                    redeemed.clear();
                
                if(baseConfig.newDayMessageEnabled())
                {
                    String msg = Utils.replacePlaceholder(
                            baseConfig.getMessage(Messages.NEW_DAY_TEXT), date, false);

                    for(Player p : Bukkit.getOnlinePlayers())
                    {
                        msg = PlaceholderAPI.setPlaceholders(p, msg);
                        
                        p.sendMessage(msg);
    
                        if(baseConfig.titleEnabled())
                        {
                            String title = baseConfig.getMessage(Messages.TITLE_TEXT);
                            String subtitle = baseConfig.getMessage(Messages.SUBTITLE_TEXT);
        
                            int fadein = baseConfig.getInteger("new-day.title.fade-in");
                            int stay = baseConfig.getInteger("new-day.title.stay");
                            int fadeout = baseConfig.getInteger("new-day.title.fade-out");
        
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
        baseConfig = new BaseConfig(this);
    
        Placeholders placeholders = new Placeholders(this);
        placeholders.register();
        
        dateUtils = new DateUtils(this);
        timeSystemUtils = new TimeSystemUtils();
        storageUtils = new StorageUtils();
        
        dateCalculator = new DateCalculator();
        
        timeConfig = new TimeConfig(this);
        calendarConfig = new CalendarConfig(this);
    }

    private void registerCommands()
    {
        CmdExecutor commandCaller = new CmdExecutor(this);
    
        for(String cmd : Arrays.asList("calendar", "date", "venturacalendar"))
        {
            PluginCommand pcmd = getCommand(cmd);
            
            if(pcmd != null)
                pcmd.setExecutor(commandCaller);
        }
    }

    private void registerEvents()
    {
        Bukkit.getPluginManager().registerEvents(new InventoryCaller(this), this);
    }
    
    public void onDisable()
    {
        instance = null;
        
        getLogger().info("VenturaCalendar has been disabled.");
    }
    
    public DateCalculator getDateCalculator()
    {
        if(dateCalculator == null)
            dateCalculator = new DateCalculator();
        
        return dateCalculator;
    }
    
    public DateUtils getDateUtils()
    {
        if(dateUtils == null)
            dateUtils = new DateUtils(this);
        
        return dateUtils;
    }
    
    public TimeSystemUtils getTimeSystemUtils()
    {
        if(timeSystemUtils == null)
            timeSystemUtils = new TimeSystemUtils();
        
        return timeSystemUtils;
    }
    
    public StorageUtils getStorageUtils()
    {
        if(storageUtils == null)
            storageUtils = new StorageUtils();
        
        return storageUtils;
    }
    
    public TimeConfig getTimeConfig()
    {
        if(timeConfig == null)
            timeConfig = new TimeConfig(this);
        
        return timeConfig;
    }
    
    public CalendarConfig getCalendarConfig()
    {
        if(calendarConfig == null)
            calendarConfig = new CalendarConfig(this);
        
        return calendarConfig;
    }
    
    public BaseConfig getBaseConfig()
    {
        if(baseConfig == null)
            baseConfig = new BaseConfig(this);
        
        return baseConfig;
    }
}