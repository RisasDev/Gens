package dev.risas.zurixgens.controllers;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.models.events.EventType;
import dev.risas.zurixgens.tasks.EventTask;
import dev.risas.zurixgens.tasks.EventEndTask;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.FileConfig;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */

@Getter
public class EventController {

    private final ZurixGens plugin;
    private final FileConfig configFile, languageFile;

    private EventType event;
    private EventTask eventTask;
    private EventEndTask eventEndTask;

    public EventController(ZurixGens plugin, FileConfig configFile, FileConfig languageFile) {
        this.plugin = plugin;
        this.configFile = configFile;
        this.languageFile = languageFile;
        this.eventTask = new EventTask(plugin, this, configFile.getInt("event-system.event-time"));
        this.eventTask.start();
        this.onReload();
    }

    public int getDoubleValue() {
        return event == EventType.DOUBLE_VALUE ? 2 : 1;
    }

    public double getDoubleSpeed() {
        return event == EventType.DOUBLE_SPEED ?
                configFile.getDouble("event-system.events.double_speed.speed") :
                0;
    }

    public int getDoubleEnchantedChance() {
        return event == EventType.DOUBLE_ENCHANTED_CHANCE ? 2 : 1;
    }

    public int getDoubleGlowChance() {
        return event == EventType.DOUBLE_GLOW_CHANCE ? 2 : 1;
    }

    public boolean isActiveEvent() {
        return event != null;
    }

    public void startEvent(EventType eventType) {
        this.event = eventType;

        for (String message : languageFile.getStringList("event-message." + eventType.name().toLowerCase() + ".start")) {
            ChatUtil.sendBroadcast(message);
        }

        this.eventEndTask.start();
        this.eventEndTask = new EventEndTask(plugin, this, eventType.getDuration());
    }

    public void stopEvent() {
        if (this.eventEndTask != null) {
            for (String message : languageFile.getStringList("event-message." + event.name().toLowerCase() + ".stop")) {
                ChatUtil.sendBroadcast(message);
            }

            this.eventEndTask.cancel();
            this.event = null;
        }

        this.eventTask = new EventTask(plugin, this, configFile.getInt("event-system.event-time"));
        this.eventTask.start();
    }

    public void onReload() {
        ConfigurationSection section = configFile.getConfiguration().getConfigurationSection("event-system.events");
        if (section == null) throw new IllegalStateException("No events configured in the config file.");

        for (EventType eventType : EventType.values()) {
            String eventTypeId = eventType.name().toLowerCase();

            eventType.setName(section.getString(eventTypeId + ".name"));
            eventType.setDuration(section.getInt(eventTypeId + ".duration"));
        }
    }
}
