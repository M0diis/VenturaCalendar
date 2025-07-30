package me.m0dii.venturacalendar.base.utils;

import com.cryptomorin.xseries.XMaterial;
import me.clip.placeholderapi.PlaceholderAPI;
import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.*;
import me.m0dii.venturacalendar.base.dateutils.VenturaCalendarDate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class Utils {
    private Utils() {
        // Utility class, no instantiation allowed
    }

    private static final VenturaCalendar plugin = VenturaCalendar.getInstance();

    private static final String[] MONTHS = new String[] {
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])");

    public static String format(String text) {
        if (text == null || text.isEmpty())
            return "";

        return ChatColor.translateAlternateColorCodes(
                '&',
                HEX_PATTERN.matcher(text).replaceAll("&x&$1&$2&$3&$4&$5&$6")
        );
    }

    public static Material getMaterial(String mat) {
        Material m = Material.getMaterial(mat);

        if (mat.contains(":")) {
            String[] split = mat.split(":");

            if (split.length == 2) {
                Optional<XMaterial> xm = XMaterial.matchXMaterial(Integer.parseInt(split[0]), Byte.parseByte(split[1]));

                if (xm.isPresent()) {
                    m = xm.get().parseMaterial();
                }
            }
        }

        if (m == null) {
            Optional<XMaterial> xm = XMaterial.matchXMaterial(mat);

            if (xm.isPresent()) {
                m = xm.get().parseMaterial();
            }
        }

        if (m == null) {
            m = Material.getMaterial(mat, true);
        }

        return m;
    }

    public static String setPlaceholders(String message, VenturaCalendarDate venturaCalendarDate, boolean papi) {
        return setPlaceholders(message, venturaCalendarDate, papi, null);
    }

    public static String setPlaceholders(String message, VenturaCalendarDate venturaCalendarDate, Player p) {
        return setPlaceholders(message, venturaCalendarDate, true, p);
    }

    public static String setPlaceholders(String message, RealTimeDate date, boolean papi) {
        return setPlaceholders(message, date, papi, null);
    }

    public static String setPlaceholders(String message, RealTimeDate date, Player p) {
        return setPlaceholders(message, date, true, p);
    }

    public static String setPlaceholders(String message, RealTimeDate date, boolean papi, Player p) {
        date = new RealTimeDate(date);

        if (message == null || message.isEmpty()) {
            Messenger.log(Messenger.Level.DEBUG, "Message is empty when setting placeholders, skipping.");
            return "";
        }

        message = message
                .replaceAll("%[sS]econd(|s)%", String.valueOf(date.getSecond()))
                .replaceAll("%[mM]inute(|s)%", String.valueOf(date.getMinute()))
                .replaceAll("%[hH]our(|s)%", String.valueOf(date.getHour()))
                .replaceAll("%[dD]ay(|s)%", String.valueOf(date.getDay()))
                .replaceAll("%[wW]eek(|s)%", String.valueOf(date.getWeek()))
                .replaceAll("%[mM]onth(|s)%", String.valueOf(date.getMonth()))
                .replaceAll("%[yY]ear(|s)%", String.valueOf(date.getYear()))
                .replaceAll("%[eE]ra(|s)%", String.valueOf(date.getEra()));

        String eventName = "";
        String eventDesc = "";

        for (MonthEvent event : plugin.getEventConfig().getEvents()) {
            if (event.includesDate(date)) {
                eventName = event.getDisplayName();
                eventDesc = String.join("\n", event.getDescription());
            }
        }

        List<String> dayNames = plugin.getBaseConfig().getListString("translations.real-time.day-names");
        List<String> monthNames = plugin.getBaseConfig().getListString("translations.real-time.month-names");
        List<String> seasonNames = plugin.getBaseConfig().getListString("translations.real-time.season-names");

        String dayName = dayNames.get(date.getLocalDateTime().getDayOfWeek().getValue() - 1);

        String monthName = monthNames.get(date.getLocalDateTime().getMonthValue() - 1);

        String seasonName = "";

        if(date.getMonth() < 3 || date.getMonth() > 10) {
            seasonName = seasonNames.get(3);
        } else if(date.getMonth() < 6) {
            seasonName = seasonNames.get(0);
        } else if(date.getMonth() < 9) {
            seasonName = seasonNames.get(1);
        } else {
            seasonName = seasonNames.get(2);
        }

        message = message
                .replaceAll("%[dD]ay(_|)[nN]ame%", dayName)
                .replaceAll("%[mM]onth(_|)[nN]ame%", monthName)
                .replaceAll("%[sS]eason(_|)[nN]ame%", seasonName)
                .replaceAll("%[eE]vent(_|)[nN]ame%", eventName)
                .replaceAll("%[eE]vent(_|)[dD]escription%", eventDesc)
                .replaceAll("%[yY]ears(_|)[pP]assed%", String.valueOf(date.getYear()));

        if (papi && plugin.papiEnabled()) {
            PlaceholderAPI.setPlaceholders(p, message);
        }

        return message;
    }

    public static String setPlaceholders(String message, VenturaCalendarDate venturaCalendarDate, boolean papi, Player p) {
        DateUtils du = plugin.getDateUtils();

        venturaCalendarDate = new VenturaCalendarDate(venturaCalendarDate);
        TimeSystem timeSystem = TimeSystem.of(venturaCalendarDate.getTimeSystem());
        venturaCalendarDate = du.addZeroPoints(venturaCalendarDate);

        if (message == null || message.isEmpty()) {
            Messenger.log(Messenger.Level.DEBUG, "Message is empty when setting placeholders, skipping.");
            return "";
        }

        message = message
                .replaceAll("%[tT]ick(|s)%", String.valueOf(venturaCalendarDate.getTick()))
                .replaceAll("%[sS]econd(|s)%", String.valueOf(venturaCalendarDate.getSecond()))
                .replaceAll("%[mM]inute(|s)%", String.valueOf(venturaCalendarDate.getMinute()))
                .replaceAll("%[hH]our(|s)%", String.valueOf(venturaCalendarDate.getHour()))
                .replaceAll("%[dD]ay(|s)%", String.valueOf(venturaCalendarDate.getDay()))
                .replaceAll("%[wW]eek(|s)%", String.valueOf(venturaCalendarDate.getWeek()))
                .replaceAll("%[mM]onth(|s)%", String.valueOf(venturaCalendarDate.getMonth()))
                .replaceAll("%[yY]ear(|s)%", String.valueOf(venturaCalendarDate.getYear()))
                .replaceAll("%[eE]ra(|s)%", String.valueOf(venturaCalendarDate.getEra()));

        if(p != null) {
            message = message.replaceAll("%world_ticks%", String.valueOf(p.getWorld().getFullTime()));
        }

        venturaCalendarDate = du.removeZeroPoints(venturaCalendarDate);

        String eventName = "";
        String eventDesc = "";

        for (MonthEvent event : plugin.getEventConfig().getEvents()) {
            if (event.includesDate(venturaCalendarDate)) {
                eventName = event.getDisplayName();
                eventDesc = String.join("\n", event.getDescription());
            }
        }

        message = message
                .replaceAll("%[dD]ay(_|)[nN]ame%", venturaCalendarDate.getDayName())
                .replaceAll("%[eE]vent(_|)[nN]ame%", eventName)
                .replaceAll("%[eE]vent(_|)[dD]escription%", eventDesc)
                .replaceAll("%[mM]onth(_|)[nN]ame%", venturaCalendarDate.getMonthName())
                .replaceAll("%[sS]eason(_|)[nN]ame%", venturaCalendarDate.getSeasonName())
                .replaceAll("%[eE]ra(_|)[nN]ame%", venturaCalendarDate.getEraName())
                .replaceAll("%[tT]ime[sS]ystem(_|)[nN]ame%", timeSystem.getName())
                .replaceAll("%[tT]ime[sS]ystem(_|)[wW]orld%", timeSystem.getWorldName())
                .replaceAll("%[yY]ears(_|)[pP]assed%", String.valueOf(venturaCalendarDate.getYear()));

        if (papi && plugin.papiEnabled())
            PlaceholderAPI.setPlaceholders(p, message);

        return message;
    }

    public static void sendCommand(Player player, String cmd) {
        cmd = cmd.replaceAll("%([pP]layer|[pP]layer(_|.*)[nN]ame)%", player.getName());

        if (plugin.papiEnabled()) {
            cmd = PlaceholderAPI.setPlaceholders(player, cmd);
        }

        cmd = format(cmd);

        if (cmd.startsWith("[")) {
            String sendAs = cmd.substring(cmd.indexOf("["), cmd.indexOf("]") + 1);

            cmd = cmd.substring(cmd.indexOf("]") + 2);

            if (sendAs.equalsIgnoreCase("[MESSAGE]") || sendAs.equalsIgnoreCase("[TEXT]")) {
                player.sendMessage(cmd);
            }
            else if (sendAs.equalsIgnoreCase("[TITLE]")) {
                String[] split = cmd.split(", ");

                int fadeIn = 20;
                int stay = 60;
                int fadeOut = 20;

                if (split.length == 1) {
                    String title = split[0];

                    player.sendTitle(title, "", fadeIn, stay, fadeOut);

                    return;
                }

                if (split.length == 2) {
                    String title = split[0];
                    String subtitle = split[1];

                    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);

                    return;
                }

                if (split.length == 4) {
                    try {
                        fadeIn = Integer.parseInt(split[1]);
                        stay = Integer.parseInt(split[2]);
                        fadeOut = Integer.parseInt(split[3]);
                    }
                    catch (NumberFormatException ex) {
                        Messenger.log(Messenger.Level.WARN, "Invalid fade-in, stay, or fade-out time for title action.");
                        Messenger.log(Messenger.Level.DEBUG, ex.getMessage());
                    }

                    player.sendTitle(split[0], "", fadeIn, stay, fadeOut);

                    return;
                }

                if (split.length == 5) {
                    String subtitle = split[1];

                    try {
                        fadeIn = Integer.parseInt(split[2]);
                        stay = Integer.parseInt(split[3]);
                        fadeOut = Integer.parseInt(split[4]);
                    }
                    catch (NumberFormatException ex) {
                        Messenger.log(Messenger.Level.WARN, "Invalid fadeIn, stay, or fadeOut time for title action.");
                        Messenger.log(Messenger.Level.DEBUG, ex.getMessage());
                    }

                    player.sendTitle(split[0], subtitle, fadeIn, stay, fadeOut);
                }
            }
            else if (sendAs.equalsIgnoreCase("[CHAT]")) {
                player.chat(cmd);
            }
            else if (sendAs.equalsIgnoreCase("[SOUND]")) {
                String[] split = cmd.split(", ");

                if (split.length == 2) {
                    try {
                        player.playSound(player.getLocation(), Sound.valueOf(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[1]));
                    }
                    catch (Exception ex) {
                        Messenger.log(Messenger.Level.WARN, "Invalid sound format: " + cmd);
                        Messenger.log(Messenger.Level.DEBUG, ex.getMessage());
                    }
                }
            }
            else if (sendAs.equalsIgnoreCase("[PLAYER]")) {
                Bukkit.dispatchCommand(player, cmd);
            }
            else if (sendAs.equalsIgnoreCase("[CONSOLE]")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }
        else Bukkit.dispatchCommand(player, cmd);
    }

    public static int getTicksFromTime(String time) {
        int value = 0;

        try {
            value = Integer.parseInt(time.substring(0, time.length() - 1));
        }
        catch (NumberFormatException ex) {
            Messenger.log(Messenger.Level.DEBUG, "Invalid time format: " + time);
        }

        int ticksPerSecond = (int) plugin.getTimeConfig().getTimeSystem().getTicksPerSecond();

        return switch (time.charAt(time.length() - 1)) {
            case 's' -> value * ticksPerSecond;
            case 'm' -> value * 60 * ticksPerSecond;
            case 'h' -> value * 3600 * ticksPerSecond;
            case 'd' -> value * 86400 * ticksPerSecond;
            default -> value;
        };
    }
}
