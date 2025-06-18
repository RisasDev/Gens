package dev.risas.zurixgens.models.user.storage;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.models.user.IUser;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.FileConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class UserFlatFile implements IUser {

    private final ZurixGens plugin;
    private final GeneratorController generatorController;

    public UserFlatFile(ZurixGens plugin, GeneratorController generatorController) {
        this.plugin = plugin;
        this.generatorController = generatorController;
    }

    @Override
    public User createUser(UUID uuid, String name) {
        return new User(plugin, uuid, name);
    }

    @Override
    public void saveUser(User user) {
        toSavable(user);
    }

    @Override
    public void loadUser(User user) {
        ConfigurationSection section = user.getDataFile().getConfiguration();

        if (!section.getKeys(false).isEmpty()) {
            this.loadUser(user, section);
        }
    }

    public void loadUser(User user, ConfigurationSection section) {
        user.setGenerators(generatorController.getGeneratorsPlayer(user.getUuid()));
        user.setMaxGenerators(section.getInt("maxGenerators"));
        user.setReceiveGenerator(section.getBoolean("receiveGenerator"));
        user.setMultiplier(section.getDouble("multiplier"));
        user.setTotalItems(section.getInt("totalItems"));
        user.setTotalEnchantedItems(section.getInt("totalEnchantedItems"));
        user.setTotalGlowItems(section.getInt("totalGlowItems"));
        user.setTotalPurchases(section.getInt("totalPurchases"));
    }

    public void toSavable(User user) {
        FileConfig userDataFile = user.getDataFile();
        ConfigurationSection section = userDataFile.getConfiguration();

        section.set("name", user.getName());
        section.set("lowerName", user.getLowerName());
        section.set("maxGenerators", user.getMaxGenerators());
        section.set("receiveGenerator", user.isReceiveGenerator());
        section.set("multiplier", user.getMultiplier());
        section.set("totalItems", user.getTotalItems());
        section.set("totalEnchantedItems", user.getTotalEnchantedItems());
        section.set("totalGlowItems", user.getTotalGlowItems());

        userDataFile.save();
    }
}
