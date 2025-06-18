package dev.risas.zurixgens.tasks;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.utilities.TimeUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class EventEndTask extends BukkitRunnable {

    private final ZurixGens plugin;
    private final EventController eventController;
    private int duration;

    public EventEndTask(ZurixGens plugin, EventController eventController, int duration) {
        this.plugin = plugin;
        this.eventController = eventController;
        this.duration = duration;
    }

    @Override
    public void run() {
        if (duration <= 0) {
            eventController.stopEvent();
            this.cancel();
            return;
        }

        duration--;
    }

    public void start() {
        this.runTaskTimer(plugin, 20L, 20L);
    }

    public String getDurationRemaining() {
        return TimeUtil.toFormatDurationSeconds(duration);
    }
}
