package me.m0dii.venturacalendar.game.gui;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.*;
import me.m0dii.venturacalendar.base.itemutils.ItemCreator;
import me.m0dii.venturacalendar.base.itemutils.ItemProperties;
import me.m0dii.venturacalendar.base.itemutils.Items;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.config.CalendarConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Calendar implements InventoryHolder
{
	final DateUtils dateUtils;
	final CalendarConfig calConf;
	
	final Date date;
	final Date creationDate;
	
	final Inventory inventory;
	final HashMap<Items, Object> items = new HashMap<>();
	
	final List<MonthEvent> events;
	
	public Calendar(Date date, Date creationDate, VenturaCalendar plugin)
	{
		dateUtils = plugin.getDateUtils();
		calConf = plugin.getCalendarConfig();
		
		date = new Date(date);
		creationDate = new Date(creationDate);
		
		this.events = plugin.getEventConfig().getEvents();
		
		this.date = date;
		this.creationDate = creationDate;
		
		this.inventory = createInventory(date, creationDate);
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	public @NotNull Inventory getInventory()
	{
		return this.inventory;
	}
	
	public HashMap<Items, Object> getItems()
	{
		return this.items;
	}
	
	private Inventory createInventory(Date date, Date creationDate)
	{
		date = new Date(date);
		creationDate = new Date(creationDate);
		
		TimeSystem ts = new TimeSystem(date.getTimeSystem());
		
		Map<InventoryProperties, Object> calendarProperties = calConf.getCalendarProperties(false);

		List<ItemStack> dayItems = new ArrayList<>();
		List<ItemStack> passedDayItems = new ArrayList<>();
		List<ItemStack> futureDayItems = new ArrayList<>();
		List<ItemStack> weekItems = new ArrayList<>();
		
		String title = Utils.setPlaceholders((String) calendarProperties.get(InventoryProperties.HEADER), date, true);
		
		Inventory inventory = Bukkit.createInventory(this, getInventorySize(date, ts), title);
		
		double daysPerMonth = ts.getDaysPerMonth().get((int) date.getMonth());
		double firstWeekDay = dateUtils.getDayOfWeek(dateUtils.down(DateEnum.DAY,
				(int) date.getDay(), date));

		double daysPerWeek = ts.getDaysPerWeek();
		 
		double weeksThisMonth = Math.ceil(((daysPerMonth + firstWeekDay) / daysPerWeek));
		
		int weekSlot = (int) daysPerWeek;
		int daySlot = (int) firstWeekDay;
		
		long dayOfMonth  = 0;
		long weekOfMonth = 0;
		
		Map<Items, HashMap<ItemProperties, Object>> itemProperties =
				(HashMap<Items, HashMap<ItemProperties, Object>>)
				calendarProperties.get(InventoryProperties.ITEMS);
		
		Map<ItemProperties, Object> todayProps = itemProperties.get(Items.TODAY);
		Map<ItemProperties, Object> passedDayProps = itemProperties.get(Items.PASSED);
		Map<ItemProperties, Object> futureDayProps = itemProperties.get(Items.FUTURE);
		Map<ItemProperties, Object> weekProps = itemProperties.get(Items.WEEK);

		for(long week = 1; week <= weeksThisMonth; week++, weekOfMonth++, weekSlot = weekSlot + 9)
		{
			date.setWeek(weekOfMonth);
			
			for(long day = 1; day <= daysPerWeek; day++, dayOfMonth++, daySlot++)
			{
				date.setDay(dayOfMonth);
				
				if(isToday(date, creationDate))
				{
					ItemStack todayItem = createItem(todayProps, date, false, MonthEvent.DisplayType.CURRENT);
					
					if(todayItem != null && daySlot < 55)
					{
						inventory.setItem(daySlot, todayItem);
						items.put(Items.TODAY, todayItem);
						dayItems.add(todayItem);
					}
				}
				else if(isFuture(date, creationDate))
				{
					ItemStack dayItem = createItem(futureDayProps, date, false, MonthEvent.DisplayType.FUTURE);
					
					if (dayItem != null && daySlot < 55)
					{
						inventory.setItem(daySlot, dayItem);
						dayItems.add(dayItem);
					}
				}
				else
				{
					ItemStack dayItem = createItem(passedDayProps, date, false, MonthEvent.DisplayType.PASSED);
					
					if (dayItem != null && daySlot < 55)
					{
						inventory.setItem(daySlot, dayItem);
						dayItems.add(dayItem);
					}
				}
				
				if(isEndOfWeek(date, daySlot))
				{
					daySlot++;
					dayOfMonth++;
					
					break;
				}
				
				if(isEndOfMonth(date))
				{
					week = (int) (weeksThisMonth + 1);
					day = (int) (daysPerWeek + 1);
				}
			}

			ItemStack weekItem = createItem(weekProps, date, true, null);
			
			if(weekItem != null && weekSlot < 55)
			{
				inventory.setItem(weekSlot, weekItem);
				weekItems.add(weekItem);
			}
			
			daySlot = (int) (daySlot + (8 - (daysPerWeek - 1)));
		}
		
		items.put(Items.DAY, dayItems);
		items.put(Items.PASSED, passedDayItems);
		items.put(Items.FUTURE, futureDayItems);
		items.put(Items.WEEK, weekItems);
		
		return inventory;
	}
	
	private boolean isEndOfWeek(Date date, int daySlot)
	{
		TimeSystem timeSystem = date.getTimeSystem();
		
		long daysPerWeek = timeSystem.getDaysPerWeek();
		
		if(daysPerWeek > 8)
			daysPerWeek = 8;
		
		if(daysPerWeek <= 0)
			daysPerWeek = 1;
		
		if(date.getWeek() == 0)
			return daySlot == (daysPerWeek - 1);
			
		return false;
	}
	
	private boolean isEndOfMonth(Date date)
	{
		TimeSystem timeSystem = date.getTimeSystem();
		
		long daysPerMonth = timeSystem.getDaysPerMonth().get((int) date.getMonth());
		
		return date.getDay() == daysPerMonth - 1;
	}
	
	public ItemStack createItem(Map<ItemProperties, Object> itemProperties, Date date, boolean week,
								MonthEvent.DisplayType type)
	{
		String name = Utils.setPlaceholders((String) itemProperties.get(ItemProperties.NAME), date, true);
		Material material = (Material) itemProperties.get(ItemProperties.MATERIAL);
		int amount = Integer.parseInt(Utils.setPlaceholders(String.valueOf(itemProperties.get(ItemProperties.AMOUNT)), date, true));
		
		List<String> lore = new ArrayList<>();
		
		if(itemProperties.get(ItemProperties.LORE) != null)
		{
			lore = new ArrayList<>((List<String>) itemProperties.get(ItemProperties.LORE))
					.stream().map(str -> Utils.setPlaceholders(str, date, true))
					.collect(Collectors.toList());
		}
		
		String skullOwner = (String)itemProperties.getOrDefault(ItemProperties.META_SKULL_OWNER, null);
		
		if(!week)
		{
			for(MonthEvent event : events)
			{
				if(event.hasFromTo())
				{
					if(event.includesDate(date))
					{
						material = event.getDisplay(type);
						
						lore.add("");
						lore.addAll(event.getDescription());
						
						continue;
					}
				}
				
				if(event.hasDayNames())
				{
					if(event.includesDayName(date))
					{
						material = event.getDisplay(type);
						
						lore.add("");
						lore.addAll(event.getDescription());
					}
				}
			}
		}
		
		if((boolean) itemProperties.get(ItemProperties.TOGGLE))
		{
			if(skullOwner == null)
				return new ItemCreator(material, amount, name, lore).getItem();
			else
				return new ItemCreator(material, amount, name, lore, skullOwner).getItem();
		}
		
		return null;
	}
	
	private boolean isToday(Date date, Date currentDate)
	{
		return date.getYear() == currentDate.getYear()
			&& date.getMonth() == currentDate.getMonth()
			&& date.getDay() == currentDate.getDay();
	}
	
	private boolean isFuture(Date date, Date currentDate)
	{
		return date.getMonth() >= currentDate.getMonth()
			&& date.getDay() > currentDate.getDay();
	}
	
	
	private int getInventorySize(Date date, TimeSystem timeSystem)
	{
		date = new Date(date);
		timeSystem = new TimeSystem(timeSystem);
		
		int slots = 0;
		
		double daysPerMonth = timeSystem.getDaysPerMonth().get((int) date.getMonth());
		double firstWeekDay = (double) dateUtils.getDayOfWeek(dateUtils.down(DateEnum.DAY, (int) date.getDay(), date));
		double daysPerWeek = timeSystem.getDaysPerWeek();

		if(daysPerWeek > 8)
			daysPerWeek = 8;
		
		double weeksPerMonth = Math.ceil((daysPerMonth + firstWeekDay) / daysPerWeek);
		
		for(int week = 1; week <= weeksPerMonth; week++)
			slots = slots + 9;
		
		return slots > 54 ? 54 : Math.max(slots, 9);
	}
}
