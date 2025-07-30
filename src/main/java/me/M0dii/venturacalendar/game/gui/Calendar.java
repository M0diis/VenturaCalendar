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

public class Calendar implements InventoryHolder {
    private final DateUtils dateUtils;
    private final CalendarConfig calConf;

    private final VenturaCalendarDate venturaCalendarDate;
    private final VenturaCalendarDate creationVenturaCalendarDate;

    private final Inventory inventory;
    private final Map<Items, Object> items = new HashMap<>();

    private final List<MonthEvent> events;

    public Calendar(VenturaCalendarDate venturaCalendarDate,
                    VenturaCalendarDate creationVenturaCalendarDate,
                    VenturaCalendar plugin) {
        dateUtils = plugin.getDateUtils();
        calConf = plugin.getCalendarConfig();

        this.venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);
        this.creationVenturaCalendarDate = VenturaCalendarDate.clone(creationVenturaCalendarDate);

        this.events = plugin.getEventConfig().getEvents();

        this.inventory = createInventory(venturaCalendarDate, creationVenturaCalendarDate);
    }

    public VenturaCalendarDate getDate() {
        return this.venturaCalendarDate;
    }

    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    private Inventory createInventory(VenturaCalendarDate venturaCalendarDate, VenturaCalendarDate creationVenturaCalendarDate) {
        venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);
        creationVenturaCalendarDate = VenturaCalendarDate.clone(creationVenturaCalendarDate);

        TimeSystem ts = TimeSystem.of(venturaCalendarDate.getTimeSystem());

        Map<InventoryProperties, Object> calendarProperties = calConf.getCalendarProperties(false);

        List<ItemStack> dayItems = new ArrayList<>();
        List<ItemStack> passedDayItems = new ArrayList<>();
        List<ItemStack> futureDayItems = new ArrayList<>();
        List<ItemStack> weekItems = new ArrayList<>();

        String title = Utils.setPlaceholders((String) calendarProperties.get(InventoryProperties.HEADER), venturaCalendarDate, true);

        Inventory newInventory = Bukkit.createInventory(this, getInventorySize(venturaCalendarDate, ts), title);

        double daysPerMonth = ts.getDaysPerMonth().get((int) venturaCalendarDate.getMonth());
        double firstWeekDay = dateUtils.getDayOfWeek(dateUtils.down(DateEnum.DAY,
                (int) venturaCalendarDate.getDay(), venturaCalendarDate
        ));

        double daysPerWeek = ts.getDaysPerWeek();

        double weeksThisMonth = Math.ceil(((daysPerMonth + firstWeekDay) / daysPerWeek));

        int weekSlot = (int) daysPerWeek;
        int daySlot = (int) firstWeekDay;

        long dayOfMonth = 0;
        long weekOfMonth = 0;

        var itemProperties = (HashMap<Items, HashMap<ItemProperties, Object>>) calendarProperties.get(InventoryProperties.ITEMS);

        Map<ItemProperties, Object> todayProps = itemProperties.get(Items.TODAY);
        Map<ItemProperties, Object> passedDayProps = itemProperties.get(Items.PASSED);
        Map<ItemProperties, Object> futureDayProps = itemProperties.get(Items.FUTURE);
        Map<ItemProperties, Object> weekProps = itemProperties.get(Items.WEEK);

        for (long week = 1; week <= weeksThisMonth; week++, weekOfMonth++, weekSlot = weekSlot + 9) {
            venturaCalendarDate.setWeek(weekOfMonth);

            for (long day = 1; day <= daysPerWeek; day++, dayOfMonth++, daySlot++) {
                venturaCalendarDate.setDay(dayOfMonth);

                if (isToday(venturaCalendarDate, creationVenturaCalendarDate)) {
                    ItemStack todayItem = createItem(todayProps, venturaCalendarDate, false, MonthEvent.DisplayType.CURRENT);

                    if (todayItem != null && daySlot < 55) {
                        newInventory.setItem(daySlot, todayItem);
                        items.put(Items.TODAY, todayItem);
                        dayItems.add(todayItem);
                    }
                } else if (isFuture(venturaCalendarDate, creationVenturaCalendarDate)) {
                    ItemStack dayItem = createItem(futureDayProps, venturaCalendarDate, false, MonthEvent.DisplayType.FUTURE);

                    if (dayItem != null && daySlot < 55) {
                        newInventory.setItem(daySlot, dayItem);
                        dayItems.add(dayItem);
                    }
                } else {
                    ItemStack dayItem = createItem(passedDayProps, venturaCalendarDate, false, MonthEvent.DisplayType.PASSED);

                    if (dayItem != null && daySlot < 55) {
                        newInventory.setItem(daySlot, dayItem);
                        dayItems.add(dayItem);
                    }
                }

                if (isEndOfWeek(venturaCalendarDate, daySlot)) {
                    daySlot++;
                    dayOfMonth++;

                    break;
                }

                if (isEndOfMonth(venturaCalendarDate)) {
                    week = (int) (weeksThisMonth + 1);
                    day = (int) (daysPerWeek + 1);
                }
            }

            ItemStack weekItem = createItem(weekProps, venturaCalendarDate, true, null);

            if (weekItem != null && weekSlot < 55) {
                newInventory.setItem(weekSlot, weekItem);
                weekItems.add(weekItem);
            }

            daySlot = (int) (daySlot + (8 - (daysPerWeek - 1)));
        }

        items.put(Items.DAY, dayItems);
        items.put(Items.PASSED, passedDayItems);
        items.put(Items.FUTURE, futureDayItems);
        items.put(Items.WEEK, weekItems);

        return newInventory;
    }

    private boolean isEndOfWeek(VenturaCalendarDate venturaCalendarDate, int daySlot) {
        TimeSystem timeSystem = venturaCalendarDate.getTimeSystem();

        long daysPerWeek = timeSystem.getDaysPerWeek();

        if (daysPerWeek > 8)
            daysPerWeek = 8;

        if (daysPerWeek <= 0)
            daysPerWeek = 1;

        if (venturaCalendarDate.getWeek() == 0)
            return daySlot == (daysPerWeek - 1);

        return false;
    }

    private boolean isEndOfMonth(VenturaCalendarDate venturaCalendarDate) {
        TimeSystem timeSystem = venturaCalendarDate.getTimeSystem();

        long daysPerMonth = timeSystem.getDaysPerMonth().get((int) venturaCalendarDate.getMonth());

        return venturaCalendarDate.getDay() == daysPerMonth - 1;
    }

    public ItemStack createItem(Map<ItemProperties, Object> itemProperties, VenturaCalendarDate venturaCalendarDate, boolean week,
                                MonthEvent.DisplayType type) {
        String name = Utils.setPlaceholders((String) itemProperties.get(ItemProperties.NAME), venturaCalendarDate, true);
        Material material = (Material) itemProperties.get(ItemProperties.MATERIAL);
        int amount = Integer.parseInt(Utils.setPlaceholders(String.valueOf(itemProperties.get(ItemProperties.AMOUNT)), venturaCalendarDate, true));

        List<String> lore = new ArrayList<>();

        if (itemProperties.get(ItemProperties.LORE) != null) {
            lore = new ArrayList<>((List<String>) itemProperties.get(ItemProperties.LORE))
                    .stream().map(str -> Utils.setPlaceholders(str, venturaCalendarDate, true))
                    .collect(Collectors.toList());
        }

        String skullOwner = (String) itemProperties.getOrDefault(ItemProperties.META_SKULL_OWNER, null);

        if (!week) {
            for (MonthEvent event : events) {
                if (event.includesDate(venturaCalendarDate)) {
                    material = event.getDisplay(type);

                    lore.add("");
                    lore.addAll(event.getDescription());
                }
            }
        }

        if ((boolean) itemProperties.get(ItemProperties.TOGGLE)) {
            if (skullOwner == null)
                return new ItemCreator(material, amount, name, lore).getItem();
            else
                return new ItemCreator(material, amount, name, lore, skullOwner).getItem();
        }

        return null;
    }

    private boolean isToday(VenturaCalendarDate venturaCalendarDate, VenturaCalendarDate currentVenturaCalendarDate) {
        return venturaCalendarDate.getYear() == currentVenturaCalendarDate.getYear()
                && venturaCalendarDate.getMonth() == currentVenturaCalendarDate.getMonth()
                && venturaCalendarDate.getDay() == currentVenturaCalendarDate.getDay();
    }

    private boolean isFuture(VenturaCalendarDate venturaCalendarDate, VenturaCalendarDate currentVenturaCalendarDate) {
        return venturaCalendarDate.getMonth() >= currentVenturaCalendarDate.getMonth()
                && venturaCalendarDate.getDay() > currentVenturaCalendarDate.getDay();
    }


    private int getInventorySize(VenturaCalendarDate venturaCalendarDate, TimeSystem timeSystem) {
        venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);
        timeSystem = TimeSystem.of(timeSystem);

        int slots = 0;

        double daysPerMonth = timeSystem.getDaysPerMonth().get((int) venturaCalendarDate.getMonth());
        double firstWeekDay = dateUtils.getDayOfWeek(dateUtils.down(DateEnum.DAY, (int) venturaCalendarDate.getDay(), venturaCalendarDate));
        double daysPerWeek = timeSystem.getDaysPerWeek();

        if (daysPerWeek > 8)
            daysPerWeek = 8;

        double weeksPerMonth = Math.ceil((daysPerMonth + firstWeekDay) / daysPerWeek);

        for (int week = 1; week <= weeksPerMonth; week++)
            slots = slots + 9;

        return slots > 54 ? 54 : Math.max(slots, 9);
    }
}
