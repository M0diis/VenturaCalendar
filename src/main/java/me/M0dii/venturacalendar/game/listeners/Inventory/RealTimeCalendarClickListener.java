package me.m0dii.venturacalendar.game.listeners.inventory;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.EventDays;
import me.m0dii.venturacalendar.base.dateutils.RealTimeDate;
import me.m0dii.venturacalendar.base.events.RealTimeCalendarClickEvent;
import me.m0dii.venturacalendar.base.events.RealTimeCalendarOpenEvent;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.config.BaseConfig;
import me.m0dii.venturacalendar.game.config.Messages;
import me.m0dii.venturacalendar.game.gui.RealTimeCalendar;
import me.m0dii.venturacalendar.game.listeners.NewDayListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.Map;

public class RealTimeCalendarClickListener implements Listener {
    private final VenturaCalendar plugin;
    private final BaseConfig baseConfig;

    public RealTimeCalendarClickListener(VenturaCalendar plugin) {
        this.plugin = plugin;
        this.baseConfig = plugin.getBaseConfig();
    }

    @EventHandler
    public void onNewDay(final RealTimeCalendarClickEvent e) {
        if (e.isCancelled()) {
            return;
        }

        RealTimeCalendar cal = e.getCalendar();

        if (cal.getDate() == null) {
            return;
        }

        ItemStack item = e.getItem();

        Player player = e.getPlayer();

        if (e.getInventoryClickEvent() == null) {
            return;
        }

        int slot = e.getInventoryClickEvent().getSlot();

        LocalDateTime currentLocalDateTime = cal.getDate().getLocalDateTime();

        if (slot == 8 || slot == 17) {
            if (slot == 8) {
                RealTimeDate nextMonthDate = new RealTimeDate(cal.getDate().getEra(), currentLocalDateTime.plusMonths(1));

                RealTimeCalendar nextMonth = new RealTimeCalendar(nextMonthDate, cal.getCreationDate());
                RealTimeCalendarOpenEvent openEvent = new RealTimeCalendarOpenEvent(nextMonth, nextMonth.getInventory(), player);

                Bukkit.getPluginManager().callEvent(openEvent);

                if (!openEvent.isCancelled()) {
                    player.openInventory(nextMonth.getInventory());
                }
            }
            if (slot == 17) {
                RealTimeDate prevMonthDate = new RealTimeDate(cal.getDate().getEra(), currentLocalDateTime.minusMonths(1));

                RealTimeCalendar nextMonth = new RealTimeCalendar(prevMonthDate, cal.getCreationDate());
                RealTimeCalendarOpenEvent openEvent = new RealTimeCalendarOpenEvent(nextMonth, nextMonth.getInventory(), player);

                Bukkit.getPluginManager().callEvent(openEvent);

                if (!openEvent.isCancelled()) {
                    player.openInventory(nextMonth.getInventory());
                }
            }

            return;
        }

        if (slot != cal.getTodaySlot()) {
            return;
        }

        if (item == null || item.getType().isAir()) {
            return;
        }

        Map<String, EventDays> redeemableMonths = baseConfig.getRedeemableMonths();

        if (baseConfig.redeemWhitelistEnabled()) {
            EventDays eventDays = redeemableMonths.get(currentLocalDateTime.getMonth().name().toLowerCase(java.util.Locale.ROOT));

            if (eventDays == null) {
                return;
            }

            long day = cal.getDate().getDay() + 1;

            if (!eventDays.includes((int) day)) {
                return;
            }
        }

        if (!baseConfig.rewardsEnabled()) {
            return;
        }

        if (NewDayListener.redeem(player, plugin)) {
            for (String cmd : baseConfig.getListString("rewards.commands")) {
                Utils.sendCommand(player, cmd);
            }
        } else {
            Messenger.send(player, Messages.REDEEMED);
        }
    }
}
