package me.m0dii.venturacalendar.game.listeners;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.*;
import me.m0dii.venturacalendar.base.events.MonthEventDayEvent;
import me.m0dii.venturacalendar.base.events.NewDayEvent;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class NewDayListener implements Listener {
    private final VenturaCalendar plugin;

    public NewDayListener(VenturaCalendar plugin) {
        this.plugin = plugin;
    }

    public static boolean redeem(Player player, VenturaCalendar plugin) {
        String dateKey = plugin.getRewardDateKey(player);
        if (dateKey == null) {
            return false;
        }

        NamespacedKey key = new NamespacedKey(plugin, "daily_reward_date");
        String previousDate = player.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (dateKey.equals(previousDate)) {
            return false;
        }

        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, dateKey);

        return true;
    }

    @EventHandler
    public void onNewDay(final NewDayEvent e) {
        if (e.isCancelled()) {
            return;
        }

        TimeSystem ts = e.getTimeSystem();
        World w = e.getWorld();

        if (w == null) {
            return;
        }

        VenturaCalendarDate venturaCalendarDate = e.getDate();
        RealTimeDate realTimeDate = DateCalculator.realTimeNow();

        if (ts.isRealTime()) {
            for (MonthEvent event : plugin.getEventConfig().getEvents()) {
                if (event.includesDate(realTimeDate)) {
                    Bukkit.getPluginManager().callEvent(new MonthEventDayEvent(ts, w, event));
                }
            }
        } else {
            for (MonthEvent event : plugin.getEventConfig().getEvents()) {
                if (event.includesDate(venturaCalendarDate)) {
                    Bukkit.getPluginManager().callEvent(new MonthEventDayEvent(ts, w, event));
                }
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if ("current".equalsIgnoreCase(ts.getWorldName()) && !p.getWorld().equals(w)) {
                continue;
            }

            for (String cmd : plugin.getBaseConfig().getNewDayCommands()) {
                Utils.sendCommand(p, cmd);
            }

            if (plugin.getBaseConfig().getNewDayMessage().isPresent()) {
                String base = plugin.getBaseConfig().getNewDayMessage().get();
                String msg = "";

                if (ts.isRealTime()) {
                    msg = Utils.setPlaceholders(base, realTimeDate, p);
                } else {
                    msg = Utils.setPlaceholders(base, venturaCalendarDate, p);
                }

                p.sendMessage(msg);
            }

            if (plugin.getBaseConfig().titleEnabled()) {
                String title = plugin.getBaseConfig().getMessage(Messages.TITLE_TEXT);
                String subtitle = plugin.getBaseConfig().getMessage(Messages.SUBTITLE_TEXT);

                int fadein = plugin.getBaseConfig().getInteger("new-day.title.fade-in");
                int stay = plugin.getBaseConfig().getInteger("new-day.title.stay");
                int fadeout = plugin.getBaseConfig().getInteger("new-day.title.fade-out");

                if (ts.isRealTime()) {
                    title = Utils.setPlaceholders(title, realTimeDate, p);
                } else {
                    title = Utils.setPlaceholders(title, venturaCalendarDate, p);
                }

                if (ts.isRealTime()) {
                    subtitle = Utils.setPlaceholders(subtitle, realTimeDate, p);
                } else {
                    subtitle = Utils.setPlaceholders(subtitle, venturaCalendarDate, p);
                }

                p.sendTitle(title, subtitle, fadein, stay, fadeout);
            }
        }
    }
}
