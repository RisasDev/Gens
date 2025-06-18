package dev.risas.zurixgens.models.generator;

import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.PersistentDataUtil;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Risas
 * @date 15-06-2025
 * @discord https://risas.me/discord
 */

@Getter
public class GeneratorPlayer {

    private final UUID owner;
    private Generator generator;
    private final Location location;
    private long lastGeneratedTime, nextGenerationTime;
    private boolean broken;
    private int generationCount;

    public GeneratorPlayer(UUID owner, Generator generator, Location location) {
        this.owner = owner;
        this.generator = generator;
        this.location = location;
        this.lastGeneratedTime = System.currentTimeMillis();
        this.nextGenerationTime = generator.getTime() * 1000L;
    }

    public GeneratorPlayer(Location location, ConfigurationSection section, GeneratorController generatorController) {
        this.owner = UUID.fromString(section.getString("owner"));
        this.generator = generatorController.getGenerator(section.getString("generator"));
        this.location = location;
        this.lastGeneratedTime = System.currentTimeMillis();
        this.nextGenerationTime = generator.getTime() * 1000L;
        this.broken = section.getBoolean("broken");
        this.generationCount = section.getInt("generationCount");
    }

    public boolean isNotOwned(UUID uuid) {
        return !owner.equals(uuid);
    }

    public void generateDrop(FileConfig configFile, User user, EventController eventController) {
        World world = location.getWorld();
        if (world == null) throw new IllegalStateException("World cannot be null");

        Location dropLocation = location.clone().add(0.5, 1, 0.5);
        GeneratorDrop generatorDrop = generator.getRandomGeneratorDrop();

        ItemStack itemStack = generatorDrop.getDropItem().clone();
        double glowChance = ThreadLocalRandom.current().nextDouble(0, 100);
        double enchantedChance = ThreadLocalRandom.current().nextDouble(0, 100);

        boolean applyGlow = generatorDrop.getGlowChance(eventController) > glowChance;
        boolean applyEnchant = generatorDrop.getEnchantedChance(eventController) > enchantedChance;

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (applyGlow) {
            user.addTotalGlowItems(1);
            container.set(PersistentDataUtil.GENERATOR_DROP_GLOW, PersistentDataType.STRING, "true");
        }

        if (applyEnchant) {
            user.addTotalEnchantedItems(1);
            container.set(PersistentDataUtil.GENERATOR_DROP_ENCHANTED, PersistentDataType.STRING, "true");
        }

        itemStack.setItemMeta(meta);

        Item item = world.dropItem(dropLocation, itemStack);
        item.setVelocity(new Vector(0, 0, 0));

        if (applyGlow) {
            item.setGlowing(true);
        }

        this.lastGeneratedTime = System.currentTimeMillis();
        this.nextGenerationTime = (long) (generator.getTime() * (1.0 - eventController.getDoubleSpeed()) * 1000L);

        this.generationCount++;

        int maxGenerations = configFile.getInt("generator-system.break-generation");

        if (maxGenerations != -1 && generationCount >= maxGenerations) {
            breakGenerator();
        }

        double breakChance = configFile.getDouble("generator-system.break-chance");
        double breakRandomChance = ThreadLocalRandom.current().nextDouble(0, 100);

        if (breakRandomChance < breakChance && !broken) {
            breakGenerator();
        }
    }

    public void breakGenerator() {
        this.broken = true;
        this.location.getBlock().setType(Material.RED_TERRACOTTA);
    }

    public void upgrade(Generator nextGenerator) {
        this.generator = nextGenerator;
        this.lastGeneratedTime = System.currentTimeMillis();
        this.nextGenerationTime = nextGenerator.getTime() * 1000L;
        this.generationCount = 0;
        this.broken = false;

        this.location.getBlock().setType(nextGenerator.getItem().getType());
    }

    public void repair() {
        this.broken = false;
        this.generationCount = 0;
        this.location.getBlock().setType(generator.getItem().getType());
    }
}
