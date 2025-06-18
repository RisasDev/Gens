package dev.risas.zurixgens.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Risas
 * @date 16-06-2025
 * @discord https://risas.me/discord
 */

@UtilityClass
public class SerializeUtil {

    public String serializeBlockLocation(Location location) {
        if (location == null) return null;

        World world = location.getWorld();
        if (world == null) return null;

        return world.getName() + ";" +
               location.getBlockX() + ";" +
               location.getBlockY() + ";" +
               location.getBlockZ();
    }

    public Location deserializeBlockLocation(String data) {
        if (data == null || data.isEmpty()) return null;

        String[] parts = data.split(";");
        if (parts.length != 4) return null;

        World world = Bukkit.getWorld(parts[0]);
        if (world == null) return null;

        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);

        return new Location(world, x, y, z);
    }
}
