package me.m0dii.venturacalendar.game.gui;

import lombok.Getter;
import lombok.Setter;
import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.DateCalculator;
import me.m0dii.venturacalendar.base.dateutils.MonthEvent;
import me.m0dii.venturacalendar.base.dateutils.RealTimeDate;
import me.m0dii.venturacalendar.base.itemutils.ItemCreator;
import me.m0dii.venturacalendar.base.itemutils.ItemProperties;
import me.m0dii.venturacalendar.base.itemutils.Items;
import me.m0dii.venturacalendar.base.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class RealTimeCalendar implements InventoryHolder {

    private final RealTimeDate date;
    private final Inventory inventory;
    private final List<MonthEvent> events;
    private RealTimeDate creationDate;
    private RealTimeDate realTimeCurrentDate;

    public RealTimeCalendar(RealTimeDate date) {
        this.date = new RealTimeDate(date);
        this.creationDate = new RealTimeDate(date);

        this.events = VenturaCalendar.getInstance().getEventConfig().getEvents();

        this.inventory = createInventory(date);

        this.realTimeCurrentDate = DateCalculator.realTimeNow();
    }

    public RealTimeCalendar(RealTimeDate date, RealTimeDate creationDate) {
        this.date = new RealTimeDate(date);
        this.creationDate = new RealTimeDate(creationDate);

        this.events = VenturaCalendar.getInstance().getEventConfig().getEvents();

        this.inventory = createInventory(date);


        this.realTimeCurrentDate = DateCalculator.realTimeNow();
    }

    public void setDate(RealTimeDate date) {
        this.date.setLocalDateTime(date.getLocalDateTime());
    }

    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    private Inventory createInventory(RealTimeDate date) {
        date = new RealTimeDate(date);
        creationDate = new RealTimeDate(creationDate);
        realTimeCurrentDate = DateCalculator.realTimeNow();

        Map<InventoryProperties, Object> calendarProperties = VenturaCalendar.getInstance()
                .getCalendarConfig().getCalendarProperties(false);

        String title = Utils.setPlaceholders((String) calendarProperties.get(InventoryProperties.HEADER), date, true);

        Inventory newInventory = Bukkit.createInventory(this, getInventorySize(date), title);

        double daysPerMonth = date.getLocalDateTime().getMonth().length(false);

        int firstWeekDay = date.getLocalDateTime().withDayOfMonth(1).get(ChronoField.DAY_OF_WEEK);

        if (Boolean.FALSE.equals(VenturaCalendar.getInstance().getTimeConfig().getBoolean("main-time-system.real-time.first-day-sunday"))) {
            firstWeekDay--;
        }

        int daysPerWeek = 7;

        double weeksThisMonth = Math.ceil(((daysPerMonth + firstWeekDay) / daysPerWeek));

        int weekSlot = daysPerWeek;
        int daySlot = firstWeekDay;

        int dayOfMonth = 1;
        long weekOfMonth = 0;

        Map<Items, HashMap<ItemProperties, Object>> itemProperties =
                (HashMap<Items, HashMap<ItemProperties, Object>>)
                        calendarProperties.get(InventoryProperties.ITEMS);

        Map<ItemProperties, Object> todayProps = itemProperties.get(Items.TODAY);
        Map<ItemProperties, Object> passedDayProps = itemProperties.get(Items.PASSED);
        Map<ItemProperties, Object> futureDayProps = itemProperties.get(Items.FUTURE);
        Map<ItemProperties, Object> weekProps = itemProperties.get(Items.WEEK);

        LocalDateTime copy = LocalDateTime.of(date.getLocalDateTime().toLocalDate(), date.getLocalDateTime().toLocalTime());

        for (int week = 0; week <= weeksThisMonth; week++, weekOfMonth++, weekSlot = weekSlot + 9) {
            date.setWeek(weekOfMonth);

            copy = LocalDateTime.of((int) date.getYear(), date.getLocalDateTime().getMonth(), dayOfMonth, date.getLocalDateTime().getHour(), date.getLocalDateTime().getMinute(), date.getLocalDateTime().getSecond());
            date.setLocalDateTime(copy);

            RealTimeDate forWeek = new RealTimeDate(date);

            forWeek.setWeek(date.getWeek() + 1);

            ItemStack weekItem = createItem(weekProps, forWeek, true, null);

            if (weekItem != null && weekSlot < 55) {
                newInventory.setItem(weekSlot, weekItem);
            }

            for (long day = 0; day < daysPerWeek; day++, dayOfMonth++, daySlot++) {
                date.setDay(dayOfMonth);

                copy = LocalDateTime.of((int) date.getYear(), date.getLocalDateTime().getMonth(), dayOfMonth, date.getLocalDateTime().getHour(), date.getLocalDateTime().getMinute(), date.getLocalDateTime().getSecond());
                date.setLocalDateTime(copy);

                if (isToday(date)) {

                    ItemStack todayItem = createItem(todayProps, date, false, MonthEvent.DisplayType.CURRENT);

                    if (todayItem != null && daySlot < 55) {
                        newInventory.setItem(daySlot, todayItem);
                    }
                } else if (isFuture(date, creationDate)) {
                    ItemStack dayItem = createItem(futureDayProps, date, false, MonthEvent.DisplayType.FUTURE);

                    if (dayItem != null && daySlot < 55) {
                        newInventory.setItem(daySlot, dayItem);
                    }
                } else {
                    ItemStack dayItem = createItem(passedDayProps, date, false, MonthEvent.DisplayType.PASSED);

                    if (dayItem != null && daySlot < 55) {
                        newInventory.setItem(daySlot, dayItem);
                    }
                }

                if (isEndOfWeek(date, daySlot)) {
                    daySlot++;
                    dayOfMonth++;

                    break;
                }

                if (isEndOfMonth(date)) {
                    week = (int) (weeksThisMonth + 1);
                    day = daysPerWeek + 1;
                }
            }

            daySlot = daySlot + (8 - (daysPerWeek - 1));
        }

        Map<ItemProperties, Object> nextMonthProps = itemProperties.get(Items.NEXT_MONTH);
        String nextMonthName = Utils.setPlaceholders((String) nextMonthProps.get(ItemProperties.NAME), creationDate, true);
        Material nextMonthMaterial = (Material) nextMonthProps.get(ItemProperties.MATERIAL);
        int nextMonthAmount = Integer.parseInt(Utils.setPlaceholders(String.valueOf(nextMonthProps.get(ItemProperties.AMOUNT)), creationDate, true));

        List<String> nextMonthLore = new ArrayList<>();

        if (itemProperties.get(Items.NEXT_MONTH) != null) {
            var nextMonthLoreList = nextMonthProps.get(ItemProperties.LORE);

            nextMonthLore = new ArrayList<>((List<String>) nextMonthLoreList)
                    .stream().map(str -> Utils.setPlaceholders(str, creationDate, true))
                    .toList();
        }

        ItemCreator nextMonthItem = new ItemCreator(nextMonthMaterial, nextMonthAmount, nextMonthName, nextMonthLore);

        Map<ItemProperties, Object> prevMonthProps = itemProperties.get(Items.PREVIOUS_MONTH);
        String prevMonthName = Utils.setPlaceholders((String) prevMonthProps.get(ItemProperties.NAME), creationDate, true);
        Material prevMonthMaterial = (Material) prevMonthProps.get(ItemProperties.MATERIAL);
        int prevMonthAmount = Integer.parseInt(Utils.setPlaceholders(String.valueOf(prevMonthProps.get(ItemProperties.AMOUNT)), creationDate, true));

        List<String> prevMonthLore = new ArrayList<>();

        if (itemProperties.get(Items.PREVIOUS_MONTH) != null) {
            var prevMonthLoreList = prevMonthProps.get(ItemProperties.LORE);

            prevMonthLore = new ArrayList<>((List<String>) prevMonthLoreList)
                    .stream().map(str -> Utils.setPlaceholders(str, creationDate, true))
                    .toList();
        }

        ItemCreator previousMonthItem = new ItemCreator(prevMonthMaterial, prevMonthAmount, prevMonthName, prevMonthLore);

        newInventory.setItem(8, nextMonthItem.getItem());
        newInventory.setItem(17, previousMonthItem.getItem());

        boolean lastRowEmpty = false;

        if (newInventory.getSize() == 54) {
            for (int i = 45; i < 54; i++) {
                if (newInventory.getItem(i) == null) {
                    lastRowEmpty = true;
                } else {
                    lastRowEmpty = false;
                    break;
                }
            }
        }

        if (lastRowEmpty) {
            Inventory smallerInventory = Bukkit.createInventory(this, 45, title);

            for (int i = 0; i < 45; i++) {
                smallerInventory.setItem(i, newInventory.getItem(i));
            }

            return smallerInventory;
        }

        return newInventory;
    }

    private boolean isEndOfWeek(RealTimeDate date, int daySlot) {
        long daysPerWeek = 7;

        if (date.getWeek() == 0) {
            return daySlot == (daysPerWeek - 1);
        }

        return false;
    }

    private boolean isEndOfMonth(RealTimeDate date) {
        return date.getDay() == date.getLocalDateTime().getMonth().length(false);
    }

    public ItemStack createItem(Map<ItemProperties, Object> itemProperties, RealTimeDate date, boolean week,
                                MonthEvent.DisplayType type) {
        String name = Utils.setPlaceholders((String) itemProperties.get(ItemProperties.NAME), date, true);
        Material material = (Material) itemProperties.get(ItemProperties.MATERIAL);
        int amount = Integer.parseInt(Utils.setPlaceholders(String.valueOf(itemProperties.get(ItemProperties.AMOUNT)), date, true));

        List<String> lore = new ArrayList<>();

        if (itemProperties.get(ItemProperties.LORE) != null) {
            lore = new ArrayList<>((List<String>) itemProperties.get(ItemProperties.LORE))
                    .stream().map(str -> Utils.setPlaceholders(str, date, true))
                    .collect(Collectors.toList());
        }

        String skullOwner = (String) itemProperties.getOrDefault(ItemProperties.META_SKULL_OWNER, null);

        if (!week) {
            for (MonthEvent event : events) {
                if (event.includesDate(date)) {
                    material = event.getDisplay(type);

                    lore.add("");
                    lore.addAll(event.getDescription());
                }
            }
        }

        if ((boolean) itemProperties.get(ItemProperties.TOGGLE)) {
            if (skullOwner == null) {
                return new ItemCreator(material, amount, name, lore).getItem();
            } else {
                return new ItemCreator(material, amount, name, lore, skullOwner).getItem();
            }
        }

        return null;
    }

    private boolean isToday(@NotNull RealTimeDate date) {
        return date.getYear() == realTimeCurrentDate.getYear()
                && date.getMonth() == realTimeCurrentDate.getMonth()
                && date.getDay() == realTimeCurrentDate.getDay();
    }

    private boolean isFuture(@NotNull RealTimeDate date, @NotNull RealTimeDate currentDate) {
        return date.getMonth() >= currentDate.getMonth()
                && date.getDay() > currentDate.getDay();
    }

    private int getInventorySize(RealTimeDate date) {
        date = new RealTimeDate(date);

        int slots = 0;

        double daysPerMonth = date.getLocalDateTime().getMonth().length(false);
        double firstWeekDay = date.getLocalDateTime().withDayOfMonth(1).get(ChronoField.DAY_OF_WEEK);

        double weeksPerMonth = Math.ceil((daysPerMonth + firstWeekDay) / 7);

        for (int week = 1; week <= weeksPerMonth; week++) {
            slots = slots + 9;
        }

        return slots > 54 ? 54 : Math.max(slots, 9);
    }
}
