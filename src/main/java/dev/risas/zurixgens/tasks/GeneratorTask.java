package dev.risas.zurixgens.tasks;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.generator.GeneratorPlayer;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.FileConfig;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Risas
 * @date 15-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorTask extends BukkitRunnable {

    private final ZurixGens plugin;
    private final FileConfig configFile;
    private final User user;
    private final UserController userController;
    private final GeneratorController generatorController;
    private final EventController eventController;

    public GeneratorTask(
            ZurixGens plugin,
            FileConfig configFile,
            User user,
            UserController userController,
            GeneratorController generatorController,
            EventController eventController) {
        this.plugin = plugin;
        this.configFile = configFile;
        this.user = user;
        this.userController = userController;
        this.generatorController = generatorController;
        this.eventController = eventController;
    }

    @Override
    public void run() {
        int generateItem = 0;

        for (GeneratorPlayer generatorPlayer : user.getAliveGenerators()) {
            long elapsed = System.currentTimeMillis() - generatorPlayer.getLastGeneratedTime();
            long nextGenerationTime = generatorPlayer.getNextGenerationTime();

            if (nextGenerationTime > 0 && elapsed < nextGenerationTime) continue;

            generateItem++;

            generatorPlayer.generateDrop(configFile, user, eventController);
            generatorController.saveGeneratorPlayer(generatorPlayer, false);
        }

        if (generateItem > 0) {
            user.addTotalItems(generateItem);
            userController.saveUser(user);
        }
    }

    public void start() {
        this.runTaskTimer(plugin, 20L, 20L);
    }
}
