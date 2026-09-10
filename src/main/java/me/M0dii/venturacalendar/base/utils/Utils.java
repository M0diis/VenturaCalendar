package me.m0dii.venturacalendar.base.utils;

import com.cryptomorin.xseries.XMaterial;
import me.clip.placeholderapi.PlaceholderAPI;
import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])");

    private Utils() {
        // Utility class, no instantiation allowed
    }

    private static VenturaCalendar plugin() {
        VenturaCalendar instance = VenturaCalendar.getInstance();

        if (instance == null) {
            throw new IllegalStateException("VenturaCalendar is not enabled.");
        }

        return instance;
    }

    public static String format(String text) {
        if (text == null || text.isEmpty())
            return "";

        return ChatColor.translateAlternateColorCodes(
                '&',
                HEX_PATTERN.matcher(text).replaceAll("&x&$1&$2&$3&$4&$5&$6")
        );
    }

    public static Material getMaterial(String mat) {
        if (mat == null || mat.isBlank()) {
            return null;
        }

        mat = mat.trim();
        Material m = Material.getMaterial(mat);

        if (mat.contains(":")) {
            String[] split = mat.split(":");

            if (split.length == 2) {
                Optional<XMaterial> xm;

                try {
                    xm = XMaterial.matchXMaterial(Integer.parseInt(split[0]), Byte.parseByte(split[1]));
                } catch (NumberFormatException ex) {
                    xm = Optional.empty();
                }

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

        VenturaCalendar plugin = plugin();

        message = message
                .replaceAll("%[sS]econd(|s)%", Matcher.quoteReplacement(String.valueOf(date.getSecond())))
                .replaceAll("%[mM]inute(|s)%", Matcher.quoteReplacement(String.valueOf(date.getMinute())))
                .replaceAll("%[hH]our(|s)%", Matcher.quoteReplacement(String.valueOf(date.getHour())))
                .replaceAll("%[dD]ay(|s)%", Matcher.quoteReplacement(String.valueOf(date.getDay())))
                .replaceAll("%[wW]eek(|s)%", Matcher.quoteReplacement(String.valueOf(date.getWeek())))
                .replaceAll("%[mM]onth(|s)%", Matcher.quoteReplacement(String.valueOf(date.getMonth())))
                .replaceAll("%[yY]ear(|s)%", Matcher.quoteReplacement(String.valueOf(date.getYear())))
                .replaceAll("%[eE]ra(|s)%", Matcher.quoteReplacement(String.valueOf(date.getEra())));

        String eventName = "";
        String eventDesc = "";

        for (MonthEvent event : plugin.getEventConfig().getEvents()) {
            if (event.includesDate(date)) {
                eventName = event.getEventDisplayName();
                eventDesc = String.join("\n", event.getDescription());
            }
        }

        List<String> dayNames = plugin.getBaseConfig().getListString("translations.real-time.day-names");
        List<String> monthNames = plugin.getBaseConfig().getListString("translations.real-time.month-names");
        List<String> seasonNames = plugin.getBaseConfig().getListString("translations.real-time.season-names");

        int dayIndex = date.getLocalDateTime().getDayOfWeek().getValue() - 1;
        int monthIndex = date.getLocalDateTime().getMonthValue() - 1;
        String dayName = dayIndex < dayNames.size() ? dayNames.get(dayIndex) : "";
        String monthName = monthIndex < monthNames.size() ? monthNames.get(monthIndex) : "";

        String seasonName = "";

        if (seasonNames.size() >= 4) {
            int seasonMonthIndex = (int) date.getMonth() - 1;

            if (seasonMonthIndex < 3 || seasonMonthIndex > 10) {
                seasonName = seasonNames.get(3);
            } else if (seasonMonthIndex < 6) {
                seasonName = seasonNames.getFirst();
            } else if (seasonMonthIndex < 9) {
                seasonName = seasonNames.get(1);
            } else {
                seasonName = seasonNames.get(2);
            }
        }

        message = message
                .replaceAll("%[dD]ay(_|)[nN]ame%", Matcher.quoteReplacement(dayName))
                .replaceAll("%[mM]onth(_|)[nN]ame%", Matcher.quoteReplacement(monthName))
                .replaceAll("%[sS]eason(_|)[nN]ame%", Matcher.quoteReplacement(seasonName))
                .replaceAll("%[eE]vent(_|)[nN]ame%", Matcher.quoteReplacement(eventName))
                .replaceAll("%[eE]vent(_|)[dD]escription%", Matcher.quoteReplacement(eventDesc))
                .replaceAll("%[yY]ears(_|)[pP]assed%", Matcher.quoteReplacement(String.valueOf(date.getYear())));

        if (papi && plugin.papiEnabled()) {
            message = PlaceholderAPI.setPlaceholders(p, message);
        }

        return message;
    }

    public static String setPlaceholders(String message, VenturaCalendarDate venturaCalendarDate, boolean papi, Player p) {
        if (message == null || message.isEmpty()) {
            Messenger.log(Messenger.Level.DEBUG, "Message is empty when setting placeholders, skipping.");
            return "";
        }

        VenturaCalendar plugin = plugin();
        DateUtils du = plugin.getDateUtils();

        venturaCalendarDate = VenturaCalendarDate.clone(venturaCalendarDate);
        TimeSystem timeSystem = TimeSystem.of(venturaCalendarDate.getTimeSystem());
        venturaCalendarDate = du.addZeroPoints(venturaCalendarDate);

        message = message
                .replaceAll("%[tT]ick(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getTick())))
                .replaceAll("%[sS]econd(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getSecond())))
                .replaceAll("%[mM]inute(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getMinute())))
                .replaceAll("%[hH]our(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getHour())))
                .replaceAll("%[dD]ay(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getDay())))
                .replaceAll("%[wW]eek(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getWeek())))
                .replaceAll("%[mM]onth(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getMonth())))
                .replaceAll("%[yY]ear(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getYear())))
                .replaceAll("%[eE]ra(|s)%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getEra())));

        if (p != null) {
            message = message.replaceAll("%world_ticks%", Matcher.quoteReplacement(String.valueOf(p.getWorld().getFullTime())));
        }

        venturaCalendarDate = du.removeZeroPoints(venturaCalendarDate);

        String eventName = "";
        String eventDesc = "";

        for (MonthEvent event : plugin.getEventConfig().getEvents()) {
            if (event.includesDate(venturaCalendarDate)) {
                eventName = event.getEventDisplayName();
                eventDesc = String.join("\n", event.getDescription());
            }
        }

        message = message
                .replaceAll("%[dD]ay(_|)[nN]ame%", Matcher.quoteReplacement(venturaCalendarDate.getDayName()))
                .replaceAll("%[eE]vent(_|)[nN]ame%", Matcher.quoteReplacement(eventName))
                .replaceAll("%[eE]vent(_|)[dD]escription%", Matcher.quoteReplacement(eventDesc))
                .replaceAll("%[mM]onth(_|)[nN]ame%", Matcher.quoteReplacement(venturaCalendarDate.getMonthName()))
                .replaceAll("%[sS]eason(_|)[nN]ame%", Matcher.quoteReplacement(venturaCalendarDate.getSeasonName()))
                .replaceAll("%[eE]ra(_|)[nN]ame%", Matcher.quoteReplacement(venturaCalendarDate.getEraName()))
                .replaceAll("%[tT]ime[sS]ystem(_|)[nN]ame%", Matcher.quoteReplacement(String.valueOf(timeSystem.getName())))
                .replaceAll("%[tT]ime[sS]ystem(_|)[wW]orld%", Matcher.quoteReplacement(String.valueOf(timeSystem.getWorldName())))
                .replaceAll("%[yY]ears(_|)[pP]assed%", Matcher.quoteReplacement(String.valueOf(venturaCalendarDate.getYear())));

        if (papi && plugin.papiEnabled())
            message = PlaceholderAPI.setPlaceholders(p, message);

        return message;
    }

    public static void sendCommand(Player player, String cmd) {
        VenturaCalendar plugin = plugin();
        cmd = cmd.replaceAll("%([pP]layer|[pP]layer(_|.*)[nN]ame)%", Matcher.quoteReplacement(player.getName()));

        if (plugin.papiEnabled()) {
            cmd = PlaceholderAPI.setPlaceholders(player, cmd);
        }

        cmd = format(cmd);

        if (cmd.startsWith("[")) {
            int closingBracket = cmd.indexOf(']');

            if (closingBracket < 0) {
                Bukkit.dispatchCommand(player, cmd);
                return;
            }

            String sendAs = cmd.substring(0, closingBracket + 1);
            cmd = cmd.substring(closingBracket + 1).stripLeading();

            if (sendAs.equalsIgnoreCase("[MESSAGE]") || sendAs.equalsIgnoreCase("[TEXT]")) {
                player.sendMessage(cmd);
            } else if (sendAs.equalsIgnoreCase("[TITLE]")) {
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
                    } catch (NumberFormatException ex) {
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
                    } catch (NumberFormatException ex) {
                        Messenger.log(Messenger.Level.WARN, "Invalid fadeIn, stay, or fadeOut time for title action.");
                        Messenger.log(Messenger.Level.DEBUG, ex.getMessage());
                    }

                    player.sendTitle(split[0], subtitle, fadeIn, stay, fadeOut);
                }
            } else if (sendAs.equalsIgnoreCase("[CHAT]")) {
                player.chat(cmd);
            } else if (sendAs.equalsIgnoreCase("[SOUND]")) {
                String[] split = cmd.split(", ");

                if (split.length == 2 || split.length == 3) {
                    try {
                        String soundName = split[0].trim().toLowerCase(Locale.ROOT);
                        float volume = Float.parseFloat(split[1].trim());
                        float pitch = split.length == 3 ? Float.parseFloat(split[2].trim()) : 1.0F;
                        Sound sound = null;
                        String normalizedSoundName = soundName.replace('.', '_');

                        for (Sound candidate : Registry.SOUNDS) {
                            if (Registry.SOUNDS.getKey(candidate).getKey().replace('.', '_').equals(normalizedSoundName)) {
                                sound = candidate;
                                break;
                            }
                        }

                        if (sound == null) {
                            throw new IllegalArgumentException("Unknown sound: " + soundName);
                        }

                        player.playSound(player.getLocation(), sound, volume, pitch);
                    } catch (Exception ex) {
                        Messenger.log(Messenger.Level.WARN, "Invalid sound format: " + cmd);
                        Messenger.log(Messenger.Level.DEBUG, ex.getMessage());
                    }
                } else {
                    Messenger.log(Messenger.Level.WARN, "Invalid sound format: " + cmd);
                }
            } else if (sendAs.equalsIgnoreCase("[PLAYER]")) {
                Bukkit.dispatchCommand(player, cmd);
            } else if (sendAs.equalsIgnoreCase("[CONSOLE]")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        } else Bukkit.dispatchCommand(player, cmd);
    }

    public static long getTicksFromTime(String time) {
        if (time == null || time.length() < 2) {
            return -1;
        }

        String normalized = time.trim().toLowerCase(Locale.ROOT);

        if (normalized.length() < 2) {
            return -1;
        }

        char unit = normalized.charAt(normalized.length() - 1);
        long value;

        try {
            value = Long.parseLong(normalized.substring(0, normalized.length() - 1));
        } catch (NumberFormatException ex) {
            Messenger.log(Messenger.Level.DEBUG, "Invalid time format: " + time);
            return -1;
        }

        if (value < 0) {
            return -1;
        }

        VenturaCalendar plugin = plugin();
        TimeSystem timeSystem = plugin.getTimeConfig().getTimeSystem();
        long ticksPerSecond = timeSystem.getTicksPerSecond();
        try {
            long multiplier = switch (unit) {
                case 's' -> 1L;
                case 'm' -> timeSystem.getSecondsPerMinute();
                case 'h' -> Math.multiplyExact(timeSystem.getSecondsPerMinute(), timeSystem.getMinutesPerHour());
                case 'd' -> Math.multiplyExact(
                        Math.multiplyExact(timeSystem.getSecondsPerMinute(), timeSystem.getMinutesPerHour()),
                        timeSystem.getHoursPerDay());
                case 'w' -> Math.multiplyExact(
                        Math.multiplyExact(
                                Math.multiplyExact(timeSystem.getSecondsPerMinute(), timeSystem.getMinutesPerHour()),
                                timeSystem.getHoursPerDay()),
                        timeSystem.getDaysPerWeek());
                default -> 0L;
            };

            if (multiplier <= 0) {
                return -1;
            }

            return Math.multiplyExact(Math.multiplyExact(value, multiplier), ticksPerSecond);
        } catch (ArithmeticException ex) {
            return -1;
        }
    }
}
