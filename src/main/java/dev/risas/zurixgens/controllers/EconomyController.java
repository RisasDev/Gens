package dev.risas.zurixgens.controllers;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

@Getter
public class EconomyController {

    private final Economy economy;

    public EconomyController() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            throw new RuntimeException("Vault economy provider not found.");
        }

        this.economy = rsp.getProvider();
    }

    public double getBalance(Player player) {
        return economy.getBalance(player);
    }

    public void giveBalance(Player player, double amount) {
        EconomyResponse response = economy.depositPlayer(player, amount);
        economy.format(response.amount);
    }

    public void removeBalance(Player player, double amount) {
        EconomyResponse response = economy.withdrawPlayer(player, amount);
        economy.format(response.amount);
    }

    public boolean hasNotBalance(Player player, double amount) {
        return !economy.has(player, amount);
    }
}
