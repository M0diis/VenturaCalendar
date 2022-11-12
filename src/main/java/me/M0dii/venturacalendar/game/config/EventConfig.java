package me.m0dii.venturacalendar.game.config;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.configutils.Config;
import me.m0dii.venturacalendar.base.configutils.ConfigUtils;
import me.m0dii.venturacalendar.base.dateutils.EventDays;
import me.m0dii.venturacalendar.base.dateutils.Month;
import me.m0dii.venturacalendar.base.dateutils.MonthEvent;
import me.m0dii.venturacalendar.base.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventConfig extends Config implements ConfigUtils {
    private final List<MonthEvent> events;
    private final VenturaCalendar plugin;
    private FileConfiguration cfg;

    public EventConfig(VenturaCalendar plugin) {
        super(plugin.getDataFolder(), "Events.yml", plugin);

        this.plugin = plugin;

        this.cfg = super.loadConfig();

        this.events = new ArrayList<>();

        loadEvents();

        VenturaCalendar.PREFIX = getString("messages.prefix");
    }

    public List<MonthEvent> getEvents() {
        return this.events;
    }

    public MonthEvent getEvent(String name) {
        return this.events.stream().filter(event -> event.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void loadEvents() {
        ConfigurationSection sec = cfg.getConfigurationSection("events");

        this.events.clear();

        if (sec != null) {
            sec.getValues(false).forEach((k, v) ->
            {
                ConfigurationSection eventSection = sec.getConfigurationSection(k);

                if (eventSection == null) {
                    return;
                }

                if (eventSection.contains("months")) {
                    for (String month : eventSection.getStringList("months")) {
                        createEvent(k, eventSection, month);
                    }
                }
                else {
                    String monthName = eventSection.getString("month", "");

                    if (monthName.equalsIgnoreCase("any") || monthName.equalsIgnoreCase("all")) {
                        for (Month month : plugin.getTimeConfig().getTimeSystem().getMonths()) {
                            createEvent(k, eventSection, month.getName());
                        }
                    }
                    else {
                        createEvent(k, eventSection, monthName);
                    }
                }
            });
        }
    }

    private void createEvent(String eventName, ConfigurationSection eventSection, String month) {
        EventDays eventDays = null;

        String eventDisplayName = Utils.format(eventSection.getString("name"));

        if (eventSection.contains("days")) {
            if(eventSection.get("days") instanceof List eventDaysList) {
                eventDays = new EventDays(eventDaysList);
            } else {
                eventDays = new EventDays(eventSection.getInt("days.start"), eventSection.getInt("days.end"));
            }
        }
        else if (eventSection.contains("day")) {
            eventDays = new EventDays(eventSection.getInt("day"), eventSection.getInt("day"));
        }

        List<String> description = eventSection.getStringList("description").stream()
                .map(Utils::format)
                .collect(Collectors.toList());

        String disp = "display-material.";

        Material matCurr = null, matPassed = null, matFuture = null;

        if (eventSection.contains(disp + "current")
         && eventSection.contains(disp + "passed")
         && eventSection.contains(disp + "future")) {
            matCurr = Utils.getMaterial(eventSection.getString("display-material.current", "GLASS_PANE"));
            matPassed = Utils.getMaterial(eventSection.getString("display-material.passed", "GLASS_PANE"));
            matFuture = Utils.getMaterial(eventSection.getString("display-material.future", "GLASS_PANE"));

        }
        else {
            matCurr = Utils.getMaterial(eventSection.getString("display-material", "WHITE_STAINED_GLASS_PANE"));
            matPassed = matCurr;
            matFuture = matCurr;
        }

        List<String> commands = eventSection.getStringList("commands");

        MonthEvent event = new MonthEvent(eventDisplayName, month, eventName, eventDays, description, commands);

        event.putDisplay(MonthEvent.DisplayType.CURRENT, matCurr);
        event.putDisplay(MonthEvent.DisplayType.PASSED, matPassed);
        event.putDisplay(MonthEvent.DisplayType.FUTURE, matFuture);

        if (eventSection.contains("day-name")) {
            event.addDayName(eventSection.getString("day-name"));
        }

        if (eventSection.contains("day-names")) {
            for (String dayName : eventSection.getStringList("day-names")) {
                event.addDayName(dayName);
            }
        }

        this.events.add(event);
    }

    public FileConfiguration reloadConfig() {
        cfg = super.loadConfig();

        loadEvents();

        return cfg;
    }

    @Override
    public String getString(String path) {
        if (path == null)
            return "";

        String str = cfg.getString(path);

        if (str == null || str.isEmpty())
            return "";

        return Utils.format(str);
    }

    @Override
    public Integer getInteger(String path) {
        return cfg.getInt(path);
    }

    @Override
    public Long getLong(String path) {
        return Long.valueOf(cfg.getString(path, "0"));
    }

    @Override
    public Boolean getBoolean(String path) {
        return cfg.getBoolean(path);
    }

    @Override
    public List<String> getListString(String path) {
        return cfg.getStringList(path).stream()
                .map(Utils::format)
                .collect(Collectors.toList());
    }
}
