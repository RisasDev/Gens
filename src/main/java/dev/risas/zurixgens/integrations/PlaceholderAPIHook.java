package dev.risas.zurixgens.integrations;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.controllers.UserController;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class PlaceholderAPIHook {

    public void initialize(ZurixGens plugin, UserController userController, EventController eventController) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            ZurixGensExpansion papi = new ZurixGensExpansion(plugin, userController, eventController);
            if (!papi.isRegistered()) papi.register();
        }
    }
}
