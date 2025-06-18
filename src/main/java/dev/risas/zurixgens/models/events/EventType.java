package dev.risas.zurixgens.models.events;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */

@Getter
public enum EventType {
    DOUBLE_VALUE("x2 Valor", 10),
    DOUBLE_SPEED("x2 Velocidad", 10),
    DOUBLE_ENCHANTED_CHANCE("x2 Probabilidad Encantada", 10),
    DOUBLE_GLOW_CHANCE("x2 Probabilidad Brillante", 10);

    @Setter public String name;
    @Setter public int duration;

    EventType(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }
}
