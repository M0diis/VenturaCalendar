package me.m0dii.venturacalendar;

import lombok.Getter;
import me.m0dii.venturacalendar.base.configutils.TimeConfig;
import me.m0dii.venturacalendar.base.dateutils.*;
import me.m0dii.venturacalendar.base.events.NewDayEvent;
import me.m0dii.venturacalendar.base.utils.*;
import me.m0dii.venturacalendar.game.commands.CmdExecutor;
import me.m0dii.venturacalendar.game.config.BaseConfig;
import me.m0dii.venturacalendar.game.config.CalendarConfig;
import me.m0dii.venturacalendar.game.config.EventConfig;
import me.m0dii.venturacalendar.game.listeners.EventDayListener;
import me.m0dii.venturacalendar.game.listeners.NewDayListener;
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
    private static final String SPIGOT_URL = "https://www.spigotmc.org/resources/99128/";
    public static boolean debug = false;
    public static String PREFIX;
    static boolean newDay = false;
    @Getter
    private static VenturaCalendar instance;
    private Placeholders placeholders;
    private DateUtils dateUtils;
    private TimeSystemUtils timeSystemUtils;
    private TimeConfig timeConfig;
    private CalendarConfig calendarConfig;
    private EventConfig eventConfig;
    private BaseConfig baseConfig;
    private boolean papiEnabled = false;

    @Override
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
        TimeSystem timeSystem = getTimeConfig().getTimeSystem();

        if (!timeSystem.isRealTime()) {
            return;
        }

        World w = Bukkit.getWorld(timeSystem.getWorldName());

        if (w == null) {
            return;
        }

        w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, !getTimeConfig().getBoolean("main-time-system.real-time.sync"));

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (Boolean.FALSE.equals(getTimeConfig().getBoolean("main-time-system.real-time.sync"))) {
                return;
            }

            RealTimeDate now = DateCalculator.realTimeNow();

            w.setTime(24000 - (now.getHour() * 1000 + now.getMinute() * 1000 / 60));
        }, 0L, 20L);
    }

    private void actionbar() {
        if (getBaseConfig().getActionBarMessage().isEmpty()) {
            return;
        }

        if (Boolean.FALSE.equals(getBaseConfig().getBoolean("action-bar.enabled"))) {
            return;
        }

        if (Version.serverIsOlderThan(Version.v1_11_R1)) {
            return;
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
        {
            if (getBaseConfig().getActionBarMessage().isEmpty()) {
                return;
            }

            if (Version.serverIsOlderThan(Version.v1_11_R1)) {
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
                RealTimeDate realTime = DateCalculator.realTimeNow();

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendActionBar(Utils.setPlaceholders(msgOpt.get(), realTime, p));
                }
            } else if (world != null) {
                VenturaCalendarDate venturaCalendarDate = DateCalculator.fromTicks(world.getFullTime(), timeSystem);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendActionBar(Utils.setPlaceholders(msgOpt.get(), venturaCalendarDate, p));
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
                getLogger().info(SPIGOT_URL);
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

    private void newDayCheckTimer() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () ->
        {
            TimeSystem ts = getTimeConfig().getTimeSystem();

            World w = Bukkit.getWorld(ts.getWorldName());

            if (w != null && w.getTime() >= 0 && w.getTime() <= 200 && !newDay) {
                newDay = true;

                Bukkit.getScheduler().runTask(this, () -> {
                    World world = Bukkit.getWorld(ts.getWorldName());

                    if (world == null) {
                        Messenger.log(Messenger.Level.ERROR, "World '" + ts.getWorldName() + "' not found for new day event.");
                        return;
                    }

                    Bukkit.getPluginManager().callEvent(new NewDayEvent(ts, world, DateCalculator.fromTicks(world.getFullTime(), ts)));
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
            this.placeholders = new Placeholders(this);
            placeholders.register();

            papiEnabled = true;
        }

        dateUtils = new DateUtils(this);
        timeSystemUtils = new TimeSystemUtils();

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

    @Override
    public void onDisable() {
        instance = null;

        Plugin pAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");

        if (pAPI != null && pAPI.isEnabled()) {
            placeholders.unregister();
        }

        getLogger().info("VenturaCalendar has been disabled.");
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
