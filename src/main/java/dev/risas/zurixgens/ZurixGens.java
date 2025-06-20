package dev.risas.zurixgens;

import dev.risas.zurixgens.commands.*;
import dev.risas.zurixgens.controllers.EconomyController;
import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.integrations.PlaceholderAPIHook;
import dev.risas.zurixgens.listeners.GeneratorListener;
import dev.risas.zurixgens.listeners.MenuListener;
import dev.risas.zurixgens.listeners.UserListener;
import dev.risas.zurixgens.utilities.FileConfig;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ZurixGens extends JavaPlugin {

    private FileConfig configFile,
            languageFile,
            menusFile,
            generatorsFile,
            generatorsDataFile;
    private GeneratorController generatorController;
    private UserController userController;
    private EconomyController economyController;
    private EventController eventController;

    @Override
    public void onEnable() {
        this.configFile = new FileConfig(this, "config.yml");
        this.languageFile = new FileConfig(this, "language.yml");
        this.menusFile = new FileConfig(this, "menus.yml");
        this.generatorsFile = new FileConfig(this, "generators.yml");
        this.generatorsDataFile = new FileConfig(this, "data/generators-data.yml");

        this.generatorController = new GeneratorController(configFile, generatorsFile, generatorsDataFile);
        this.userController = new UserController(this, configFile, generatorController);
        this.economyController = new EconomyController();
        this.eventController = new EventController(this, configFile, languageFile);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new UserListener(userController), this);
        pluginManager.registerEvents(new MenuListener(), this);
        pluginManager.registerEvents(new GeneratorListener(this, configFile, languageFile, generatorsDataFile, userController, generatorController, economyController, eventController), this);

        this.getCommand("generator").setExecutor(new GeneratorCommand(this, languageFile, menusFile, userController, generatorController, economyController));
        this.getCommand("generator").setTabCompleter(new GeneratorCommand(this, languageFile, menusFile, userController, generatorController, economyController));
        this.getCommand("sell").setExecutor(new SellCommand(configFile, languageFile, userController, economyController, eventController));
        this.getCommand("statistic").setExecutor(new StatisticCommand(languageFile, userController));
        this.getCommand("event").setExecutor(new EventCommand(eventController));
        this.getCommand("event").setTabCompleter(new EventCommand(eventController));
        this.getCommand("pickupgens").setExecutor(new PickupGensCommand(userController, generatorController));

        PlaceholderAPIHook.initialize(this, userController, eventController);
    }

    public void onReload() {
        this.configFile.reload();
        this.languageFile.reload();
        this.menusFile.reload();
        this.generatorsFile.reload();
        this.generatorController.onReload();
        this.eventController.onReload();
    }

    public static ZurixGens getInstance() {
        return getPlugin(ZurixGens.class);
    }
}
