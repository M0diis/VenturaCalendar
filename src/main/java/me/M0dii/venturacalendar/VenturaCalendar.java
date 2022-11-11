package me.m0dii.venturacalendar;

import me.m0dii.venturacalendar.base.configutils.TimeConfig;
import me.m0dii.venturacalendar.base.dateutils.*;
import me.m0dii.venturacalendar.base.dateutils.realtime.RealTimeCalculator;
import me.m0dii.venturacalendar.base.dateutils.realtime.RealTimeDate;
import me.m0dii.venturacalendar.base.events.NewDayEvent;
import me.m0dii.venturacalendar.base.utils.*;
import me.m0dii.venturacalendar.game.config.BaseConfig;
import me.m0dii.venturacalendar.game.config.CalendarConfig;
import me.m0dii.venturacalendar.game.config.EventConfig;
import me.m0dii.venturacalendar.game.gui.Storage;
import me.m0dii.venturacalendar.game.gui.StorageUtils;
import me.m0dii.venturacalendar.game.listeners.EventDayListener;
import me.m0dii.venturacalendar.game.listeners.NewDayListener;
import me.m0dii.venturacalendar.game.listeners.commands.CmdExecutor;
import me.m0dii.venturacalendar.game.listeners.inventory.*;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.CustomChart;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VenturaCalendar extends JavaPlugin implements Listener {
    private static VenturaCalendar instance;

    public static VenturaCalendar getInstance() {
        return instance;
    }

    public static boolean debug = false;

    public String getSpigotLink() {
        return "https://www.spigotmc.org/resources/99128/";
    }

    public static HashMap<Player, Storage> storages = new HashMap<>();

    public static String PREFIX;

    private DateCalculator dateCalculator;
    private DateUtils dateUtils;
    private TimeSystemUtils timeSystemUtils;

    private StorageUtils storageUtils;

    private TimeConfig timeConfig;
    private CalendarConfig calendarConfig;
    private EventConfig eventConfig;
    private BaseConfig baseConfig;

    private boolean papiEnabled = false;

    public void onEnable() {
        instance = this;

        registerObjects();
        registerCommands();
        registerListeners();

        setupMetrics();

        newDayCheckTimer();

        Messenger.log(Messenger.Level.INFO, "VenturaCalendar has been successfully enabled!");

        actionbar();

        syncDaylight();

        if (baseConfig.updateCheck()) {
            checkForUpdates();
        }
    }

    private void syncDaylight() {
        if(getTimeConfig().getBoolean("main-tyme-system.real-time.sync")) {
            for (World world : Bukkit.getWorlds()) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }
        } else {
            for (World world : Bukkit.getWorlds()) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            }
        }

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for(World world : Bukkit.getWorlds()) {
                RealTimeDate now = RealTimeCalculator.now();

                world.setTime(24000 - (now.getHour() * 1000 + now.getMinute() * 1000 / 60));
            }
        }, 0L, 20L);
    }

    private void actionbar() {
        if (getBaseConfig().getActionBarMessage().isEmpty()) {
            return;
        }

        if(!getBaseConfig().getBoolean("action-bar.enabled")) {
            return;
        }

        if (!Version.serverIsNewerThan(Version.v1_11_R1)) {
            return;
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
        {
            if (getBaseConfig().getActionBarMessage().isEmpty()) {
                return;
            }

            if (!Version.serverIsNewerThan(Version.v1_11_R1)) {
                return;
            }

            Optional<String> msgOpt = getBaseConfig().getActionBarMessage();

            if (msgOpt.isEmpty()) {
                Messenger.log(Messenger.Level.DEBUG, "Action bar message is empty.");

                return;
            }

            TimeSystem timeSystem = getTimeConfig().getTimeSystem();

            World world = Bukkit.getWorld(timeSystem.getWorldName());

            if (timeSystem.isRealTime()) {
                RealTimeDate realTime = RealTimeCalculator.now();

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendActionBar(Utils.setPlaceholders(msgOpt.get(), realTime, p));
                }
            }
            else if (world != null) {
                Date date = getDateCalculator().fromTicks(world.getFullTime(), timeSystem);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendActionBar(Utils.setPlaceholders(msgOpt.get(), date, p));
                }
            }
        }, 0L, 20L);
    }

    private void checkForUpdates() {
        new UpdateChecker(this, 99128).getVersion(ver ->
        {
            String curr = this.getDescription().getVersion();

            if (!curr.equalsIgnoreCase(ver)) {
                getLogger().info("You are running an outdated version of VenturaCalendar.");
                getLogger().info("Latest version: " + ver + ", you are using: " + curr);
                getLogger().info("You can download the latest version on Spigot:");
                getLogger().info(getSpigotLink());
            }
        });
    }

    private void setupMetrics() {
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

    private void newDayCheckTimer() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
        {
            TimeSystem ts = getTimeConfig().getTimeSystem();

            World w = Bukkit.getWorld(ts.getWorldName());

            if (w != null && w.getTime() >= 0 && w.getTime() <= 200 && !newDay) {
                newDay = true;

                Bukkit.getScheduler().runTask(this, () -> {
                    Bukkit.getPluginManager().callEvent(new NewDayEvent(ts));
                });
            }

            if (w != null && w.getTime() > 200) {
                newDay = false;
            }

        }, 0L, 90L);
    }

    private void registerObjects() {
        baseConfig = new BaseConfig(this);

        Plugin pAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (pAPI != null && pAPI.isEnabled()) {
            Placeholders placeholders = new Placeholders(this);
            placeholders.register();

            papiEnabled = true;
        }

        dateUtils = new DateUtils(this);
        timeSystemUtils = new TimeSystemUtils();
        storageUtils = new StorageUtils();

        dateCalculator = new DateCalculator(this);

        timeConfig = new TimeConfig(this);
        calendarConfig = new CalendarConfig(this);
        eventConfig = new EventConfig(this);
    }

    private void registerCommands() {
        CmdExecutor commandCaller = new CmdExecutor(this);

        for (String cmd : Arrays.asList("calendar", "date", "venturacalendar")) {
            PluginCommand pcmd = getCommand(cmd);

            if (pcmd != null)
                pcmd.setExecutor(commandCaller);
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new NewDayListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EventDayListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CalendarClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CalendarCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new RealTimeCalendarClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RealTimeCalendarCloseListener(), this);
    }

    public void onDisable() {
        instance = null;

        getLogger().info("VenturaCalendar has been disabled.");
    }

    public DateCalculator getDateCalculator() {
        if (dateCalculator == null) {
            dateCalculator = new DateCalculator(this);
        }

        return dateCalculator;
    }

    public DateUtils getDateUtils() {
        if (dateUtils == null)
            dateUtils = new DateUtils(this);

        return dateUtils;
    }

    public TimeSystemUtils getTimeSystemUtils() {
        if (timeSystemUtils == null) {
            timeSystemUtils = new TimeSystemUtils();
        }

        return timeSystemUtils;
    }

    public StorageUtils getStorageUtils() {
        if (storageUtils == null) {
            storageUtils = new StorageUtils();
        }

        return storageUtils;
    }

    public TimeConfig getTimeConfig() {
        if (timeConfig == null) {
            timeConfig = new TimeConfig(this);
        }

        return timeConfig;
    }

    public CalendarConfig getCalendarConfig() {
        if (calendarConfig == null) {
            calendarConfig = new CalendarConfig(this);
        }

        return calendarConfig;
    }

    public BaseConfig getBaseConfig() {
        if (baseConfig == null) {
            baseConfig = new BaseConfig(this);
        }

        return baseConfig;
    }

    public boolean papiEnabled() {
        return papiEnabled;
    }

    public EventConfig getEventConfig() {
        if (eventConfig == null) {
            eventConfig = new EventConfig(this);
        }

        return eventConfig;
    }
}
