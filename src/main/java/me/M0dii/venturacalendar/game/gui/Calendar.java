package me.M0dii.venturacalendar.game.gui;

import me.M0dii.venturacalendar.base.dateutils.*;
import me.M0dii.venturacalendar.base.itemutils.ItemCreator;
import me.M0dii.venturacalendar.base.itemutils.ItemProperties;
import me.M0dii.venturacalendar.base.itemutils.Items;
import me.M0dii.venturacalendar.base.utils.Utils;
import me.M0dii.venturacalendar.game.config.CalendarConfig;
import me.M0dii.venturacalendar.VenturaCalendar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		
		this.events = plugin.getBaseConfig().getEvents();
		
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
		
		HashMap<InventoryProperties, Object> calendarProperties = calConf.getCalendarProperties(false);

		ArrayList<ItemStack> dayItems = new ArrayList<>();
		ArrayList<ItemStack> weekItems = new ArrayList<>();
		
		String title = Utils.replacePlaceholder((String) calendarProperties.get(InventoryProperties.HEADER), date, true);
		
		Inventory inventory = Bukkit.createInventory(this, getInventorySize(date, ts), title);
		
		double daysPerMonth = ts.getDaysPerMonth().get((int) date.getMonth());
		double firstWeekDay = dateUtils.getDayOfWeek(dateUtils.down(DateEnum.DAY,
				(int) date.getDay(), date));

		double daysPerWeek = ts.getDaysPerWeek();
		 
		double weeksThisMonth = Math.ceil(((daysPerMonth + firstWeekDay) / daysPerWeek));
		
		int weekSlot = (int) daysPerWeek;
		int daySlot = (int) firstWeekDay;
		int dayNullSlot = daySlot;
		
		long dayOfMonth  = 0;
		long weekOfMonth = 0;
		
		HashMap<Items, HashMap<ItemProperties, Object>> itemProperties =
				(HashMap<Items, HashMap<ItemProperties, Object>>)
				calendarProperties.get(InventoryProperties.ITEMS);
		
		HashMap<ItemProperties, Object> todayProperties = itemProperties.get(Items.TODAY);
		HashMap<ItemProperties, Object> dayProperties = itemProperties.get(Items.DAY);
		HashMap<ItemProperties, Object> weekProperties = itemProperties.get(Items.WEEK);

		for(long week = ts.getWeekZero(); week <= weeksThisMonth; week++, weekOfMonth++, weekSlot = weekSlot + 9)
		{
			date.setWeek(weekOfMonth);
			
			for(long day = ts.getDayZero(); day <= daysPerWeek; day++, dayOfMonth++, daySlot++)
			{
				date.setDay(dayOfMonth);
				
				if(isToday(date, creationDate))
				{
					ItemStack todayItem = createItem(todayProperties, date, false);
					
					if(todayItem != null && daySlot < 55)
					{
						inventory.setItem(daySlot, todayItem);
						items.put(Items.TODAY, todayItem);
						dayItems.add(todayItem);
					}
				}
				else
				{
					ItemStack dayItem = createItem(dayProperties, date, false);
					
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

			ItemStack weekItem = createItem(weekProperties, date, true);
			
			if(weekItem != null && weekSlot < 55)
			{
				inventory.setItem(weekSlot, weekItem);
				weekItems.add(weekItem);
			}
			
			daySlot = (int) (daySlot + (8 - (daysPerWeek - ts.getDayZero())));
		}
		
		items.put(Items.DAY, dayItems);
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
			return daySlot == (daysPerWeek - timeSystem.getDayZero());
			
		return false;
	}
	
	private boolean isEndOfMonth(Date date)
	{
		TimeSystem timeSystem = date.getTimeSystem();
		
		long daysPerMonth = timeSystem.getDaysPerMonth().get((int) date.getMonth());
		
		return date.getDay() == daysPerMonth - timeSystem.getDayZero();
	}
	
	public ItemStack createItem(HashMap<ItemProperties, Object> itemProperties, Date date, boolean week)
	{
		String name = Utils.replacePlaceholder((String) itemProperties.get(ItemProperties.NAME), date, true);
		Material material = (Material) itemProperties.get(ItemProperties.MATERIAL);
		int amount = Integer.parseInt(Utils.replacePlaceholder((String) itemProperties.get(ItemProperties.AMOUNT), date, true));
		
		List<String> lore = new ArrayList<>();
		
		if(itemProperties.get(ItemProperties.LORE) != null)
		{
			lore = new ArrayList<>((List<String>) itemProperties.get(ItemProperties.LORE))
					.stream().map(str -> Utils.replacePlaceholder(str, date, true))
					.collect(Collectors.toList());
		}
		
		if(!week)
		{
			for(MonthEvent event : events)
			{
				if(event.includesMonth(date.getMonthName()) && event.includesDay((int)date.getDay() + 1))
				{
					material = event.getDisplay();
					
					name = "%s%s".formatted(name, " " + event.getName());
					
					lore.add("");
					
					lore.addAll(event.getDescription());
				}
			}
		}
		
		if((boolean) itemProperties.get(ItemProperties.TOGGLE))
			return new ItemCreator(material, amount, name, lore).getItem();
		
		return null;
	}
	
	private boolean isToday(Date date, Date currentDate)
	{
		return date.getYear() == currentDate.getYear()
				&& date.getMonth() == currentDate.getMonth()
				&& date.getDay() == currentDate.getDay();
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
