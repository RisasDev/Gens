package dev.risas.zurixgens.listeners;

import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    private final UserController userController;

    public UserListener(UserController userController) {
        this.userController = userController;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        User user = userController.createUser(event.getUniqueId(), event.getName());
        userController.loadUser(user);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onPlayerLoginEvent(PlayerLoginEvent event) {
        User user = userController.getUser(event.getPlayer().getUniqueId());
        if (user != null) return;

        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(ChatUtil.translate("&c[ZurixGens] Error al cargar tu cuenta. Por favor ingrese nuevamente."));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        User user = userController.getUser(event.getPlayer().getUniqueId());
        if (user == null) return;

        userController.destroyUser(user);
    }
}
