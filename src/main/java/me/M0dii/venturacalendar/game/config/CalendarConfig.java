package me.m0dii.venturacalendar.game.config;

import me.clip.placeholderapi.PlaceholderAPI;
import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.configutils.Config;
import me.m0dii.venturacalendar.base.configutils.ConfigUtils;
import me.m0dii.venturacalendar.base.itemutils.ItemProperties;
import me.m0dii.venturacalendar.base.itemutils.Items;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.gui.InventoryProperties;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalendarConfig extends Config implements ConfigUtils {
    public CalendarConfig(VenturaCalendar plugin) {
        super(plugin.getDataFolder(), "CalendarConfig.yml", plugin);

        super.loadConfig();

        getCalendarProperties(true);
    }

    final Map<InventoryProperties, Object> calendar = new HashMap<>();
    final Map<Items, Map<ItemProperties, Object>> items = new HashMap<>();

    public Map<InventoryProperties, Object> getCalendarProperties(boolean reload) {
        if (!reload) {
            return calendar;
        }

        String title;
        int size;

        String path = "items.";

        // Calendar
        title = getString("title");
        size = getInteger("size");

        // Items

        // Today
        items.put(Items.TODAY, getItemProperties(path + "today."));

        // Day
        items.put(Items.DAY, getItemProperties(path + "day."));

        // Future
        items.put(Items.FUTURE, getItemProperties(path + "future."));

        // Passed
        items.put(Items.PASSED, getItemProperties(path + "passed."));

        // Week
        items.put(Items.WEEK, getItemProperties(path + "week."));

        // Next Month Button
        items.put(Items.NEXT_MONTH, getItemProperties(path + "next-month."));

        // Previous Month Button
        items.put(Items.PREVIOUS_MONTH, getItemProperties(path + "previous-month."));


        calendar.put(InventoryProperties.HEADER, title);
        calendar.put(InventoryProperties.SIZE, size);
        calendar.put(InventoryProperties.ITEMS, items);

        return calendar;
    }

    private Map<ItemProperties, Object> getItemProperties(String path) {
        Map<ItemProperties, Object> itemProperties = new HashMap<>();

        itemProperties.put(ItemProperties.TOGGLE, getBoolean(path + "toggle"));
        itemProperties.put(ItemProperties.NAME, getString(path + "name"));

        String matName = config.getString(path + "material", "WHITE_STAINED_GLASS_PANE");

        if (matName.contains("player_skull=")) {
            String uuid = matName.replace("player_skull=", "");

//			itemProperties.put(ItemProperties.MATERIAL, Material.PLAYER_HEAD);
//			itemProperties.put(ItemProperties.META_SKULL_OWNER, uuid);
        }
        else {
            itemProperties.put(ItemProperties.MATERIAL, Utils.getMaterial(matName));
        }

        itemProperties.put(ItemProperties.AMOUNT, config.getString(path + "amount"));
        itemProperties.put(ItemProperties.LORE, getListString(path + "lore"));

        return itemProperties;
    }

    public FileConfiguration reloadConfig() {
        config = super.reloadConfig();

        getCalendarProperties(true);

        return config;
    }

    @Override
    public String getString(String path) {
        if (path == null)
            return "";

        String str = config.getString(path);

        if (str == null || str.isEmpty())
            return "";

        return Utils.format(str);
    }

    @Override
    public Integer getInteger(String path) {
        return config.getInt(path);
    }

    @Override
    public Long getLong(String path) {
        return Long.valueOf(config.getString(path, "0"));
    }

    @Override
    public Boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    @Override
    public List<String> getListString(String path) {
        Stream<String> list = config.getStringList(path).stream().map(Utils::format);

        if (plugin.papiEnabled()) {
            list = list.map(str -> PlaceholderAPI.setPlaceholders(null, str));
        }

        return list.collect(Collectors.toList());
    }
}
