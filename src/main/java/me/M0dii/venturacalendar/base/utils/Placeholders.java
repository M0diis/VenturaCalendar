package me.m0dii.venturacalendar.base.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.*;
import me.m0dii.venturacalendar.game.config.BaseConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

public class Placeholders extends PlaceholderExpansion {

    private final VenturaCalendar plugin;
    private final BaseConfig baseConfig;

    public Placeholders(VenturaCalendar plugin) {
        this.plugin = plugin;
        this.baseConfig = plugin.getBaseConfig();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return "m0dii";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "venturacalendar";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String id) {
        TimeSystem ts = plugin.getTimeConfig().getTimeSystem();

        if (id.startsWith("event_")) {
            String eventName = id.split("_")[1];

            MonthEvent event = plugin.getEventConfig().getEvent(eventName);

            if (event == null) {
                return "";
            }

            if (id.endsWith("_start") && event.hasFromTo()) {
                return String.valueOf(event.getStartDay());
            }

            if (id.endsWith("_end") && event.hasFromTo()) {
                return String.valueOf(event.getEndDay());
            }

            if (id.endsWith("_description")) {
                return String.join("\n", event.getDescription());
            }
        }

        switch (id) {
            case "newday_message" -> {
                return plugin.getBaseConfig().getNewDayMessage().orElse("");
            }
            case "actionbar_message" -> {
                return plugin.getBaseConfig().getActionBarMessage().orElse("");
            }
        }

        if (ts.isRealTime()) {
            return parseRealTimeDatePlaceholders(DateCalculator.realTimeNow(), id.toLowerCase());
        }

        String worldName = ts.getWorldName();

        World w = Bukkit.getWorld(worldName);

        if (w == null) {
            return "Error: Time-system world not found.";
        }

        return parseDatePlaceholders(DateCalculator.fromTicks(w.getFullTime(), ts), id.toLowerCase());
    }

    private static final String[] MONTHS = new String[]{
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };

    private String parseRealTimeDatePlaceholders(RealTimeDate date, String id) {
        List<String> dayNames = baseConfig.getListString("translations.real-time.day-names");
        List<String> monthNames = baseConfig.getListString("translations.real-time.month-names");
        List<String> seasonNames = baseConfig.getListString("translations.real-time.season-names");

        if (id.startsWith("month_")) {
            if (id.endsWith("_season")) {
                String month = id.split("_")[1];

                int monthIndex = -1;

                for (int i = 0; i < MONTHS.length; i++) {
                    if (MONTHS[i].equalsIgnoreCase(month)) {
                        monthIndex = i;
                        break;
                    }
                }

                if (monthIndex == -1) {
                    return "Error: Invalid month.";
                }

                if (seasonNames.size() < 4) {
                    return "Error: Invalid season names.";
                }

                if (monthIndex < 3 || monthIndex > 10) {
                    return seasonNames.get(3);
                } else if (monthIndex < 6) {
                    return seasonNames.getFirst();
                } else if (monthIndex < 9) {
                    return seasonNames.get(1);
                } else {
                    return seasonNames.get(2);
                }
            }

            if (id.endsWith("_days")) {
                String month = id.split("_")[1];

                int monthIndex = -1;

                for (int i = 0; i < MONTHS.length; i++) {
                    if (MONTHS[i].equalsIgnoreCase(month)) {
                        monthIndex = i;
                        break;
                    }
                }

                if (monthIndex == -1) {
                    return "Error: Invalid month.";
                }

                return switch (monthIndex) {
                    case 1 -> "28";
                    case 3, 5, 8, 10 -> "30";
                    default -> "31";
                };
            }
        }

        switch (id) {
            case "date_event_name" -> {
                for (MonthEvent event : this.plugin.getEventConfig().getEvents()) {
                    if (event.includesDate(date)) {
                        return event.getDisplayName();
                    }
                }
                return "";
            }
            case "date_event_description" -> {
                for (MonthEvent event : this.plugin.getEventConfig().getEvents()) {
                    if (event.includesDate(date)) {
                        return String.join("\n", event.getDescription());
                    }
                }
                return "";
            }
            case "date_second" -> {
                return String.valueOf(date.getSecond());
            }
            case "date_minute" -> {
                return String.valueOf(date.getMinute());
            }
            case "date_hour" -> {
                return String.valueOf(date.getHour());
            }
            case "date_day" -> {
                return String.valueOf(date.getDay());
            }
            case "date_week" -> {
                return String.valueOf(date.getWeek());
            }
            case "date_month" -> {
                return String.valueOf(date.getMonth());
            }
            case "date_season" -> {
                if (seasonNames.size() < 4) {
                    return "Error: Invalid season names.";
                }

                if (date.getMonth() < 3 || date.getMonth() > 10) {
                    return "4";
                } else if (date.getMonth() < 6) {
                    return "1";
                } else if (date.getMonth() < 9) {
                    return "2";
                } else {
                    return "3";
                }
            }
            case "date_year" -> {
                return String.valueOf(date.getYear());
            }
            case "date_era" -> {
                return String.valueOf(date.getEra());
            }
            case "date_day_name" -> {
                return dayNames.get(date.getLocalDateTime().getDayOfWeek().getValue() - 1);
            }
            case "date_month_name" -> {
                return monthNames.get(date.getLocalDateTime().getMonthValue() - 1);
            }
            case "date_season_name" -> {
                if (seasonNames.size() < 4) {
                    return "Error: Invalid season names.";
                }

                if (date.getMonth() < 3 || date.getMonth() > 10) {
                    return seasonNames.get(3);
                } else if (date.getMonth() < 6) {
                    return seasonNames.getFirst();
                } else if (date.getMonth() < 9) {
                    return seasonNames.get(1);
                } else {
                    return seasonNames.get(2);
                }
            }
            case "date_day_of_week" -> {
                return String.valueOf(date.getLocalDateTime().getDayOfWeek().getValue());
            }
            default -> {
                return "";
            }
        }
    }

