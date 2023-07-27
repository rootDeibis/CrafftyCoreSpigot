package me.rootdeibis.crafftty.manager;

import me.rootdeibis.crafftty.CrafttyLogger;
import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.manager.Files.RFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;

public class SpawnManager {

    private static RFile cacheFile;
    private static Location currentSpawnLocation;

    private static boolean isLoadedSpawn = false;

    public static void load() {
        if(cacheFile == null)
            cacheFile = CrafttySpigotCore.getFileManager().use("cache/spawn.yml").create();

        if(!isLoadedSpawn) {
            Location loadedLocation = deserializeLocation();
            if(loadedLocation != null) {
                currentSpawnLocation = deserializeLocation();
                isLoadedSpawn = true;

                CrafttyLogger.send("&aSpawn loaded");
            } else {
                CrafttyLogger.send("&cSpawn no set.");
            }

        }

    }


    public static void set(Location loc) {
        currentSpawnLocation = loc;
        serializeLocation();

        isLoadedSpawn = true;
    }


    public static Location getSpawnLocation() {
        return currentSpawnLocation;
    }

    public static String getWorldName() {
        return  cacheFile.getString("world-name");
    }

    public static boolean isSpawnLaoded() {
        return isLoadedSpawn;
    }


    private static void serializeLocation() {

        String world = currentSpawnLocation.getWorld().getName();
        double[] cords = new double[] {currentSpawnLocation.getX(), currentSpawnLocation.getY(), currentSpawnLocation.getZ()};

        cacheFile.set("world-name", world);
        cacheFile.set("cord-x", cords[0]);
        cacheFile.set("cord-y", cords[1]);
        cacheFile.set("cord-z", cords[2]);
        cacheFile.set("cord-yaw", currentSpawnLocation.getYaw());
        cacheFile.set("cord-pitch", currentSpawnLocation.getPitch());


        cacheFile.save();


    }

    private static Location deserializeLocation() {
        String w = cacheFile.getString("world-name");
        String x = cacheFile.getString("cord-x");
        String y = cacheFile.getString("cord-y");
        String z = cacheFile.getString("cord-z");
        String yaw = cacheFile.getString("cord-yaw");
        String pitch = cacheFile.getString("cord-pitch");



        if(w == null || x == null || y == null || z == null || yaw == null || pitch == null || Bukkit.getWorld(w) == null) return null;

        World world = Bukkit.getWorld(w);


        String[] c = new String[]{x,y,z};

        double[] cords = Arrays.stream(c).mapToDouble(Double::parseDouble).toArray();

        return new Location(world, cords[0], cords[1], cords[2], Float.valueOf(yaw),  Float.valueOf(pitch));
    }



}
