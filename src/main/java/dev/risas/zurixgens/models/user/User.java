package dev.risas.zurixgens.models.user;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.generator.GeneratorPlayer;
import dev.risas.zurixgens.tasks.GeneratorTask;
import dev.risas.zurixgens.utilities.FileConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class User {

    private UUID uuid;
    private String name, lowerName;
    private FileConfig dataFile;
    private List<GeneratorPlayer> generators;
    private int maxGenerators;
    private GeneratorTask generatorTask;
    private boolean receiveGenerator;
    private double multiplier;

    private int totalItems, totalEnchantedItems, totalGlowItems, totalPurchases;

    public User(ZurixGens plugin, UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.lowerName = name.toLowerCase();
        this.dataFile = new FileConfig(plugin, "data/user-data/" + uuid.toString() + ".yml");
        this.generators = new ArrayList<>();
        this.maxGenerators = 30;
        this.multiplier = 0;
    }

    public Set<GeneratorPlayer> getAliveGenerators() {
        return generators.stream()
                .filter(generatorPlayer -> !generatorPlayer.isBroken())
                .collect(Collectors.toSet());
    }

    public void addGenerator(
            Player player,
            GeneratorPlayer generatorPlayer,
            Location location) {
        generators.add(generatorPlayer);
        player.spawnParticle(Particle.VILLAGER_HAPPY, location, 10, 0.5, 0.5, 0.5, 0.1);
    }

    public void removeGenerator(Player player, GeneratorPlayer generatorPlayer, Location location) {
        generators.remove(generatorPlayer);
        player.spawnParticle(Particle.CLOUD, location, 10, 0.5, 0.5, 0.5, 0.1);
    }

    public int getGeneratorCount() {
        return generators.size();
    }

    public boolean hasReachedMaxGenerators() {
        return getGeneratorCount() >= maxGenerators;
    }

    public void addMaxGenerator(int amount) {
        this.maxGenerators += amount;
    }

    public void addMultiplier(double amount) {
        this.multiplier += amount;
    }

    public String getMultiplierFormatted() {
        return String.format(Locale.US, "%.1f", multiplier);
    }

    public void addTotalItems(int amount) {
        this.totalItems += amount;
    }

    public void addTotalEnchantedItems(int amount) {
        this.totalEnchantedItems += amount;
    }

    public void addTotalGlowItems(int amount) {
        this.totalGlowItems += amount;
    }

    public void addTotalPurchases(int amount) {
        this.totalPurchases += amount;
    }

    public void startGeneratorTask(
            ZurixGens plugin,
            FileConfig configFile,
            UserController userController,
            GeneratorController generatorController,
            EventController eventController) {
        if (generatorTask != null) {
            generatorTask.cancel();
        }

        generatorTask = new GeneratorTask(
                plugin,
                configFile,
                this,
                userController,
                generatorController,
                eventController
        );
        generatorTask.start();
    }

    public void stopGeneratorTask() {
        if (generatorTask != null) {
            generatorTask.cancel();
            generatorTask = null;
        }
    }

    public boolean isGeneratorTaskRunning() {
        return generatorTask != null;
    }
}
