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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
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
    private int todaySlot = -1;

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
        this.date.setSecond(date.getSecond());
        this.date.setMinute(date.getMinute());
        this.date.setHour(date.getHour());
        this.date.setDay(date.getDay());
        this.date.setWeek(date.getWeek());
        this.date.setMonth(date.getMonth());
        this.date.setYear(date.getYear());
        this.date.setEra(date.getEra());
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

        int dayOfWeek = date.getLocalDateTime().withDayOfMonth(1).get(ChronoField.DAY_OF_WEEK);
        boolean firstDaySunday = Boolean.TRUE.equals(VenturaCalendar.getInstance()
                .getTimeConfig().getBoolean("main-time-system.real-time.first-day-sunday"));
        int firstWeekDay = firstDaySunday ? dayOfWeek % 7 : (dayOfWeek + 6) % 7;

        int daysPerWeek = 7;

        double weeksThisMonth = Math.ceil(((daysPerMonth + firstWeekDay) / daysPerWeek));

        int weekSlot = daysPerWeek;
        int daySlot = firstWeekDay;

        int dayOfMonth = 1;
        long weekOfMonth = 0;

        Map<Items, Map<ItemProperties, Object>> itemProperties =
                (Map<Items, Map<ItemProperties, Object>>)
                        calendarProperties.get(InventoryProperties.ITEMS);

        Map<ItemProperties, Object> todayProps = itemProperties.get(Items.TODAY);
        Map<ItemProperties, Object> passedDayProps = itemProperties.get(Items.PASSED);
        Map<ItemProperties, Object> futureDayProps = itemProperties.get(Items.FUTURE);
        Map<ItemProperties, Object> weekProps = itemProperties.get(Items.WEEK);

        LocalDateTime copy = LocalDateTime.of(date.getLocalDateTime().toLocalDate(), date.getLocalDateTime().toLocalTime());

        for (int week = 0; week < weeksThisMonth; week++, weekOfMonth++, weekSlot = weekSlot + 9) {
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
                        todaySlot = daySlot;
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

        ItemStack nextMonthItem = createItem(itemProperties.get(Items.NEXT_MONTH), creationDate, true, null);
        if (nextMonthItem != null) {
            newInventory.setItem(8, nextMonthItem);
        }

        ItemStack previousMonthItem = createItem(itemProperties.get(Items.PREVIOUS_MONTH), creationDate, true, null);
        if (previousMonthItem != null) {
            newInventory.setItem(17, previousMonthItem);
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
        if (itemProperties == null) {
            return null;
        }

        String name = Utils.setPlaceholders((String) itemProperties.get(ItemProperties.NAME), date, true);
        org.bukkit.Material material = (org.bukkit.Material) itemProperties.get(ItemProperties.MATERIAL);

        if (material == null) {
            return null;
        }

        int amount;

        try {
            amount = Integer.parseInt(Utils.setPlaceholders(String.valueOf(itemProperties.get(ItemProperties.AMOUNT)), date, true));
        } catch (NumberFormatException ex) {
            amount = 1;
        }

        amount = Math.clamp(amount, 1, 99);

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

        if (Boolean.TRUE.equals(itemProperties.get(ItemProperties.TOGGLE))) {
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
        return date.getLocalDateTime().toLocalDate().isAfter(currentDate.getLocalDateTime().toLocalDate());
    }

    private int getInventorySize(RealTimeDate date) {
        date = new RealTimeDate(date);

        int slots = 0;

        double daysPerMonth = date.getLocalDateTime().getMonth().length(false);
        int dayOfWeek = date.getLocalDateTime().withDayOfMonth(1).get(ChronoField.DAY_OF_WEEK);
        boolean firstDaySunday = Boolean.TRUE.equals(VenturaCalendar.getInstance()
                .getTimeConfig().getBoolean("main-time-system.real-time.first-day-sunday"));
        double firstWeekDay = firstDaySunday ? dayOfWeek % 7 : (dayOfWeek + 6) % 7;

        double weeksPerMonth = Math.ceil((daysPerMonth + firstWeekDay) / 7);

        for (int week = 1; week <= weeksPerMonth; week++) {
            slots = slots + 9;
        }

        Object configuredSize = VenturaCalendar.getInstance().getCalendarConfig()
                .getCalendarProperties(false).get(InventoryProperties.SIZE);
        int minimumSize = configuredSize instanceof Number number ? number.intValue() : 18;
        minimumSize = Math.clamp(((Math.max(9, minimumSize) + 8) / 9) * 9, 18, 54);

        return slots > 54 ? 54 : Math.min(54, Math.max(slots, minimumSize));
    }
}
