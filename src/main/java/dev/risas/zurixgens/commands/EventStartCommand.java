package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.models.events.EventType;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class EventStartCommand extends SubCommand {

    private final EventController eventController;

    public EventStartCommand(EventController eventController) {
        super(List.of("<event>"), "Inicia un evento.");
        this.eventController = eventController;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            ChatUtil.sendMessage(sender, "&cUsage: /" + label + " start <event>");
            return;
        }

        String eventName = args[1].toUpperCase();
        EventType eventType;

        try {
            eventType = EventType.valueOf(eventName);
        }
        catch (IllegalArgumentException e) {
            ChatUtil.sendMessage(sender, "&cEvent '" + eventName + "' not found.");
            return;
        }

        eventController.startEvent(eventType);
        ChatUtil.sendMessage(sender, "&fHas iniciado el evento &6" + eventType.getName() + "&f.");
    }
}
