package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class EventStopCommand extends SubCommand {

    private final EventController eventController;

    public EventStopCommand(EventController eventController) {
        super(List.of("<event>"), "Finaliza un evento activo.");
        this.eventController = eventController;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            ChatUtil.sendMessage(sender, "&cUsage: /" + label + " stop <event>");
            return;
        }

        if (!eventController.isActiveEvent()) {
            ChatUtil.sendMessage(sender, "&cNo hay eventos activos en este momento.");
            return;
        }

        ChatUtil.sendMessage(sender, "&fHas finalizado el evento &6" + eventController.getEvent().getName() + "&f.");
        eventController.stopEvent();
    }
}
