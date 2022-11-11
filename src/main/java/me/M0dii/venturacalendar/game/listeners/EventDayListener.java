package me.m0dii.venturacalendar.game.listeners;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.events.MonthEventDayEvent;
import me.m0dii.venturacalendar.base.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventDayListener implements Listener {
    private final VenturaCalendar plugin;

    public EventDayListener(VenturaCalendar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEventDay(MonthEventDayEvent e) {
        if (e.isCancelled()) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            for (String cmd : e.getMonthEvent().getCommands()) {
                Bukkit.getScheduler().runTask(this.plugin, () -> Utils.sendCommand(p, cmd));
            }
        }
    }
}
