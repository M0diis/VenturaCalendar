package me.m0dii.venturacalendar.base.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public enum Version implements Comparable<Version> {
    v1_19_R1(18),
    v1_18_R2(17),
    v1_18_R1(16),
    v1_17_R1(15),
    v1_16_R3(14),
    v1_16_R2(13),
    v1_16_R1(12),
    v1_15_R1(11),
    v1_14_R1(10),
    v1_13_R2(9),
    v1_13_R1(8),
    v1_12_R1(7),
    v1_11_R1(6),
    v1_10_R1(5),
    v1_9_R2(4),
    v1_9_R1(3),
    v1_8_R3(2),
    v1_8_R2(1),
    v1_8_R1(0),
    UNKNOWN(-1);

    private boolean notified = false;

    private final int value;

    Version(int value) {
        this.value = value;
    }

    public static boolean serverIsNewerThan(Version version) {
        return getServerVersion(Bukkit.getServer()).isNewerThan(version);
    }

    /**
     * @param server to get the version from
     * @return the version of the server
     * @throws IllegalArgumentException if server is null
     */
    public static Version getServerVersion(Server server) {
        String packageName = server.getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        Messenger.log(Messenger.Level.DEBUG, "Package name: " + packageName + ", using server version: " + version);

        try {
            return valueOf(version.trim());
        }
        catch (final IllegalArgumentException e) {
            return Version.UNKNOWN;
        }
    }

    /**
     * @param server to check
     * @return true if the server is Paper or false of not
     * @throws IllegalArgumentException if server is null
     */
    public static boolean isPaper(Server server) {
        return server.getName().equalsIgnoreCase("Paper");
    }

    /**
     * Checks if the version is newer than the given version
     * <p>
     * If both versions are the same, the method will return false
     *
     * @param version to check against
     * @return true if the version is newer than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isNewerThan(Version version) {
        if (checkUnknown(version)) {
            return true;
        }

        return value > version.value;
    }

    /**
     * Checks if the version is newer or the same than the given version
     *
     * @param version to check against
     * @return true if the version is newer or the same than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isNewerOrSameThan(Version version) {
        if (checkUnknown(version)) {
            return true;
        }

        return value >= version.value;
    }

    /**
     * Checks if the version is older than the given version
     *
     * @param version to check against
     * @return true if the version is older than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isOlderThan(Version version) {
        if (!checkUnknown(version)) {
            return true;
        }

        return value < version.value;
    }

    private boolean checkUnknown(Version version) {
        if (version == UNKNOWN && !notified) {
            Messenger.log(Messenger.Level.WARN, "Provided version is UNKNOWN. Some features may not work correctly.");
            Messenger.log(Messenger.Level.WARN, "Assuming using the latest version.");

            notified = true;

            return true;
        }

        if (this == UNKNOWN && !notified) {
            Messenger.log(Messenger.Level.WARN, "Server version is UNKNOWN. Some features may not work correctly.");
            Messenger.log(Messenger.Level.WARN, "Assuming using the latest version.");

            notified = true;

            return true;
        }

        return false;
    }

    /**
     * Checks if the version is older or the same than the given version
     *
     * @param version to check against
     * @return true if the version is older or the same than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isOlderOrSameThan(Version version) {
        if (!checkUnknown(version)) {
            return true;
        }

        return value <= version.value;
    }

}