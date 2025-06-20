package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.models.events.EventType;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class EventListCommand extends SubCommand {

    public EventListCommand() {
        super("Lista todos los eventos disponibles.");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        ChatUtil.sendMessage(sender, ChatUtil.NORMAL_LINE);
        ChatUtil.sendMessage(sender, "&6&lEvents");
        ChatUtil.sendMessage(sender, "");

        for (EventType eventType : EventType.values()) {
            ChatUtil.sendMessage(sender, " &7● &f" + eventType.name() + " - " + eventType.getDuration());
        }

        ChatUtil.sendMessage(sender, ChatUtil.NORMAL_LINE);
    }
}
