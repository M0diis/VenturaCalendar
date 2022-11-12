package me.m0dii.venturacalendar.game.listeners.inventory;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.EventDays;
import me.m0dii.venturacalendar.base.events.RealTimeCalendarClickEvent;
import me.m0dii.venturacalendar.base.itemutils.ItemProperties;
import me.m0dii.venturacalendar.base.itemutils.Items;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.config.BaseConfig;
import me.m0dii.venturacalendar.game.config.Messages;
import me.m0dii.venturacalendar.game.gui.InventoryProperties;
import me.m0dii.venturacalendar.game.gui.RealTimeCalendar;
import me.m0dii.venturacalendar.game.listeners.NewDayListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
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

        if (cal.getDate() != null) {
            return;
        }

        HashMap<String, EventDays> redeemableMonths = baseConfig.getRedeemableMonths();

        if (cal.getDate() != null && baseConfig.redeemWhitelistEnabled()) {
            EventDays eventDays = redeemableMonths.get(cal.getDate().getLocalDateTime().getMonth().name());

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

        Map<Items, HashMap<ItemProperties, Object>> itemProperties =
                (Map<Items, HashMap<ItemProperties, Object>>)
                        plugin.getCalendarConfig().getCalendarProperties(false)
                                .get(InventoryProperties.ITEMS);

        Map<ItemProperties, Object> today = itemProperties.get(Items.TODAY);

        Material m = (Material) today.get(ItemProperties.MATERIAL);

        if (item == null || !m.equals(item.getType())) {
            return;
        }

        if (!baseConfig.getBoolean("rewards.enabled")) {
            return;
        }

        if (NewDayListener.redeem(player.getUniqueId())) {
            for (String cmd : baseConfig.getListString("rewards.commands")) {
                Utils.sendCommand(player, cmd);
            }
        }
        else {
            Messenger.send(player, Messages.REDEEMED);
        }
    }
}
