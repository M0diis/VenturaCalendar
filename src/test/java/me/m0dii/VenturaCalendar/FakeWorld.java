//package me.m0dii.VenturaCalendar;
//
//import com.destroystokyo.paper.HeightmapType;
//import io.papermc.paper.world.MoonPhase;
//import org.bukkit.*;
//import org.bukkit.block.Biome;
//import org.bukkit.block.Block;
//import org.bukkit.block.data.BlockData;
//import org.bukkit.boss.DragonBattle;
//import org.bukkit.entity.*;
//import org.bukkit.event.entity.CreatureSpawnEvent;
//import org.bukkit.generator.BlockPopulator;
//import org.bukkit.generator.ChunkGenerator;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.material.MaterialData;
//import org.bukkit.metadata.MetadataValue;
//import org.bukkit.plugin.Plugin;
//import org.bukkit.util.BoundingBox;
//import org.bukkit.util.Consumer;
//import org.bukkit.util.RayTraceResult;
//import org.bukkit.util.Vector;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.io.File;
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//import java.util.function.Predicate;
//
//public class FakeWorld implements World
//{
//    private final String name;
//    private final Environment env;
//    private final UUID uid;
//
//    public FakeWorld(final String string, final Environment environment) {
//        this.name = string;
//        this.env = environment;
//        this.uid = UUID.randomUUID();
//    }
//
//    @Override
//    public int getEntityCount()
//    {
//        return 0;
//    }
//    @Override
//    public int getTileEntityCount()
//    {
//        return 0;
//    }
//    @Override
//    public int getTickableTileEntityCount()
//    {
//        return 0;
//    }
//    @Override
//    public int getChunkCount()
//    {
//        return 0;
//    }
//    @Override
//    public int getPlayerCount()
//    {
//        return 0;
//    }
//    @Override
//    public @NotNull MoonPhase getMoonPhase()
//    {
//        return null;
//    }
//    @Override
//    public boolean lineOfSightExists(@NotNull Location from, @NotNull Location to)
//    {
//        return false;
//    }
//    @Override
//    public @NotNull Block getBlockAt(int x, int y, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Block getBlockAt(@NotNull Location location) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getHighestBlockYAt(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getHighestBlockYAt(@NotNull Location location) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Block getHighestBlockAt(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Block getHighestBlockAt(@NotNull Location location) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getHighestBlockYAt(int x, int z, @NotNull HeightmapType heightmap) throws UnsupportedOperationException
//    {
//        return 0;
//    }
//
//    @Override
//    public int getHighestBlockYAt(int x, int z, @NotNull HeightMap heightMap)
//    {
//        return 0;
//    }
//
//    @Override
//    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap)
//    {
//        return 0;
//    }
//
//    @Override
//    public @NotNull Block getHighestBlockAt(int x, int z, @NotNull HeightMap heightMap)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Chunk getChunkAt(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Chunk getChunkAt(@NotNull Location location) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Chunk getChunkAt(@NotNull Block block) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen, boolean urgent)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull NamespacedKey getKey()
//    {
//        return null;
//    }
//
//    @Override
//    public boolean isChunkLoaded(@NotNull Chunk chunk) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Chunk[] getLoadedChunks() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void loadChunk(@NotNull Chunk chunk) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean isChunkLoaded(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public boolean isChunkGenerated(int x, int z)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isChunkInUse(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void loadChunk(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean loadChunk(int x, int z, boolean generate) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean unloadChunk(@NotNull Chunk chunk) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean unloadChunk(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean unloadChunk(int x, int z, boolean save) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean unloadChunkRequest(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean regenerateChunk(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean refreshChunk(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public boolean isChunkForceLoaded(int x, int z)
//    {
//        return false;
//    }
//    @Override
//    public void setChunkForceLoaded(int x, int z, boolean forced)
//    {
//
//    }
//    @Override
//    public @NotNull Collection<Chunk> getForceLoadedChunks()
//    {
//        return null;
//    }
//    @Override
//    public boolean addPluginChunkTicket(int x, int z, @NotNull Plugin plugin)
//    {
//        return false;
//    }
//    @Override
//    public boolean removePluginChunkTicket(int x, int z, @NotNull Plugin plugin)
//    {
//        return false;
//    }
//    @Override
//    public void removePluginChunkTickets(@NotNull Plugin plugin)
//    {
//
//    }
//    @Override
//    public @NotNull Collection<Plugin> getPluginChunkTickets(int x, int z)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull Map<Plugin, Collection<Chunk>> getPluginChunkTickets()
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<Item> function)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public @NotNull Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<Item> function)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull Arrow spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed,
//                                                           float spread, @NotNull Class<T> clazz)
//    {
//        return null;
//    }
//
//    @Override
//    public boolean generateTree(@NotNull Location location, @NotNull TreeType type) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean generateTree(@NotNull Location loc, @NotNull TreeType type, @NotNull BlockChangeDelegate delegate) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull LightningStrike strikeLightning(@NotNull Location loc) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull LightningStrike strikeLightningEffect(@NotNull Location loc) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull List<Entity> getEntities() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull List<LivingEntity> getLivingEntities() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @SafeVarargs
//    @Override
//    public final <T extends Entity> @NotNull Collection<T> getEntitiesByClass(Class<T>... classes) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T extends Entity> @NotNull Collection<T> getEntitiesByClass(@NotNull Class<T> cls) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull List<Player> getPlayers() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public @Nullable Entity getEntity(@NotNull UUID uuid)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z, @Nullable Predicate<Entity> filter)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, @Nullable Predicate<Entity> filter)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance,
//                                                     double raySize)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance,
//                                                     @Nullable Predicate<Entity> filter)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance,
//                                                     double raySize, @Nullable Predicate<Entity> filter)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance,
//                                                   @NotNull FluidCollisionMode fluidCollisionMode)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance,
//                                                   @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, @Nullable Predicate<Entity> filter)
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull String getName() {
//        return name;
//    }
//
//    @Override
//    public @NotNull UUID getUID() {
//        return uid;
//    }
//
//    @Override
//    public @NotNull Location getSpawnLocation() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean setSpawnLocation(@NotNull Location location) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public boolean setSpawnLocation(int x, int y, int z, float angle)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean setSpawnLocation(int x, int y, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public long getTime() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setTime(long time) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public long getFullTime() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setFullTime(long time) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public boolean isDayTime()
//    {
//        return false;
//    }
//    @Override
//    public long getGameTime()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean hasStorm() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setStorm(boolean hasStorm) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getWeatherDuration() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setWeatherDuration(int duration) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean isThundering() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setThundering(boolean thundering) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getThunderDuration() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setThunderDuration(int duration) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public boolean isClearWeather()
//    {
//        return false;
//    }
//    @Override
//    public void setClearWeatherDuration(int duration)
//    {
//
//    }
//    @Override
//    public int getClearWeatherDuration()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean createExplosion(double x, double y, double z, float power) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks,
//                                   @Nullable Entity source)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean createExplosion(@NotNull Location loc, float power) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire, boolean breakBlocks)
//    {
//        return false;
//    }
//    @Override
//    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks)
//    {
//        return false;
//    }
//    @Override
//    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source)
//    {
//        return false;
//    }
//
//    @Override
//    public @NotNull Environment getEnvironment() {
//        return env;
//    }
//
//    @Override
//    public long getSeed() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean getPVP() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setPVP(boolean pvp) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public ChunkGenerator getGenerator() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void save() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull List<BlockPopulator> getPopulators() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz) throws IllegalArgumentException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function, CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull MaterialData data) throws IllegalArgumentException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData data) throws IllegalArgumentException
//    {
//        return null;
//    }
//
//    @Override
//    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte data) throws IllegalArgumentException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data, int radius) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, T data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, T data, int radius) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean getAllowAnimals() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean getAllowMonsters() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Biome getBiome(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public @NotNull Biome getBiome(int x, int y, int z)
//    {
//        return null;
//    }
//
//    @Override
//    public void setBiome(int x, int z, @NotNull Biome bio) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public void setBiome(int x, int y, int z, @NotNull Biome bio)
//    {
//
//    }
//
//    @Override
//    public double getTemperature(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public double getTemperature(int x, int y, int z)
//    {
//        return 0;
//    }
//
//    @Override
//    public double getHumidity(int x, int z) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public double getHumidity(int x, int y, int z)
//    {
//        return 0;
//    }
//    @Override
//    public int getMinHeight()
//    {
//        return 0;
//    }
//
//    @Override
//    public int getMaxHeight() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getSeaLevel() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean getKeepSpawnInMemory() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setKeepSpawnInMemory(boolean keepLoaded) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean isAutoSave() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setAutoSave(boolean value) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setDifficulty(@NotNull Difficulty difficulty) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Difficulty getDifficulty() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull File getWorldFolder() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public WorldType getWorldType() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean canGenerateStructures() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public boolean isHardcore()
//    {
//        return false;
//    }
//    @Override
//    public void setHardcore(boolean hardcore)
//    {
//
//    }
//
//    @Override
//    public long getTicksPerAnimalSpawns() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public long getTicksPerMonsterSpawns() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public long getTicksPerWaterSpawns()
//    {
//        return 0;
//    }
//    @Override
//    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns)
//    {
//
//    }
//    @Override
//    public long getTicksPerWaterAmbientSpawns()
//    {
//        return 0;
//    }
//    @Override
//    public void setTicksPerWaterAmbientSpawns(int ticksPerAmbientSpawns)
//    {
//
//    }
//    @Override
//    public long getTicksPerAmbientSpawns()
//    {
//        return 0;
//    }
//    @Override
//    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns)
//    {
//
//    }
//
//    @Override
//    public int getMonsterSpawnLimit() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setMonsterSpawnLimit(int limit) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getAnimalSpawnLimit() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setAnimalSpawnLimit(int limit) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public int getWaterAnimalSpawnLimit() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setWaterAnimalSpawnLimit(int limit) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public int getWaterAmbientSpawnLimit()
//    {
//        return 0;
//    }
//    @Override
//    public void setWaterAmbientSpawnLimit(int limit)
//    {
//
//    }
//
//    @Override
//    public int getAmbientSpawnLimit() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setAmbientSpawnLimit(int limit) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public String[] getGameRules() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public String getGameRuleValue(String rule) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean setGameRuleValue(@NotNull String rule, @NotNull String value) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean isGameRule(@NotNull String rule) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> rule)
//    {
//        return null;
//    }
//    @Override
//    public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> rule)
//    {
//        return null;
//    }
//    @Override
//    public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue)
//    {
//        return false;
//    }
//
//    @Override
//    public @NotNull WorldBorder getWorldBorder() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, T data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, T data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY,
//                              double offsetZ, double extra) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY,
//                                  double offsetZ, double extra, T data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY,
//                                  double offsetZ, double extra, T data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @Nullable Player source,
//                                  double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ,
//                                  double extra, @Nullable T data, boolean force)
//    {
//
//    }
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force)
//    {
//
//    }
//    @Override
//    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force)
//    {
//
//    }
//    @Override
//    public @Nullable Location locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType, int radius, boolean findUnexplored)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius)
//    {
//        return null;
//    }
//    @Override
//    public @Nullable Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius, int step)
//    {
//        return null;
//    }
//    @Override
//    public boolean isUltrawarm()
//    {
//        return false;
//    }
//    @Override
//    public boolean isNatural()
//    {
//        return false;
//    }
//    @Override
//    public double getCoordinateScale()
//    {
//        return 0;
//    }
//    @Override
//    public boolean hasSkylight()
//    {
//        return false;
//    }
//    @Override
//    public boolean hasBedrockCeiling()
//    {
//        return false;
//    }
//    @Override
//    public boolean isPiglinSafe()
//    {
//        return false;
//    }
//    @Override
//    public boolean doesBedWork()
//    {
//        return false;
//    }
//    @Override
//    public boolean doesRespawnAnchorWork()
//    {
//        return false;
//    }
//    @Override
//    public boolean hasRaids()
//    {
//        return false;
//    }
//    @Override
//    public boolean isFixedTime()
//    {
//        return false;
//    }
//    @Override
//    public @NotNull Collection<Material> getInfiniburn()
//    {
//        return null;
//    }
//    @Override
//    public int getViewDistance()
//    {
//        return 0;
//    }
//    @Override
//    public void setViewDistance(int viewDistance)
//    {
//
//    }
//    @Override
//    public int getNoTickViewDistance()
//    {
//        return 0;
//    }
//    @Override
//    public void setNoTickViewDistance(int viewDistance)
//    {
//
//    }
//    @Override
//    public @NotNull Spigot spigot()
//    {
//        return null;
//    }
//    @Override
//    public @Nullable Raid locateNearestRaid(@NotNull Location location, int radius)
//    {
//        return null;
//    }
//    @Override
//    public @NotNull List<Raid> getRaids()
//    {
//        return null;
//    }
//    @Override
//    public @Nullable DragonBattle getEnderDragonBattle()
//    {
//        return null;
//    }
//
//    @Override
//    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean hasMetadata(@NotNull String metadataKey) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, byte[] message) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public @NotNull Set<String> getListeningPluginChannels() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}