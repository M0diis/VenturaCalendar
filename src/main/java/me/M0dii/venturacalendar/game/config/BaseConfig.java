package me.m0dii.venturacalendar.game.config;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.configutils.Config;
import me.m0dii.venturacalendar.base.configutils.ConfigUtils;
import me.m0dii.venturacalendar.base.dateutils.EventDays;
import me.m0dii.venturacalendar.base.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseConfig extends Config implements ConfigUtils {
    final HashMap<String, EventDays> redeemableMonths = new HashMap<>();
    final HashMap<Messages, String> messages = new HashMap<>();
    FileConfiguration cfg;

    public BaseConfig(VenturaCalendar plugin) {
        super(plugin.getDataFolder(), "BaseConfig.yml", plugin);

        cfg = super.loadConfig();

        VenturaCalendar.PREFIX = getString("messages.prefix");

        debug();
    }

    public void debug() {
        VenturaCalendar.debug = getBoolean("debug");
    }

    public boolean updateCheck() {
        return getBoolean("update-check");
    }

    public boolean rewardsEnabled() {
        return getBoolean("rewards.enabled");
    }

    public boolean titleEnabled() {
        return getBoolean("new-day.title.enabled");
    }

    public List<String> getNewDayCommands() {
        return getListString("new-day.commands");
    }

    public Optional<String> getNewDayMessage() {
        List<String> msg = getListString("new-day.messages");

        if (msg.isEmpty()) {
            return Optional.empty();
        }

        boolean allEmpty = msg.stream().allMatch(String::isEmpty);

        if (allEmpty) {
            return Optional.empty();
        }

        return Optional.of(msg.stream()
                .map(m -> Utils.format(m) + "\n")
                .collect(Collectors.joining()).trim());
    }

    public Optional<String> getActionBarMessage() {
        if (Boolean.FALSE.equals(getBoolean("action-bar.enabled")))
            return Optional.empty();

        return Optional.of(getString("action-bar.text"));
    }

    public boolean redeemWhitelistEnabled() {
        return cfg.getBoolean("rewards.redeemable-months.enabled");
    }

    public Map<String, EventDays> getRedeemableMonths() {
        ConfigurationSection sec = cfg.getConfigurationSection("rewards.redeemable-months");

        if (sec != null) {
            sec.getValues(false).forEach((k, v) -> {
                if (!k.equalsIgnoreCase("enabled")) {
                    String[] fromToString = String.valueOf(v).split("-");

                    int from = Integer.parseInt(fromToString[0]);
                    int to = Integer.parseInt(fromToString[1]);

                    redeemableMonths.put(k, new EventDays(from, to));
                }
            });
        }

        return redeemableMonths;
    }

    public String getMessage(Messages msg) {
        return this.getMessages().get(msg);
    }

    private HashMap<Messages, String> getMessages() {
        String path = "messages.";

        messages.put(Messages.NO_PERMISSION, VenturaCalendar.PREFIX +
                getString(path + "no-permission"));
        messages.put(Messages.NOT_PLAYER, VenturaCalendar.PREFIX +
                getString(path + "player-only"));
        messages.put(Messages.UNKNOWN_COMMAND, VenturaCalendar.PREFIX +
                getString(path + "unknown-command"));
        messages.put(Messages.CONFIG_RELOADED, VenturaCalendar.PREFIX +
                getString(path + "config-reloaded"));
        messages.put(Messages.REDEEMED, VenturaCalendar.PREFIX +
                getString(path + "redeemed"));
        messages.put(Messages.REAL_TIME_SET, VenturaCalendar.PREFIX +
                getString(path + "real-time-set"));
        messages.put(Messages.HELP, VenturaCalendar.PREFIX +
                getString(path + "help"));

        messages.put(Messages.TITLE_TEXT, getString("new-day.title.text"));
        messages.put(Messages.SUBTITLE_TEXT, getString("new-day.title.subtitle"));

        return messages;
    }

    @Override
    public FileConfiguration reloadConfig() {
        cfg = super.reloadConfig();

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
                .toList();
    }
}
