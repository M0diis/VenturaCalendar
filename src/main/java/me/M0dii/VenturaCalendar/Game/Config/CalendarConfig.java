package me.M0dii.VenturaCalendar.Game.Config;

import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Base.ConfigUitls.Config;
import me.M0dii.VenturaCalendar.Base.ConfigUitls.ConfigUtils;
import me.M0dii.VenturaCalendar.Base.ItemUtils.ItemProperties;
import me.M0dii.VenturaCalendar.Base.ItemUtils.Items;
import me.M0dii.VenturaCalendar.Game.GUI.InventoryProperties;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarConfig extends Config implements ConfigUtils
{
	FileConfiguration config;
	
	public CalendarConfig()
	{
		super(VenturaCalendar.instance.getDataFolder(), "CalendarConfig.yml");
		
		config = super.loadConfig();
	}
	
	public HashMap<InventoryProperties, Object> getCalendarProperties()
	{
		HashMap<InventoryProperties, Object> calendar = new HashMap<>();
		HashMap<Items, HashMap<ItemProperties, Object>> items = new HashMap<>();
		
		String title;
		int size;
		
		String defaultPath;
		String path;
		
		// Calendar
		title = getString("title");
		size = getInteger("size");
		
		// Items
		defaultPath = "items.";
		
		// Today
		path = defaultPath + "today.";
		items.put(Items.TODAY, getItemProperties(path));
		
		//Day
		path = defaultPath + "day.";
		items.put(Items.DAY, getItemProperties(path));
		
		//Week
		path = defaultPath + "week.";
		items.put(Items.WEEK, getItemProperties(path));
		
		calendar.put(InventoryProperties.HOLDER, null);
		calendar.put(InventoryProperties.HEADER, title);
		calendar.put(InventoryProperties.SIZE, size);
		calendar.put(InventoryProperties.ITEMS, items);	
		
		return calendar;
	}
	
	private HashMap<ItemProperties, Object> getItemProperties(String path)
	{
		HashMap<ItemProperties, Object> item = new HashMap<>();
		
		item.put(ItemProperties.TOGGLE, getBoolean(path + "toggle"));
		item.put(ItemProperties.NAME, getString(path + "name"));
		item.put(ItemProperties.MATERIAL, Material.getMaterial(config.getString(path + "material", "WHITE_STAINED_GLASS_PANE")));
		item.put(ItemProperties.AMOUNT, config.getString(path + "amount"));
		item.put(ItemProperties.LORE, getListString(path + "lore"));
		
		return item;
	}
	
	public FileConfiguration reloadConfig()
	{
		return config = super.reloadConfig();
	}
	
	@Override
	public String getString(String path)
	{
		return ChatColor.translateAlternateColorCodes('&', config.getString(path, ""));
	}

	@Override
	public Integer getInteger(String path)
	{
		return config.getInt(path);
	}
	
	@Override
	public Long getLong(String path) {
		return Long.valueOf(config.getString(path, "0"));
	}
	
	@Override
	public Boolean getBoolean(String path)
	{
		return config.getBoolean(path);
	}

	@Override
	public List<String> getListString(String path)
	{
		if (config.getList(path) != null)
		{
			List<String> list = config.getStringList(path);
			
			for(int index = 0; index < list.size(); index++)
				list.set(index, Utils.format(PlaceholderAPI.setPlaceholders(null, list.get(index))));
			
			return list;
		}
		
		return null;
	}
}
