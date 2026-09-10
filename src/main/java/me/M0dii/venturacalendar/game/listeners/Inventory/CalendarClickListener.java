package me.m0dii.venturacalendar.game.listeners.inventory;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.EventDays;
import me.m0dii.venturacalendar.base.dateutils.DateEnum;
import me.m0dii.venturacalendar.base.events.CalendarOpenEvent;
import me.m0dii.venturacalendar.base.events.CalendarClickEvent;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.config.BaseConfig;
import me.m0dii.venturacalendar.game.config.Messages;
import me.m0dii.venturacalendar.game.gui.Calendar;
import me.m0dii.venturacalendar.game.listeners.NewDayListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CalendarClickListener implements Listener {
    private final VenturaCalendar plugin;
    private final BaseConfig baseConfig;

    public CalendarClickListener(VenturaCalendar plugin) {
        this.plugin = plugin;
        this.baseConfig = plugin.getBaseConfig();
    }

    @EventHandler
    public void onNewDay(final CalendarClickEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Calendar cal = e.getCalendar();

        if (cal.getDate() == null) {
            return;
        }

        if (e.getSlot() == 8 || e.getSlot() == 17) {
            var monthStart = plugin.getDateUtils().down(DateEnum.DAY,
                    (int) cal.getDate().getDay(), cal.getDate());
            var targetDate = e.getSlot() == 8
                    ? plugin.getDateUtils().up(DateEnum.MONTH, 1, monthStart)
                    : plugin.getDateUtils().down(DateEnum.MONTH, 1, monthStart);
            Calendar nextCalendar = new Calendar(targetDate, cal.getCreationDate(), plugin);
            CalendarOpenEvent openEvent = new CalendarOpenEvent(nextCalendar, nextCalendar.getInventory(), e.getPlayer());

            Bukkit.getPluginManager().callEvent(openEvent);

            if (!openEvent.isCancelled()) {
                e.getPlayer().openInventory(nextCalendar.getInventory());
            }

            return;
        }

        if (e.getSlot() != cal.getTodaySlot()) {
            return;
        }

        Map<String, EventDays> redeemableMonths = baseConfig.getRedeemableMonths();

        if (baseConfig.redeemWhitelistEnabled()) {
            EventDays eventDays = redeemableMonths.get(cal.getDate().getMonthName().toLowerCase(java.util.Locale.ROOT));

            if (eventDays == null) {
                return;
            }

            long day = cal.getDate().getDay() + 1;

            if (!eventDays.includes((int) day)) {
                return;
            }
        }

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if (item == null || item.getType().isAir()) {
            return;
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
