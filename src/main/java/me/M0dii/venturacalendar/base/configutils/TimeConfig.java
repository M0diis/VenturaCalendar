package me.m0dii.venturacalendar.base.configutils;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.Month;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.base.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeConfig extends Config implements ConfigUtils {
    final HashMap<String, TimeSystem> timeSystems = new HashMap<>();
    private FileConfiguration cfg;

    public TimeConfig(VenturaCalendar plugin) {
        super(plugin.getDataFolder(), "TimeConfig.yml", plugin);

        cfg = super.loadConfig();

        reload();
    }

    public void set(@NotNull String path, @Nullable Object obj) {
        cfg.set(path, obj);

        saveConfig();
    }

    public TimeSystem getTimeSystem() {
        if (timeSystems.containsKey("main-time-system")) {
            return timeSystems.get("main-time-system");
        } else {
            Messenger.log(Messenger.Level.ERROR, "Timesystem 'main-time-system' was not found in the config.");
        }

        reload();

        return timeSystems.get("main-time-system");
    }

    private void reload() {
        timeSystems.put("main-time-system", loadTimeSystem());
    }

    private TimeSystem loadTimeSystem() {
        String path = "main-time-system.";
        String worldName = getString(path + "world-name");

        if (worldName.isBlank()) {
            worldName = "world";
        }

        boolean realTime = getBoolean(path + "real-time.enabled");

        // Tick
        long tickZero = 0;

        // Second
        long ticksPerSecond = positive(path + "ticks-per-second", 20L);
        long secondZero = 0;

        // Minute
        long secondsPerMinute = positive(path + "seconds-per-minute", 60L);
        long minuteZero = 0;

        // Hour
        long minutesPerHour = positive(path + "minutes-per-hour", 60L);
        long hourZero = 1;

        // Day
        long hoursPerDay = positive(path + "hours-per-day", 24L);
        long dayZero = getLong(path + "day-offset");

        List<String> dayNames = getListString(path + "days");

        // Week
        long daysPerWeek = positive(path + "days-per-week", 7L);
        long weekZero = getLong(path + "week-offset");

        // Month
        List<Month> months = new ArrayList<>();
        List<Long> monthDays = new ArrayList<>();

        for (String month : getListString(path + "months")) {
            String[] split = month.split(",", -1);

            if (split.length != 3) {
                plugin.getLogger().warning("Invalid month format: " + month);
                plugin.getLogger().warning("Expected format: 'Month Name, Days, Season Name'.");
                continue;
            }

            String monthName = split[0].trim();
            long monthDaysCount;
            String seasonName = split[2].trim();

            try {
                monthDaysCount = Long.parseLong(split[1].trim());
            } catch (NumberFormatException ex) {
                plugin.getLogger().warning("Invalid month day count: " + month);
                continue;
            }

            if (monthName.isEmpty() || monthDaysCount <= 0) {
                plugin.getLogger().warning("Invalid month definition: " + month);
                continue;
            }

            Month m = new Month(monthName, monthDaysCount, seasonName);

            monthDays.add(monthDaysCount);

            months.add(m);
        }

        if (months.isEmpty()) {
            throw new IllegalArgumentException("At least one valid month must be configured.");
        }

        long monthZero = getLong(path + "month-offset");

        // Year
        long monthsPerYear = positive(path + "months-per-year", months.size());

        if (monthsPerYear != months.size()) {
            plugin.getLogger().warning("months-per-year does not match the configured month list; using " + months.size() + ".");
            monthsPerYear = months.size();
        }
        long yearZero = getLong(path + "starting-year");

        // Era
        List<String> eraNames = new ArrayList<>();
        List<Long> erasBegin = new ArrayList<>();
        List<Long> erasEnd = new ArrayList<>();

        for (String era : getListString(path + "eras")) {
            String[] split = era.split(",", -1);

            if (split.length != 3) {
                plugin.getLogger().warning("Invalid era format: " + era);
                plugin.getLogger().warning("Expected format: 'Era Name, Begin Year, End Year'.");
                continue;
            }

            String eraName = split[0].trim();
            long eraBegin;
            long eraEnd;

            try {
                eraBegin = Long.parseLong(split[1].trim());
                eraEnd = Long.parseLong(split[2].trim());
            } catch (NumberFormatException ex) {
                plugin.getLogger().warning("Invalid era years: " + era);
                continue;
            }

            if (eraName.isEmpty() || eraEnd < eraBegin) {
                plugin.getLogger().warning("Invalid era definition: " + era);
                continue;
            }

            eraNames.add(eraName);
            erasBegin.add(eraBegin);
            erasEnd.add(eraEnd);
        }

        long eraZero = 1;

        TimeSystem ts = TimeSystem.builder()
                .worldName(worldName)
                .name("main-time-system")
                .ticksPerSecond(ticksPerSecond)
                .secondsPerMinute(secondsPerMinute)
                .minutesPerHour(minutesPerHour)
                .hoursPerDay(hoursPerDay)
                .daysPerWeek(daysPerWeek)
                .daysPerMonth(monthDays)
                .monthsPerYear(monthsPerYear)
                .erasBegin(erasBegin)
                .erasEnd(erasEnd)
                .tickZero(tickZero)
                .secondZero(secondZero)
                .minuteZero(minuteZero)
                .hourZero(hourZero)
                .dayZero(dayZero)
                .weekZero(weekZero)
                .monthZero(monthZero)
                .yearZero(yearZero)
                .eraZero(eraZero)
                .dayNames(dayNames)
                .months(months)
                .eraNames(eraNames)
                .build();

        ts.setRealTime(realTime);

        return ts;
    }

    private long positive(String path, long fallback) {
        long value = getLong(path);

        if (value > 0) {
            return value;
        }

        plugin.getLogger().warning("Invalid or missing value for '" + path + "'; using " + fallback + ".");
        return fallback;
    }

    @Override
    public FileConfiguration reloadConfig() {
        cfg = super.reloadConfig();

        reload();

        return cfg;
    }

    private ArrayList<Object> getSection(String path, String value) {
        ArrayList<Object> names = new ArrayList<>();

        ConfigurationSection section = cfg.getConfigurationSection(path);

        if (section != null) {
            for (String key : section.getKeys(false)) {
                names.add(getString(path + "." + key + "." + value));
            }
        }

        return names;
    }

    @Override
    public String getString(String path) {
        if (path == null) {
            return "";
        }

        String str = cfg.getString(path);

        if (str == null || str.isEmpty()) {
            return "";
        }

        return Utils.format(str);
    }

    @Override
    public Integer getInteger(String path) {
        return cfg.getInt(path);
    }

    @Override
    public Long getLong(String path) {
        return cfg.getLong(path, 0);
    }

    @Override
    public Boolean getBoolean(String path) {
        return cfg.getBoolean(path);
    }

    @Override
    public List<String> getListString(String path) {
        return cfg.getStringList(path).stream()
                .map(Utils::format)
                .toList();
    }
}
