package dev.risas.zurixgens.controllers;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.models.user.IUser;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.models.user.storage.UserFlatFile;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class UserController {

    private final ZurixGens plugin;
    private final IUser user;
    private final Map<UUID, User> users;

    public UserController(ZurixGens plugin, GeneratorController generatorController) {
        this.plugin = plugin;
        this.users = new ConcurrentHashMap<>();
        this.user = new UserFlatFile(plugin, generatorController);
    }

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public User createUser(UUID uuid, String name) {
        User user = this.user.createUser(uuid, name);
        users.put(uuid, user);
        return user;
    }

    public void saveUser(User user) {
        CompletableFuture.runAsync(() -> this.user.saveUser(user));
    }

    public void loadUser(User user) {
        this.user.loadUser(user);
    }

    public void destroyUser(User user) {
        CompletableFuture.runAsync(() -> users.remove(user.getUuid()));
    }
}
