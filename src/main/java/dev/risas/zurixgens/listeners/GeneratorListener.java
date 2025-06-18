package dev.risas.zurixgens.listeners;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.controllers.EconomyController;
import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.generator.Generator;
import dev.risas.zurixgens.models.generator.GeneratorMultiplierItem;
import dev.risas.zurixgens.models.generator.GeneratorPlayer;
import dev.risas.zurixgens.models.generator.GeneratorSlotItem;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.ui.GeneratorRepairMenu;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.PersistentDataUtil;
import dev.risas.zurixgens.utilities.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author Risas
 * @date 15-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorListener implements Listener {

    private final ZurixGens plugin;
    private final FileConfig configFile;
    private final UserController userController;
    private final GeneratorController generatorController;
    private final EconomyController economyController;
    private final EventController eventController;

    public GeneratorListener(
            ZurixGens plugin,
            FileConfig configFile,
            UserController userController,
            GeneratorController generatorController,
            EconomyController economyController,
            EventController eventController) {
        this.plugin = plugin;
        this.configFile = configFile;
        this.userController = userController;
        this.generatorController = generatorController;
        this.economyController = economyController;
        this.eventController = eventController;
    }

    @EventHandler
    public void onGeneratorSlotItem(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();

        String slotItemId = persistentDataContainer.get(PersistentDataUtil.SLOT_ITEM_ID, PersistentDataType.STRING);
        if (slotItemId == null) return;

        GeneratorSlotItem slotItem = generatorController.getGeneratorSlotItem(slotItemId);
        if (slotItem == null) return;

        Player player = event.getPlayer();
        User user = userController.getUser(player.getUniqueId());

        slotItem.incrementSlot(user, userController);
        PlayerUtil.decrementItem(player, itemStack);

        ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &fHas incrementado tu limite de generadores a &a" + user.getMaxGenerators() + " &fgeneradores.");
    }

    @EventHandler
    public void onGeneratorMultiplierItem(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();

        String multiplierItemId = persistentDataContainer.get(PersistentDataUtil.MULTIPLIER_ITEM_ID, PersistentDataType.STRING);
        if (multiplierItemId == null) return;

        GeneratorMultiplierItem multiplierItem = generatorController.getGeneratorMultiplierItem(multiplierItemId);
        if (multiplierItem == null) return;

        Player player = event.getPlayer();
        User user = userController.getUser(player.getUniqueId());

        multiplierItem.incrementMultiplier(user, userController);
        PlayerUtil.decrementItem(player, itemStack);

        ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &fHas incrementado tu multiplicador de ventas &ex" + user.getMultiplierFormatted() + "&f.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = userController.getUser(player.getUniqueId());

        if (!user.getAliveGenerators().isEmpty()) {
            user.startGeneratorTask(plugin, configFile, userController, generatorController, eventController);
        }

        Generator generator = generatorController.getGenerator("pollo");
        if (generator == null || user.isReceiveGenerator()) return;

        player.getInventory().addItem(generator.getItem(generatorController, configFile.getInt("generator-system.starting-generators")));

        user.setReceiveGenerator(true);
        userController.saveUser(user);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = userController.getUser(player.getUniqueId());
        if (user == null) return;

        user.stopGeneratorTask();
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = event.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        if (persistentDataContainer.has(PersistentDataUtil.GENERATOR_DROP_ID, PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }

        String generatorId = persistentDataContainer.get(PersistentDataUtil.GENERATOR_ID, PersistentDataType.STRING);
        if (generatorId == null) return;

        Generator generator = generatorController.getGenerator(generatorId);
        if (generator == null) return;

        User user = userController.getUser(player.getUniqueId());

        if (user.hasReachedMaxGenerators()) {
            event.setCancelled(true);
            ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &cHas alcanzado el limite de generadores que puedes colocar.");
            return;
        }

        Location location = event.getBlockPlaced().getLocation();

        GeneratorPlayer generatorPlayer = new GeneratorPlayer(player.getUniqueId(), generator, location);
        generatorController.addGeneratorPlayer(user, player, generatorPlayer, location);

        if (!user.isGeneratorTaskRunning()) {
            user.startGeneratorTask(plugin, configFile, userController, generatorController, eventController);
        }

        ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &7Has colocado un " + generator.getDisplayName() + " &7("
                + user.getGeneratorCount() + "/" + user.getMaxGenerators() + ")");
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();

        GeneratorPlayer generatorPlayer = generatorController.getGeneratorPlayer(location);
        if (generatorPlayer == null) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Location location = block.getLocation();
        GeneratorPlayer generatorPlayer = generatorController.getGeneratorPlayer(location);
        if (generatorPlayer == null) return;

        Player player = event.getPlayer();

        if (generatorPlayer.isNotOwned(player.getUniqueId())) {
            ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &cEste generador no es tuyo.");
            return;
        }

        if (generatorPlayer.isBroken()) {
            GeneratorRepairMenu menu = new GeneratorRepairMenu(player, generatorPlayer, generatorController, economyController);
            menu.open();
            return;
        }

        if (action == Action.LEFT_CLICK_BLOCK && !player.isSneaking()) {
            User user = userController.getUser(player.getUniqueId());
            generatorController.removeGeneratorPlayer(user, player, generatorPlayer, location);

            if (user.getGeneratorCount() <= 0) {
                user.stopGeneratorTask();
            }

            ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &7Has recogido un " + generatorPlayer.getGenerator().getDisplayName() + " &7("
                    + user.getGeneratorCount() + "/" + user.getMaxGenerators() + ")");
        }
        else if (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
            Generator nextGenerator = generatorController.getNextGenerator(generatorPlayer.getGenerator().getId());

            if (nextGenerator == null) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10.0F, 1.0F);
                ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &cEste generador ya esta en su nivel maximo.");
                return;
            }

            double price = nextGenerator.getPrice();

            if (economyController.hasNotBalance(player, price)) {
                ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &cNo tienes el balance suficiente para mejorar este generador. ($" +
                        + economyController.getBalance(player) + "/$" + price + ")");
                return;
            }

            economyController.removeBalance(player, price);

            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0F, 1.0F);
            ChatUtil.sendMessage(player, "&6&lGENERATORS &8» &7Has mejorado tu generador de " + generatorPlayer.getGenerator().getDisplayName()
                    + " &7a " + nextGenerator.getDisplayName() + "&7.");

            generatorPlayer.upgrade(nextGenerator);
            generatorController.saveGeneratorPlayer(generatorPlayer, false);
        }
    }

    @EventHandler
    public void onGeneratorWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        User user = userController.getUser(player.getUniqueId());
        if (user == null) return;

        GeneratorPlayer generatorPlayer = user.getGenerators().get(0);
        if (generatorPlayer == null) return;

        World generatorWorld = generatorPlayer.getLocation().getWorld();
        if (generatorWorld == null) return;

        World currentWorld = player.getWorld();

        if (currentWorld.equals(generatorWorld) && !user.isGeneratorTaskRunning()) {
            user.startGeneratorTask(plugin, configFile, userController, generatorController, eventController);
        }
        else if (!currentWorld.equals(generatorWorld) && user.isGeneratorTaskRunning()) {
            user.stopGeneratorTask();
        }
    }
}
