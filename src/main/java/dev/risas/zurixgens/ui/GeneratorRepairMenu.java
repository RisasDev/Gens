package dev.risas.zurixgens.ui;

import dev.risas.zurixgens.controllers.EconomyController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.models.generator.GeneratorPlayer;
import dev.risas.zurixgens.ui.buttons.GeneratorRepairButton;
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
public class GeneratorRepairMenu extends Menu {

    private final FileConfig languageFile;
    private final GeneratorPlayer generatorPlayer;
    private final GeneratorController generatorController;
    private final EconomyController economyController;

    public GeneratorRepairMenu(
            Player player,
            GeneratorPlayer generatorPlayer,
            FileConfig languageFile,
            GeneratorController generatorController,
            EconomyController economyController) {
        super(
                player,
                "Generator Repair",
                1
        );
        this.languageFile = languageFile;
        this.generatorPlayer = generatorPlayer;
        this.generatorController = generatorController;
        this.economyController = economyController;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSize() - 5, new GeneratorRepairButton(generatorPlayer, languageFile, generatorController, economyController));

        return buttons;
    }
}
