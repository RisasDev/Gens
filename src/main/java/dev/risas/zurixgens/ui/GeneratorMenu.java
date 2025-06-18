package dev.risas.zurixgens.ui;

import dev.risas.zurixgens.controllers.EconomyController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.ui.buttons.GeneratorButton;
import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.menu.Button;
import dev.risas.zurixgens.utilities.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorMenu extends Menu {

    private final FileConfig languageFile;
    private final UserController userController;
    private final GeneratorController generatorController;
    private final EconomyController economyController;

    public GeneratorMenu(
            Player player,
            FileConfig languageFile,
            UserController userController,
            GeneratorController generatorController,
            EconomyController economyController) {
        super(
                player,
                "Generators",
                6
        );
        this.languageFile = languageFile;
        this.userController = userController;
        this.generatorController = generatorController;
        this.economyController = economyController;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        generatorController.getGenerators().forEach(generator ->
                buttons.put(buttons.size(), new GeneratorButton(generator, languageFile, userController, generatorController, economyController)));

        return buttons;
    }
}