    private String parseDatePlaceholders(VenturaCalendarDate venturaCalendarDate, String id) {
        TimeSystem ts = plugin.getTimeConfig().getTimeSystem();
        DateUtils du = plugin.getDateUtils();

        long dow = du.getDayOfWeek(venturaCalendarDate);

        venturaCalendarDate = du.addZeroPoints(venturaCalendarDate);

        if (id.startsWith("month_")) {
            if (id.endsWith("_season")) {
                String month = id.split("_")[1];

                Month m = ts.getMonth(month);

                return m.getSeasonName() == null ? "" : m.getSeasonName();
            }

            if (id.endsWith("_days")) {
                String month = id.split("_")[1];

                Month m = ts.getMonth(month);

                return m == null ? "" : String.valueOf(m.getDays());
            }
        }

        // %venturacalendar_date_formatted_<simple-date-format>%
        if (id.startsWith("date_formatted_")) {
            String pattern = id.split("_")[2];

            if (pattern == null || pattern.isEmpty()) {
                return "";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            return sdf.format(venturaCalendarDate.toDate());
        }

        switch (id) {
            case "date_event_name" -> {
                venturaCalendarDate = du.removeZeroPoints(venturaCalendarDate);
                for (MonthEvent event : this.plugin.getEventConfig().getEvents()) {
                    if (event.includesDate(venturaCalendarDate)) {
                        return event.getDisplayName();
                    }
                }
                return "";
            }
            case "date_event_description" -> {
                venturaCalendarDate = du.removeZeroPoints(venturaCalendarDate);
                for (MonthEvent event : this.plugin.getEventConfig().getEvents()) {
                    if (event.includesDate(venturaCalendarDate)) {
                        return String.join("\n", event.getDescription());
                    }
                }
                return "";
            }
            case "date_tick" -> {
                return String.valueOf(venturaCalendarDate.getTick());
            }
            case "date_second" -> {
                return String.valueOf(venturaCalendarDate.getSecond());
            }
            case "date_minute" -> {
                return String.valueOf(venturaCalendarDate.getMinute());
            }
            case "date_hour" -> {
                return String.valueOf(venturaCalendarDate.getHour());
            }
            case "date_day" -> {
                return String.valueOf(venturaCalendarDate.getDay());
            }
            case "date_week" -> {
                return String.valueOf(venturaCalendarDate.getWeek());
            }
            case "date_month" -> {
                return String.valueOf(venturaCalendarDate.getMonth());
            }
            case "date_year" -> {
                return String.valueOf(venturaCalendarDate.getYear());
            }
            case "date_years_passed" -> {
                venturaCalendarDate = du.removeZeroPoints(venturaCalendarDate);
                return String.valueOf(venturaCalendarDate.getYear());
            }
            case "date_era" -> {
                return String.valueOf(venturaCalendarDate.getEra());
            }
            case "date_day_name" -> {
                return String.valueOf(venturaCalendarDate.getDayName());
            }
            case "date_month_name" -> {
                venturaCalendarDate = du.removeZeroPoints(venturaCalendarDate);
                return String.valueOf(venturaCalendarDate.getMonthName());
            }
            case "date_season_name" -> {
                return String.valueOf(venturaCalendarDate.getSeasonName());
            }
            case "date_era_name" -> {
                return String.valueOf(venturaCalendarDate.getEraName());
            }
            case "date_day_of_week" -> {
                return String.valueOf(dow);
            }
            default -> {
                return "";
            }
        }
    }

    private static final List<String> PLACEHOLDERS = List.of(
            "%venturacalendar_date_tick%",
            "%venturacalendar_date_second%",
            "%venturacalendar_date_minute%",
            "%venturacalendar_date_hour%",
            "%venturacalendar_date_day%",
            "%venturacalendar_date_week%",
            "%venturacalendar_date_month%",
            "%venturacalendar_date_year%",
            "%venturacalendar_date_era%",
            "%venturacalendar_date_day_name%",
            "%venturacalendar_date_month_name%",
            "%venturacalendar_date_season_name%",
            "%venturacalendar_date_era_name%",
            "%venturacalendar_date_day_of_week%",
            "%venturacalendar_date_event_name%",
            "%venturacalendar_date_event_description%",
            "%venturacalendar_date_years_passed%",
            "%venturacalendar_date_formatted_<simple-date-format>%",
            "%venturacalendar_month_<month>_season%",
            "%venturacalendar_month_<month>_days%",
            "%venturacalendar_newday_message%",
            "%venturacalendar_actionbar_message%",
            "%venturacalendar_event_<event>_start%",
            "%venturacalendar_event_<event>_end%",
            "%venturacalendar_event_<event>_description%"
    );

    @Override
    public @NotNull List<String> getPlaceholders() {
        return PLACEHOLDERS;
    }
}