package dev.risas.zurixgens.controllers;

import dev.risas.zurixgens.models.generator.GeneratorMultiplierItem;
import dev.risas.zurixgens.models.generator.GeneratorSlotItem;
import dev.risas.zurixgens.models.generator.Generator;
import dev.risas.zurixgens.models.generator.GeneratorPlayer;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.SerializeUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Risas
 * @date 15-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorController {

    private final FileConfig configFile, generatorsFile, generatorsDataFile;
    private final Map<String, Generator> generators;
    private final Map<Location, GeneratorPlayer> generatorPlayers;
    private final Map<String, GeneratorSlotItem> generatorSlotItems;
    private final Map<String, GeneratorMultiplierItem> generatorMultiplierItems;

    public GeneratorController(FileConfig configFile, FileConfig generatorsFile, FileConfig generatorsDataFile) {
        this.configFile = configFile;
        this.generatorsFile = generatorsFile;
        this.generatorsDataFile = generatorsDataFile;
        this.generators = new LinkedHashMap<>();
        this.generatorPlayers = new HashMap<>();
        this.generatorSlotItems = new HashMap<>();
        this.generatorMultiplierItems = new HashMap<>();
        this.onLoad();
    }

    public GeneratorSlotItem getGeneratorSlotItem(String slotId) {
        return generatorSlotItems.get(slotId);
    }

    public GeneratorMultiplierItem getGeneratorMultiplierItem(String multiplierId) {
        return generatorMultiplierItems.get(multiplierId);
    }

    public Collection<Generator> getGenerators() {
        return generators.values();
    }

    public Generator getGenerator(String id) {
        return generators.get(id);
    }

    public Generator getNextGenerator(String currentId) {
        boolean found = false;

        for (Map.Entry<String, Generator> entry : generators.entrySet()) {
            if (found) return entry.getValue();
            if (entry.getKey().equals(currentId)) found = true;
        }

        return null;
    }

    public GeneratorPlayer getGeneratorPlayer(Location location) {
        return generatorPlayers.get(location);
    }

    public List<GeneratorPlayer> getGeneratorsPlayer(UUID uuid) {
        return generatorPlayers.values().stream()
                .filter(generatorPlayer -> generatorPlayer.getOwner().equals(uuid))
                .collect(Collectors.toList());
    }

    public void addGeneratorPlayer(
            User user,
            Player player,
            GeneratorPlayer generatorPlayer,
            Location location) {
        generatorPlayers.put(location, generatorPlayer);
        user.addGenerator(player, generatorPlayer, location);

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);

        saveGeneratorPlayer(generatorPlayer, false);
    }

    public void removeGeneratorPlayer(
            User user,
            Player player,
            GeneratorPlayer generatorPlayer,
            Location location) {
        generatorPlayers.remove(location);
        user.removeGenerator(player, generatorPlayer, location);

        location.getBlock().setType(Material.AIR);
        player.getInventory().addItem(
                generatorPlayer.getGenerator().getItem(this, 1));
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 10.0F, 1.0F);

        saveGeneratorPlayer(generatorPlayer, true);
    }

    public void saveGeneratorPlayer(GeneratorPlayer generatorPlayer, boolean delete) {
        ConfigurationSection section = generatorsDataFile.getConfiguration();
        if (section == null) throw new IllegalStateException("Generators data section cannot be null");

        String location = SerializeUtil.serializeBlockLocation(generatorPlayer.getLocation());

        if (delete) {
            section.set(location, null);
        }
        else {
            section.set(location + ".owner", generatorPlayer.getOwner().toString());
            section.set(location + ".generator", generatorPlayer.getGenerator().getId());
            section.set(location + ".broken", generatorPlayer.isBroken());
            section.set(location + ".generationCount", generatorPlayer.getGenerationCount());
        }

        generatorsDataFile.save();
    }

    public void onLoad() {
        this.onReload();

        ConfigurationSection section = generatorsDataFile.getConfiguration();
        if (section == null) throw new IllegalStateException("Generators data section cannot be null");

        for (String locationId : section.getKeys(false)) {
            ConfigurationSection generatorSection = section.getConfigurationSection(locationId);
            if (generatorSection == null) throw new IllegalStateException("Generator section for " + locationId + " cannot be null");

            Location location = SerializeUtil.deserializeBlockLocation(locationId);
            generatorPlayers.put(location, new GeneratorPlayer(location, generatorSection, this));
        }
    }

    public void onReload() {
        generators.clear();

        ConfigurationSection generatorsSection = generatorsFile.getConfiguration().getConfigurationSection("generators");
        if (generatorsSection == null) throw new IllegalStateException("Generators section cannot be null");

        for (String generatorId : generatorsSection.getKeys(false)) {
            ConfigurationSection generatorSection = generatorsSection.getConfigurationSection(generatorId);
            if (generatorSection == null) throw new IllegalStateException("Generator section for " + generatorId + " cannot be null");

            Generator generator = new Generator(generatorId, generatorSection);
            generators.put(generatorId, generator);
        }

        generatorSlotItems.clear();

        ConfigurationSection slotsSection = configFile.getConfiguration().getConfigurationSection("generator-system.slots");
        if (slotsSection == null) throw new IllegalStateException("Slots section not found in configuration file.");

        for (String slotId : slotsSection.getKeys(false)) {
            ConfigurationSection slotSection = slotsSection.getConfigurationSection(slotId);
            if (slotSection == null) throw new IllegalStateException("Slot section not found - " + slotId);

            generatorSlotItems.put(slotId, new GeneratorSlotItem(slotId, slotSection));
        }

        generatorMultiplierItems.clear();

        ConfigurationSection multipliersSection = configFile.getConfiguration().getConfigurationSection("generator-system.multipliers");
        if (multipliersSection == null) throw new IllegalStateException("Multipliers section not found in configuration file.");

        for (String multiplierId : multipliersSection.getKeys(false)) {
            ConfigurationSection multiplierSection = multipliersSection.getConfigurationSection(multiplierId);
            if (multiplierSection == null) throw new IllegalStateException("Multiplier section not found - " + multiplierId);

            generatorMultiplierItems.put(multiplierId, new GeneratorMultiplierItem(multiplierId, multiplierSection));
        }
    }
}
