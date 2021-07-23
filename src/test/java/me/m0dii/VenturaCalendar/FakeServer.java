//package me.m0dii.VenturaCalendar;
//import com.destroystokyo.paper.entity.ai.MobGoals;
//import com.destroystokyo.paper.profile.PlayerProfile;
//import io.papermc.paper.datapack.DatapackManager;
//import net.kyori.adventure.audience.Audience;
//import net.kyori.adventure.text.Component;
//import org.bukkit.*;
//import org.bukkit.advancement.Advancement;
//import org.bukkit.block.data.BlockData;
//import org.bukkit.boss.*;
//import org.bukkit.command.*;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.inventory.InventoryType;
//import org.bukkit.event.player.PlayerJoinEvent;
//import org.bukkit.generator.ChunkGenerator;
//import org.bukkit.help.HelpMap;
//import org.bukkit.inventory.*;
//import org.bukkit.loot.LootTable;
//import org.bukkit.map.MapView;
//import org.bukkit.permissions.Permissible;
//import org.bukkit.permissions.Permission;
//import org.bukkit.plugin.*;
//import org.bukkit.plugin.messaging.Messenger;
//import org.bukkit.scheduler.BukkitScheduler;
//import org.bukkit.scoreboard.ScoreboardManager;
//import org.bukkit.util.CachedServerIcon;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.*;
//import java.util.function.Consumer;
//import java.util.logging.Logger;
//
//public final class FakeServer implements Server
//{
//    private final List<World> worlds = new ArrayList<>();
//    private final List<Player> players = new ArrayList<>();
//    private final PluginManager pluginManager = new FakePluginManager();
//
//    FakeServer()
//    {
//        createWorld("FakeWorld", World.Environment.NORMAL);
//    }
//
//    public static FakeServer getServer()
//    {
//        if(Bukkit.getServer() == null)
//            Bukkit.setServer(new FakeServer());
//
//        return (FakeServer) Bukkit.getServer();
//    }
//
//    void addPlayer(final Player pl)
//    {
//        players.add(pl);
//
//        pluginManager.callEvent(new PlayerJoinEvent(pl, (String)null));
//    }
//
//    @Override
//    public @NotNull String getName()
//    {
//        return "M0dii-Fake-Server";
//    }
//
//    @Override
//    public @NotNull String getVersion()
//    {
//        return "1.0";
//    }
//
//    @Override
//    public @NotNull String getBukkitVersion()
//    {
//        return "FakeServer";
//    }
//
//    @Override
//    public @NotNull String getMinecraftVersion()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Collection<? extends Player> getOnlinePlayers()
//    {
//        return players;
//    }
//
//    private int maxPlayers = 100;
//
//    @Override
//    public int getMaxPlayers()
//    {
//        return this.maxPlayers;
//    }
//
//    @Override
//    public void setMaxPlayers(int maxPlayers)
//    {
//        this.maxPlayers = maxPlayers;
//    }
//
//    @Override
//    public int getPort()
//    {
//        return 25565;
//    }
//
//    @Override
//    public int getViewDistance()
//    {
//        return 3;
//    }
//
//    @Override
//    public @NotNull String getIp()
//    {
//        return "127.0.0.1";
//    }
//
//    @Override
//    public @NotNull String getWorldType()
//    {
//        return String.valueOf(WorldType.NORMAL);
//    }
//
//    @Override
//    public boolean getGenerateStructures()
//    {
//        return false;
//    }
//
//    @Override
//    public int getMaxWorldSize()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean getAllowEnd()
//    {
//        return true;
//    }
//    @Override
//
//    public boolean getAllowNether()
//    {
//        return true;
//    }
//
//    private boolean whitelist = false;
//
//    @Override
//    public boolean hasWhitelist()
//    {
//        return whitelist;
//    }
//
//    @Override
//    public void setWhitelist(boolean value)
//    {
//        this.whitelist = value;
//    }
//
//    @Override
//    public @NotNull Set<OfflinePlayer> getWhitelistedPlayers()
//    {
//        return null;
//    }
//
//    @Override
//    public void reloadWhitelist()
//    {
//
//
//    }
//
//    @Override
//    public int broadcastMessage(@NotNull String message)
//    {
//        return 0;
//    }
//
//    @Override
//    public @NotNull String getUpdateFolder()
//    {
//        return "update";
//    }
//
//    @Override
//    public @NotNull File getUpdateFolderFile()
//    {
//        return null;
//    }
//
//    @Override
//    public long getConnectionThrottle()
//    {
//        return 0;
//    }
//    @Override
//    public int getTicksPerAnimalSpawns()
//    {
//        return 0;
//    }
//    @Override
//    public int getTicksPerMonsterSpawns()
//    {
//        return 0;
//    }
//    @Override
//    public int getTicksPerWaterSpawns()
//    {
//        return 0;
//    }
//    @Override
//    public int getTicksPerWaterAmbientSpawns()
//    {
//        return 0;
//    }
//    @Override
//    public int getTicksPerAmbientSpawns()
//    {
//        return 0;
//    }
//    @Override
//    public @Nullable Player getPlayer(@NotNull String name)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable Player getPlayerExact(@NotNull String name)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull List<Player> matchPlayer(@NotNull String name)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable Player getPlayer(@NotNull UUID id)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable UUID getPlayerUniqueId(@NotNull String playerName)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull PluginManager getPluginManager()
//    {
//        return null;
//    }
//    @Override
//    public @NotNull BukkitScheduler getScheduler()
//    {
//        return null;
//    }
//    @Override
//    public @NotNull ServicesManager getServicesManager()
//    {
//        return null;
//    }
//    @Override
//    public @NotNull List<World> getWorlds()
//    {
//        return null;
//    }
//
//    @Override
//    public @Nullable World createWorld(@NotNull WorldCreator creator)
//    {
//        return null;
//    }
//
//
//    public World createWorld(final String string, final World.Environment e)
//    {
//        final World w = new FakeWorld(string, e);
//
//        worlds.add(w);
//
//        return w;
//    }
//
//    @Override
//    public boolean unloadWorld(@NotNull String name, boolean save)
//    {
//        return false;
//    }
//    @Override
//    public boolean unloadWorld(@NotNull World world, boolean save)
//    {
//        return false;
//    }
//    @Override
//    public @Nullable World getWorld(@NotNull String name)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable World getWorld(@NotNull UUID uid)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable World getWorld(@NotNull NamespacedKey worldKey)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable MapView getMap(int id)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull MapView createMap(@NotNull World world)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull ItemStack createExplorerMap(@NotNull World world,
//                                                @NotNull Location location,
//                                                @NotNull StructureType structureType)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull ItemStack createExplorerMap(@NotNull World world,
//                                                @NotNull Location location,
//                                                @NotNull StructureType structureType, int radius, boolean findUnexplored)
//    {
//        return null;
//    }
//    @Override
//    public void reload()
//    {
//
//    }
//    @Override
//    public void reloadData()
//    {
//
//    }
//    @Override
//    public @NotNull Logger getLogger()
//    {
//        return Logger.getLogger("Minecraft");
//    }
//
//    @Override
//    public @Nullable PluginCommand getPluginCommand(@NotNull String name)
//    {
//        return null;
//    }
//    @Override
//    public void savePlayers()
//    {
//
//    }
//    @Override
//    public boolean dispatchCommand(@NotNull CommandSender sender, @NotNull String commandLine) throws CommandException
//    {
//        return false;
//    }
//    @Override
//    public boolean addRecipe(@Nullable Recipe recipe)
//    {
//        return false;
//    }
//    @Override
//    public @NotNull List<Recipe> getRecipesFor(@NotNull ItemStack result)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable Recipe getRecipe(@NotNull NamespacedKey recipeKey)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull Iterator<Recipe> recipeIterator()
//    {
//        return null;
//    }
//    @Override
//    public void clearRecipes()
//    {
//
//    }
//    @Override
//    public void resetRecipes()
//    {
//
//    }
//    @Override
//    public boolean removeRecipe(@NotNull NamespacedKey key)
//    {
//        return false;
//    }
//    @Override
//    public @NotNull Map<String, String[]> getCommandAliases()
//    {
//        return null;
//    }
//    @Override
//    public int getSpawnRadius()
//    {
//        return 0;
//    }
//    @Override
//    public void setSpawnRadius(int value)
//    {
//
//    }
//    @Override
//    public boolean getOnlineMode()
//    {
//        return false;
//    }
//    @Override
//    public boolean getAllowFlight()
//    {
//        return false;
//    }
//    @Override
//    public boolean isHardcore()
//    {
//        return false;
//    }
//
//    @Override
//    public void shutdown()
//    {
//
//    }
//
//    @Override
//    public int broadcast(@NotNull String message, @NotNull String permission)
//    {
//        return 0;
//    }
//
//    @Override
//    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull String name)
//    {
//        return createOfflinePlayer(name);
//    }
//
//    @Override
//    public @Nullable OfflinePlayer getOfflinePlayerIfCached(@NotNull String name)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID id)
//    {
//        return null;
//    }
//
//    private OfflinePlayer createOfflinePlayer(final String name)
//    {
//        return new OfflinePlayer() {
//            @Override
//            public boolean isOnline() {
//                return false;
//            }
//
//            @Override
//            public String getName() {
//                return name;
//            }
//
//            @Override
//            public boolean isBanned() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public boolean isWhitelisted() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void setWhitelisted(final boolean bln) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public Player getPlayer() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public boolean isOp() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public Map<String, Object> serialize() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public long getFirstPlayed() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void setOp(final boolean bln) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public long getLastPlayed() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public boolean hasPlayedBefore() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public Location getBedSpawnLocation() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//            @Override
//            public long getLastLogin()
//            {
//                return 0;
//            }
//            @Override
//            public long getLastSeen()
//            {
//                return 0;
//            }
//
//            @Override
//            public UUID getUniqueId() {
//                switch (name) {
//                    case "testPlayer1":
//                        return UUID.fromString("3c9ebe1a-9098-43fd-bc0c-a369b76817ba");
//                    case "testPlayer2":
//                        return UUID.fromString("2c9ebe1a-9098-43fd-bc0c-a369b76817ba");
//                    case "npc1":
//                        return UUID.fromString("f4a37409-5c40-3b2c-9cd6-57d3c5abdc76");
//                }
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void incrementStatistic(final Statistic statistic) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void decrementStatistic(final Statistic statistic) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public int getStatistic(final Statistic statistic) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void incrementStatistic(final Statistic statistic, final int amount) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void decrementStatistic(final Statistic statistic, final int amount) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void setStatistic(final Statistic statistic, final int newValue) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void incrementStatistic(final Statistic statistic, final Material material) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void decrementStatistic(final Statistic statistic, final Material material) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public int getStatistic(final Statistic statistic, final Material material) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void incrementStatistic(final Statistic statistic, final Material material, final int amount) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void decrementStatistic(final Statistic statistic, final Material material, final int amount) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void setStatistic(final Statistic statistic, final Material material, final int newValue) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void incrementStatistic(final Statistic statistic, final EntityType entityType) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void decrementStatistic(final Statistic statistic, final EntityType entityType) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public int getStatistic(final Statistic statistic, final EntityType entityType) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void incrementStatistic(final Statistic statistic, final EntityType entityType, final int amount) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void decrementStatistic(final Statistic statistic, final EntityType entityType, final int amount) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public void setStatistic(final Statistic statistic, final EntityType entityType, final int newValue) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
//    }
//
//    @Override
//    public @NotNull Set<String> getIPBans()
//    {
//        return null;
//    }
//
//    @Override
//    public void banIP(@NotNull String address)
//    {
//
//    }
//
//    @Override
//    public void unbanIP(@NotNull String address)
//    {
//
//    }
//
//    @Override
//    public @NotNull Set<OfflinePlayer> getBannedPlayers()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull BanList getBanList(BanList.@NotNull Type type)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Set<OfflinePlayer> getOperators()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull GameMode getDefaultGameMode()
//    {
//        return null;
//    }
//
//    @Override
//    public void setDefaultGameMode(@NotNull GameMode mode)
//    {
//
//    }
//
//    @Override
//    public @NotNull ConsoleCommandSender getConsoleSender()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull File getWorldContainer()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull
//    OfflinePlayer[] getOfflinePlayers()
//    {
//        return new OfflinePlayer[0];
//    }
//
//    @Override
//    public @NotNull Messenger getMessenger()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull HelpMap getHelpMap()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull String title)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, int size) throws IllegalArgumentException
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull String title) throws IllegalArgumentException
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Merchant createMerchant(@Nullable String title)
//    {
//        return null;
//    }
//
//    @Override
//    public int getMonsterSpawnLimit()
//    {
//        return 0;
//    }
//
//    @Override
//    public int getAnimalSpawnLimit()
//    {
//        return 0;
//    }
//
//    @Override
//    public int getWaterAnimalSpawnLimit()
//    {
//        return 0;
//    }
//
//    @Override
//    public int getWaterAmbientSpawnLimit()
//    {
//        return 0;
//    }
//
//    @Override
//    public int getAmbientSpawnLimit()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean isPrimaryThread()
//    {
//        return false;
//    }
//
//    @Override
//    public @NotNull Component motd()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull String getMotd()
//    {
//        return null;
//    }
//
//    @Override
//    public @Nullable Component shutdownMessage()
//    {
//        return null;
//    }
//
//    @Override
//    public @Nullable String getShutdownMessage()
//    {
//        return "Server is shutting down.";
//    }
//
//    @Override
//    public Warning.@NotNull WarningState getWarningState()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull ItemFactory getItemFactory()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull ScoreboardManager getScoreboardManager()
//    {
//        return null;
//    }
//
//    @Override
//    public @Nullable CachedServerIcon getServerIcon()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull CachedServerIcon loadServerIcon(@NotNull File file) throws IllegalArgumentException, Exception
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull CachedServerIcon loadServerIcon(@NotNull BufferedImage image) throws IllegalArgumentException, Exception
//    {
//        return null;
//    }
//
//    @Override
//    public void setIdleTimeout(int threshold)
//    {
//
//    }
//
//    @Override
//    public int getIdleTimeout()
//    {
//        return 0;
//    }
//
//    @Override
//    public ChunkGenerator.@NotNull ChunkData createChunkData(@NotNull World world)
//    {
//        return null;
//    }
//
//    @Override
//    public ChunkGenerator.@NotNull ChunkData createVanillaChunkData(@NotNull World world, int x, int z)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull BossBar createBossBar(@Nullable String title,
//                                          @NotNull BarColor color,
//                                          @NotNull BarStyle style,
//                                          @NotNull BarFlag... flags)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull KeyedBossBar createBossBar(@NotNull NamespacedKey key,
//                                               @Nullable String title,
//                                               @NotNull BarColor color,
//                                               @NotNull BarStyle style,
//                                               @NotNull BarFlag... flags)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Iterator<KeyedBossBar> getBossBars()
//    {
//        return null;
//    }
//    @Override
//    public @Nullable KeyedBossBar getBossBar(@NotNull NamespacedKey key)
//    {
//        return null;
//    }
//
//    @Override
//    public boolean removeBossBar(@NotNull NamespacedKey key)
//    {
//        return false;
//    }
//
//    @Override
//    public @Nullable Entity getEntity(@NotNull UUID uuid)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull
//    double[] getTPS()
//    {
//        return new double[0];
//    }
//
//    @Override
//    public @NotNull
//    long[] getTickTimes()
//    {
//        return new long[0];
//    }
//
//    @Override
//    public double getAverageTickTime()
//    {
//        return 0;
//    }
//
//    @Override
//    public @NotNull CommandMap getCommandMap()
//    {
//        return null;
//    }
//
//    @Override
//    public @Nullable Advancement getAdvancement(@NotNull NamespacedKey key)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Iterator<Advancement> advancementIterator()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull BlockData createBlockData(@NotNull Material material)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull BlockData createBlockData(@NotNull Material material,
//                                              @Nullable Consumer<BlockData> consumer)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull BlockData createBlockData(@NotNull String data) throws IllegalArgumentException
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull BlockData createBlockData(@Nullable Material material,
//                                              @Nullable String data) throws IllegalArgumentException
//    {
//        return null;
//    }
//
//    @Override
//    public <T extends Keyed> Tag<T> getTag(@NotNull String registry,
//                                           @NotNull NamespacedKey tag,
//                                           @NotNull Class<T> clazz)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull <T extends Keyed> Iterable<Tag<T>> getTags(@NotNull String registry, @NotNull Class<T> clazz)
//    {
//        return null;
//    }
//
//    @Override
//    public @Nullable LootTable getLootTable(@NotNull NamespacedKey key)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull List<Entity> selectEntities(@NotNull CommandSender sender, @NotNull String selector) throws IllegalArgumentException
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull UnsafeValues getUnsafe()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Spigot spigot()
//    {
//        return null;
//    }
//
//    @Override
//    public void reloadPermissions()
//    {
//
//    }
//
//    @Override
//    public boolean reloadCommandAliases()
//    {
//        return false;
//    }
//
//    @Override
//    public boolean suggestPlayerNamesWhenNullTabCompletions()
//    {
//        return false;
//    }
//
//    @Override
//    public @NotNull String getPermissionMessage()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull PlayerProfile createProfile(@NotNull UUID uuid)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull PlayerProfile createProfile(@NotNull String name)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull PlayerProfile createProfile(@Nullable UUID uuid,
//                                                @Nullable String name)
//    {
//        return null;
//    }
//
//    @Override
//    public int getCurrentTick()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean isStopping()
//    {
//        return false;
//    }
//    @Override
//    public @NotNull MobGoals getMobGoals()
//    {
//        return null;
//    }
//    @Override
//    public @NotNull DatapackManager getDatapackManager()
//    {
//        return null;
//    }
//    @Override
//    public @NotNull Merchant createMerchant(net.kyori.adventure.text.@Nullable Component title)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull Component title) throws IllegalArgumentException
//    {
//        return null;
//    }
//    @Override
//    public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull Component title)
//    {
//        return null;
//    }
//    @Override
//    public int broadcast(@NotNull Component message, @NotNull String permission)
//    {
//        return 0;
//    }
//    @Override
//    public int broadcast(@NotNull Component message)
//    {
//        return 0;
//    }
//    @Override
//    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message)
//    {
//
//    }
//    @Override
//    public @NotNull Set<String> getListeningPluginChannels()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Iterable<? extends Audience> audiences()
//    {
//        return null;
//    }
//
//    static class FakePluginManager implements PluginManager {
//        final ArrayList<RegisteredListener> listeners = new ArrayList<>();
//
//        @Override
//        public void registerInterface(final Class<? extends PluginLoader> loader) throws IllegalArgumentException {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Plugin getPlugin(final String name) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Plugin[] getPlugins() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public boolean isPluginEnabled(final String name) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public boolean isPluginEnabled(final Plugin plugin) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Plugin loadPlugin(final File file) throws UnknownDependencyException
//        {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Plugin[] loadPlugins(final File directory) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void disablePlugins() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void clearPlugins() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void callEvent(final Event event) throws IllegalStateException
//        {
//            Logger.getLogger("Minecraft").info("Called event " + event.getEventName());
//        }
//
//        @Override
//        public void registerEvents(final Listener listener, final Plugin plugin) {
//            listeners.add(new RegisteredListener(listener, null, null, plugin, false));
//        }
//
//        @Override
//        public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener,
//                                  @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin)
//        {
//
//        }
//
//        @Override
//        public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener,
//                                  @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin,
//                                  boolean ignoreCancelled)
//        {
//
//        }
//
//        @Override
//        public void enablePlugin(final Plugin plugin) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void disablePlugin(final Plugin plugin) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//        @Override
//        public void disablePlugin(@NotNull Plugin plugin, boolean closeClassloader)
//        {
//
//        }
//
//        @Override
//        public Permission getPermission(final String name) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void addPermission(final Permission perm) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void removePermission(final Permission perm) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void removePermission(final String name) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Set<Permission> getDefaultPermissions(final boolean op) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void recalculatePermissionDefaults(final Permission perm) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void subscribeToPermission(final String permission, final Permissible permissible) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void unsubscribeFromPermission(final String permission, final Permissible permissible) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Set<Permissible> getPermissionSubscriptions(final String permission) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void subscribeToDefaultPerms(final boolean op, final Permissible permissible) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public void unsubscribeFromDefaultPerms(final boolean op, final Permissible permissible) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Set<Permissible> getDefaultPermSubscriptions(final boolean op) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Set<Permission> getPermissions() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public boolean useTimings() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//    }
//}
