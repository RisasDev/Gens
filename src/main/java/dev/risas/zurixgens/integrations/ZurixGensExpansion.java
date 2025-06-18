package dev.risas.zurixgens.integrations;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.user.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ZurixGensExpansion extends PlaceholderExpansion {

    private final ZurixGens plugin;
    private final UserController userController;
    private final EventController eventController;

    public ZurixGensExpansion(ZurixGens plugin, UserController userController, EventController eventController) {
        this.plugin = plugin;
        this.userController = userController;
        this.eventController = eventController;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return "Risas";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "zurixgens";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        User user = userController.getUser(player.getUniqueId());

        if (user != null) {
            switch (identifier) {
                case "user_multiplicator" -> {
                    return user.getMultiplierFormatted();
                }
                case "user_generators" -> {
                    return String.valueOf(user.getGeneratorCount());
                }
                case "user_max_generators" -> {
                    return String.valueOf(user.getMaxGenerators());
                }
            }
        }

        if (identifier.equalsIgnoreCase("event_time")) {
            return eventController.isActiveEvent() ?
                    eventController.getEventEndTask().getDurationRemaining() :
                    eventController.getEventTask().getDurationRemaining();
        }
        if (identifier.equalsIgnoreCase("event_name")) {
            return eventController.isActiveEvent() ?
                    eventController.getEvent().getName() :
                    "None";
        }

        return null;
    }
}
