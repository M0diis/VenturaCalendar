package me.m0dii.venturacalendar.base.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

public enum Version implements Comparable<Version>
{
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
    
    private final int value;
    
    Version(int value) {
        this.value = value;
    }
    
    public static boolean serverIsNewerThan(Version version)
    {
        return getServerVersion(Bukkit.getServer()).isNewerThan(version);
    }
    
    public static boolean serverIsOlderThan(Version version)
    {
        return getServerVersion(Bukkit.getServer()).isOlderThan(version);
    }
    
    /**
     * @param server to get the version from
     * @return the version of the server
     * @throws IllegalArgumentException if server is null
     */
    @NotNull
    public static Version getServerVersion(@NotNull Server server) {
        Validate.notNull(server, "Server cannot be null");
        
        String packageName = server.getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        
        try {
            return valueOf(version.trim());
        } catch (final IllegalArgumentException e) {
            return Version.UNKNOWN;
        }
    }
    
    /**
     * @param server to check
     * @return true if the server is Paper or false of not
     * @throws IllegalArgumentException if server is null
     */
    public static boolean isPaper(@NotNull Server server) {
        Validate.notNull(server, "Server cannot be null");
        
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
    public boolean isNewerThan(@NotNull Version version) {
        Validate.notNull(version, "Version cannot be null");
        Validate.isTrue(this != UNKNOWN, "Cannot check, if version UNKNOWN is newer");
        Validate.isTrue(version != UNKNOWN, "Cannot check, if version UNKNOWN is newer");
        
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
    public boolean isNewerOrSameThan(@NotNull Version version) {
        Validate.notNull(version, "Version cannot be null");
        Validate.isTrue(this != UNKNOWN, "Cannot check, if version UNKNOWN is newer or same");
        Validate.isTrue(version != UNKNOWN, "Cannot check, if version UNKNOWN is newer or same");
        
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
    public boolean isOlderThan(@NotNull Version version) {
        Validate.notNull(version, "Version cannot be null");
        Validate.isTrue(this != UNKNOWN, "Cannot check, if version UNKNOWN is older");
        Validate.isTrue(version != UNKNOWN, "Cannot check, if version UNKNOWN is older");
        
        return value < version.value;
    }
    
    /**
     * Checks if the version is older or the same than the given version
     *
     * @param version to check against
     * @return true if the version is older or the same than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isOlderOrSameThan(@NotNull Version version) {
        Validate.notNull(version, "Version cannot be null");
        Validate.isTrue(this != UNKNOWN, "Cannot check, if version UNKNOWN is older or same");
        Validate.isTrue(version != UNKNOWN, "Cannot check, if version UNKNOWN is older or same");
        
        return value <= version.value;
    }
    
}