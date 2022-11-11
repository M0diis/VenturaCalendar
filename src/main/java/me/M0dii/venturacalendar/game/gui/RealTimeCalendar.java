package me.m0dii.venturacalendar.game.gui;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.MonthEvent;
import me.m0dii.venturacalendar.base.dateutils.realtime.RealTimeDate;
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

public class RealTimeCalendar implements InventoryHolder {
    private final RealTimeDate date;

    private RealTimeDate creationDate;

    private final Inventory inventory;

    private final List<MonthEvent> events;

    public RealTimeCalendar(RealTimeDate date) {
        date = new RealTimeDate(date);
        creationDate = new RealTimeDate(date);

        this.date = date;

        this.events = VenturaCalendar.getInstance().getEventConfig().getEvents();

        this.inventory = createInventory(date);
    }

    public RealTimeDate getDate() {
        return this.date;
    }

    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    private Inventory createInventory(RealTimeDate date) {
        date = new RealTimeDate(date);
        creationDate = new RealTimeDate(date);

        Map<InventoryProperties, Object> calendarProperties = VenturaCalendar.getInstance()
                .getCalendarConfig().getCalendarProperties(false);

        String title = Utils.setPlaceholders((String) calendarProperties.get(InventoryProperties.HEADER), date, true);

        Inventory inventory = Bukkit.createInventory(this, getInventorySize(date), title);

        double daysPerMonth = date.getLocalDateTime().getMonth().length(false);

        int firstWeekDay = date.getLocalDateTime().withDayOfMonth(1).get(ChronoField.DAY_OF_WEEK);

        if(!VenturaCalendar.getInstance().getTimeConfig().getBoolean("main-time-system.real-time.first-day-sunday")) {
            firstWeekDay--;
        }

        int daysPerWeek = 7;

        double weeksThisMonth = Math.ceil(((daysPerMonth + firstWeekDay) / daysPerWeek));

        int weekSlot = daysPerWeek;
        int daySlot =  firstWeekDay;

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

        for (long week = 0; week <= weeksThisMonth; week++, weekOfMonth++, weekSlot = weekSlot + 9) {
            date.setWeek(weekOfMonth);

            copy = LocalDateTime.of((int)date.getYear(), date.getLocalDateTime().getMonth(), dayOfMonth, date.getLocalDateTime().getHour(), date.getLocalDateTime().getMinute(), date.getLocalDateTime().getSecond());
            date.setLocalDateTime(copy);

            ItemStack weekItem = createItem(weekProps, date, true, null);

            if (weekItem != null && weekSlot < 55) {
                inventory.setItem(weekSlot, weekItem);
            }

            for (long day = 0; day < daysPerWeek; day++, dayOfMonth++, daySlot++) {
                date.setDay(dayOfMonth);

                copy = LocalDateTime.of((int)date.getYear(), date.getLocalDateTime().getMonth(), dayOfMonth, date.getLocalDateTime().getHour(), date.getLocalDateTime().getMinute(), date.getLocalDateTime().getSecond());
                date.setLocalDateTime(copy);

                if (isToday(date, creationDate)) {

                    ItemStack todayItem = createItem(todayProps, date, false, MonthEvent.DisplayType.CURRENT);

                    if (todayItem != null && daySlot < 55) {
                        inventory.setItem(daySlot, todayItem);
                    }
                }
                else if (isFuture(date, creationDate)) {
                    ItemStack dayItem = createItem(futureDayProps, date, false, MonthEvent.DisplayType.FUTURE);

                    if (dayItem != null && daySlot < 55) {
                        inventory.setItem(daySlot, dayItem);
                    }
                }
                else {
                    ItemStack dayItem = createItem(passedDayProps, date, false, MonthEvent.DisplayType.PASSED);

                    if (dayItem != null && daySlot < 55) {
                        inventory.setItem(daySlot, dayItem);
                    }
                }

                if (isEndOfWeek(date, daySlot)) {
                    daySlot++;
                    dayOfMonth++;

                    break;
                }

                if (isEndOfMonth(date)) {
                    week = (int) (weeksThisMonth + 1);
                    day = (int) (daysPerWeek + 1);
                }
            }

            daySlot = (int) (daySlot + (8 - (daysPerWeek - 1)));
        }

        return inventory;
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

        if(week) {
            amount++;
        }

        List<String> lore = new ArrayList<>();

        if (itemProperties.get(ItemProperties.LORE) != null) {
            lore = new ArrayList<>((List<String>) itemProperties.get(ItemProperties.LORE))
                    .stream().map(str -> Utils.setPlaceholders(str, date, true))
                    .collect(Collectors.toList());
        }

        String skullOwner = (String) itemProperties.getOrDefault(ItemProperties.META_SKULL_OWNER, null);

        if (!week) {
            for (MonthEvent event : events) {
                if (event.hasFromTo()) {
                    if (event.includesDate(date)) {
                        material = event.getDisplay(type);

                        lore.add("");
                        lore.addAll(event.getDescription());

                        continue;
                    }
                }

                if (event.hasDayNames()) {
                    if (event.includesDayName(date)) {
                        material = event.getDisplay(type);

                        lore.add("");
                        lore.addAll(event.getDescription());
                    }
                }
            }
        }

        return new ItemCreator(material, amount, name, lore).getItem();
    }

    private boolean isToday(RealTimeDate date, RealTimeDate currentDate) {
        return date.getYear() == currentDate.getYear()
            && date.getMonth() == currentDate.getMonth()
            && date.getDay() == currentDate.getDay();
    }

    private boolean isFuture(RealTimeDate date, RealTimeDate currentDate) {
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
