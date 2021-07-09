package me.M0dii.VenturaCalendar.Game.GUI;

import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Base.DateUtils.Date;
import me.M0dii.VenturaCalendar.Base.DateUtils.DateEnum;
import me.M0dii.VenturaCalendar.Base.DateUtils.DateUtils;
import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.Base.ItemUtils.ItemCreator;
import me.M0dii.VenturaCalendar.Base.ItemUtils.ItemProperties;
import me.M0dii.VenturaCalendar.Base.ItemUtils.Items;
import me.M0dii.VenturaCalendar.Game.Config.CalendarConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Calendar implements InventoryHolder
{
	DateUtils dateUtils = VenturaCalendar.getDateUtils();
	
	CalendarConfig calendarConfig = VenturaCalendar.getCalendarConfig();
	
	Date date;
	Date creationDate;
	
	Inventory inventory;
	HashMap<Items, Object> items = new HashMap<>();
	
	public Calendar(Date date, Date creationDate)
	{
		date = new Date(date);
		creationDate = new Date(creationDate);
		
		inventory = createInventory(date, creationDate);
	}
	
	public @NotNull Inventory getInventory()
	{
		return inventory;
	}
	
	public HashMap<Items, Object> getItems()
	{
		return items;
	}
	
	private Inventory createInventory(Date date, Date creationDate)
	{
		date = new Date(date);
		
		TimeSystem timeSystem = new TimeSystem(date.getTimeSystem());
		
		creationDate = new Date(creationDate);
		
		HashMap<InventoryProperties, Object> calendarProperties = calendarConfig.getCalendarProperties();

		ArrayList<ItemStack> dayItems = new ArrayList<>();
		ArrayList<ItemStack> weekItems = new ArrayList<>();
		
		String title = Utils.replacePlaceholder((String) calendarProperties.get(InventoryProperties.HEADER), date);

		Inventory inventory = Bukkit.createInventory(this,
				getInventorySize(date, timeSystem), Component.text(title));
		
		double daysPerMonth = timeSystem.getDaysPerMonth().get((int) date.getMonth());
		double firstWeekDay = dateUtils.getDayOfWeek(dateUtils.down(DateEnum.day, (int) date.getDay(), date));
		double daysPerWeek = timeSystem.getDaysPerWeek();
		
		 if(daysPerWeek > 8)
			 daysPerWeek = 8;
		 
		double weeksThisMonth = Math.ceil(((daysPerMonth + firstWeekDay) / daysPerWeek));
		
		int weekSlot = (int) daysPerWeek;
		int daySlot = (int) firstWeekDay;
		int dayNullSlot = daySlot;
		
		long dayOfMonth  = 0;
		long weekOfMonth = 0;
		
		HashMap<Items, HashMap<ItemProperties, Object>> itemProperties =
				(HashMap<Items, HashMap<ItemProperties, Object>>)
				calendarProperties.get(InventoryProperties.ITEMS);
		
		HashMap<ItemProperties, Object> todayItemProperties = itemProperties.get(Items.TODAY);
		HashMap<ItemProperties, Object> dayItemProperties = itemProperties.get(Items.DAY);
		HashMap<ItemProperties, Object> weekItemProperties = itemProperties.get(Items.WEEK);

		for(long week = timeSystem.getWeekZero(); week <= weeksThisMonth; week++, weekOfMonth++, weekSlot = weekSlot + 9)
		{
			date.setWeek(weekOfMonth);
			
			for(long day = timeSystem.getDayZero(); day <= daysPerWeek; day++, dayOfMonth++, daySlot++)
			{
				date.setDay(dayOfMonth);
				
				if(isToday(date, creationDate))
				{
					ItemStack todayItem =  createItem(todayItemProperties, date);
					
					if(todayItem != null)
					{
						inventory.setItem(daySlot, todayItem);
						items.put(Items.TODAY, todayItem);
						dayItems.add(todayItem);
					}
				}
				else
				{
					ItemStack dayItem = createItem(dayItemProperties, date);
					
					if (dayItem != null)
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

			ItemStack weekItem = createItem(weekItemProperties, date);
			
			if(weekItem != null)
			{
				inventory.setItem(weekSlot, weekItem);
				weekItems.add(weekItem);
			}
			
			daySlot = (int) (daySlot + (8 - (daysPerWeek - timeSystem.getDayZero())));
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
	
	public ItemStack createItem(HashMap<ItemProperties, Object> itemProperties, Date date)
	{
		String name = Utils.replacePlaceholder((String) itemProperties.get(ItemProperties.NAME), date);
		Material material = (Material) itemProperties.get(ItemProperties.MATERIAL);
		int amount = Integer.parseInt(Utils.replacePlaceholder((String) itemProperties.get(ItemProperties.AMOUNT), date));
		
		List<String> lore = null;
		
		if(itemProperties.get(ItemProperties.LORE) != null)
		{
			lore = new ArrayList<>((List<String>) itemProperties.get(ItemProperties.LORE));
			
			if(lore != null)
				for(String line : lore)
					lore.set(lore.indexOf(line), Utils.replacePlaceholder(line, date));
		}
		
		if((boolean) itemProperties.get(ItemProperties.TOGGLE))
			return new ItemCreator(material, amount, name, lore).getItem();
		
		return null;
	}
	
	private boolean isToday(Date date, Date currentDate)
	{
		if(date.getYear() == currentDate.getYear())
			if(date.getMonth() == currentDate.getMonth())
				return date.getDay() == currentDate.getDay();

		return false;
	}

	private int getInventorySize(Date date, TimeSystem timeSystem)
	{
		date = new Date(date);
		timeSystem = new TimeSystem(timeSystem);
		
		int slots = 0;
		
		double daysPerMonth = timeSystem.getDaysPerMonth().get((int) date.getMonth());
		double firstWeekDay = (double) dateUtils.getDayOfWeek(dateUtils.down(DateEnum.day, (int) date.getDay(), date));
		double daysPerWeek = timeSystem.getDaysPerWeek();

		if(daysPerWeek > 8)
			daysPerWeek = 8;
		
		double weeksPerMonth = Math.ceil((daysPerMonth + firstWeekDay) / daysPerWeek);
		
		for(int week = 1; week <= weeksPerMonth; week++)
			slots = slots + 9;
		
		if(slots > 54)
			return 54;
		
		return Math.max(slots, 9);
	}
}
