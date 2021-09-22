package me.M0dii.venturacalendar.game.config;

import me.M0dii.venturacalendar.base.utils.Utils;
import me.M0dii.venturacalendar.VenturaCalendar;
import me.M0dii.venturacalendar.base.configutils.Config;
import me.M0dii.venturacalendar.base.configutils.ConfigUtils;
import me.M0dii.venturacalendar.base.itemutils.ItemProperties;
import me.M0dii.venturacalendar.base.itemutils.Items;
import me.M0dii.venturacalendar.game.gui.InventoryProperties;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

public class CalendarConfig extends Config implements ConfigUtils
{
	FileConfiguration cfg;
	
	public CalendarConfig(VenturaCalendar plugin)
	{
		super(plugin.getDataFolder(), "CalendarConfig.yml", plugin);
		
		cfg = super.loadConfig();
		
		reload();
	}
	
	private void reload()
	{
		getCalendarProperties(true);
	}
	
	final HashMap<InventoryProperties, Object> calendar = new HashMap<>();
	final HashMap<Items, HashMap<ItemProperties, Object>> items = new HashMap<>();
	
	public HashMap<InventoryProperties, Object> getCalendarProperties(boolean reload)
	{
		if(!reload)
			return calendar;
		
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
		HashMap<ItemProperties, Object> itemProperties = new HashMap<>();
		
		itemProperties.put(ItemProperties.TOGGLE, getBoolean(path + "toggle"));
		itemProperties.put(ItemProperties.NAME, getString(path + "name"));
		itemProperties.put(ItemProperties.MATERIAL, Material.getMaterial(cfg.getString(path + "material", "WHITE_STAINED_GLASS_PANE")));
		itemProperties.put(ItemProperties.AMOUNT, cfg.getString(path + "amount"));
		itemProperties.put(ItemProperties.LORE, getListString(path + "lore"));
		
		return itemProperties;
	}
	
	public FileConfiguration reloadConfig()
	{
		cfg = super.reloadConfig();
		
		this.reload();
		
		return cfg;
	}
	
	@Override
	public String getString(String path)
	{
		if(path == null)
			return "";
		
		String str = cfg.getString(path);
		
		if(str == null || str.isEmpty())
			return "";
		
		return Utils.format(str);
	}

	@Override
	public Integer getInteger(String path)
	{
		return cfg.getInt(path);
	}
	
	@Override
	public Long getLong(String path) {
		return Long.valueOf(cfg.getString(path, "0"));
	}
	
	@Override
	public Boolean getBoolean(String path)
	{
		return cfg.getBoolean(path);
	}

	@Override
	public List<String> getListString(String path)
	{
		if (cfg.getList(path) != null)
		{
			List<String> list = cfg.getStringList(path);
			
			for(int index = 0; index < list.size(); index++)
				list.set(index, Utils.format(PlaceholderAPI.setPlaceholders(null, list.get(index))));
			
			return list;
		}
		
		return null;
	}
}
