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
import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;

public class VenturaCalendar extends JavaPlugin implements Listener {
    private static final String SPIGOT_URL = "https://www.spigotmc.org/resources/99128/";
    public static boolean debug = false;
    public static String PREFIX;
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
    private final Map<UUID, String> newDayWorldDates = new HashMap<>();
    private final Set<BukkitRunnable> fastForwardTasks = new HashSet<>();

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

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            boolean sync = Boolean.TRUE.equals(getTimeConfig().getBoolean("main-time-system.real-time.sync"));
            RealTimeDate now = DateCalculator.realTimeNow();
            long time = Math.floorMod(
                    (now.getHour() * 3600L) + (now.getMinute() * 60L) + now.getSecond() - (6L * 3600L),
                    24L * 3600L
            ) * 24000L / (24L * 3600L);

            for (World world : getTimeSystemWorlds(timeSystem)) {
                world.setGameRule(GameRules.ADVANCE_TIME, !sync);

                if (sync) {
                    world.setTime(time);
                }
            }
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

        Bukkit.getScheduler().runTaskTimer(this, () ->
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

            if (timeSystem.isRealTime()) {
                RealTimeDate realTime = DateCalculator.realTimeNow();

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendActionBar(Utils.setPlaceholders(msgOpt.get(), realTime, p));
                }
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    World world = getTimeSystemWorld(p);

                    if (world == null) {
                        continue;
                    }

                    VenturaCalendarDate venturaCalendarDate = DateCalculator.fromTicks(world.getFullTime(), timeSystem);
                    p.sendActionBar(Utils.setPlaceholders(msgOpt.get(), venturaCalendarDate, p));
                }
            }
        }, 0L, 20L);
    }

    private void checkForUpdates() {
        new UpdateChecker(this, 99128).getVersion(ver ->
        {
            String curr = this.getPluginMeta().getVersion();

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
        Bukkit.getScheduler().runTaskTimer(this, () ->
        {
            TimeSystem ts = getTimeConfig().getTimeSystem();

            Set<UUID> configuredWorlds = new HashSet<>();

            for (World world : getTimeSystemWorlds(ts)) {
                configuredWorlds.add(world.getUID());

                VenturaCalendarDate date = DateCalculator.fromTicks(world.getFullTime(), ts);
                String dateKey = ts.isRealTime()
                        ? "real:" + DateCalculator.realTimeNow().getLocalDateTime().toLocalDate()
                        : date.getYear() + ":" + date.getMonth() + ":" + date.getDay();
                String previousDateKey = newDayWorldDates.put(world.getUID(), dateKey);

                if (previousDateKey != null && !previousDateKey.equals(dateKey)) {
                    Bukkit.getPluginManager().callEvent(new NewDayEvent(ts, world, date));
                }
            }

            newDayWorldDates.keySet().retainAll(configuredWorlds);

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

        for (String cmd : Arrays.asList("calendar", "venturacalendar")) {
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

        if (pAPI != null && pAPI.isEnabled() && placeholders != null) {
            placeholders.unregister();
        }

        cancelFastForwardTasks();

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

    public World getTimeSystemWorld(Player player) {
        TimeSystem timeSystem = getTimeConfig().getTimeSystem();
        String worldName = timeSystem.getWorldName();

        if (worldName != null && worldName.equalsIgnoreCase("current")) {
            return player == null ? null : player.getWorld();
        }

        return worldName == null ? null : Bukkit.getWorld(worldName);
    }

    private List<World> getTimeSystemWorlds(TimeSystem timeSystem) {
        if (timeSystem.getWorldName() != null && timeSystem.getWorldName().equalsIgnoreCase("current")) {
            return Bukkit.getWorlds();
        }

        World world = timeSystem.getWorldName() == null ? null : Bukkit.getWorld(timeSystem.getWorldName());
        return world == null ? List.of() : List.of(world);
    }

    public String getRewardDateKey(Player player) {
        TimeSystem timeSystem = getTimeConfig().getTimeSystem();

        if (timeSystem.isRealTime()) {
            return "real:" + DateCalculator.realTimeNow().getLocalDateTime().toLocalDate();
        }

        World world = getTimeSystemWorld(player);

        if (world == null) {
            return null;
        }

        VenturaCalendarDate date = DateCalculator.fromTicks(world.getFullTime(), timeSystem);
        return "game:" + world.getUID() + ":" + date.getYear() + ":" + date.getMonth() + ":" + date.getDay();
    }

    public void trackFastForwardTask(BukkitRunnable task) {
        fastForwardTasks.add(task);
    }

    public void untrackFastForwardTask(BukkitRunnable task) {
        fastForwardTasks.remove(task);
    }

    public void cancelFastForwardTasks() {
        fastForwardTasks.forEach(BukkitRunnable::cancel);
        fastForwardTasks.clear();
    }
}
