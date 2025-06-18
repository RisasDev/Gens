package dev.risas.zurixgens.utilities.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class SubCommand {

    private final String description;
    private final List<String> parameters;
    private final String permission;
    private final boolean playerOnly;

    public SubCommand(List<String> parameters, String permission, String description, boolean playerOnly) {
        this.parameters = parameters;
        this.permission = permission;
        this.description = description;
        this.playerOnly = playerOnly;
    }

    public SubCommand(List<String> parameters, String permission, String description) {
        this(parameters, permission, description, false);
    }

    public SubCommand(List<String> parameter, String description, boolean playerOnly) {
        this(parameter, "", description, playerOnly);
    }

    public SubCommand(List<String> parameter, String description) {
        this(parameter, "", description, false);
    }

    public SubCommand(String permission, String description, boolean playerOnly) {
        this(new ArrayList<>(), permission, description, playerOnly);
    }

    public SubCommand(String permission, String description) {
        this(new ArrayList<>(), permission, description, false);
    }

    public SubCommand(String description, boolean playerOnly) {
        this(new ArrayList<>(), "", description, playerOnly);
    }

    public SubCommand(String description) {
        this(new ArrayList<>(), "", description, false);
    }

    public String getParametersFormatted() {
        return parameters.isEmpty() ? "" : " " + String.join(" ", parameters);
    }

    public boolean hasPermission(CommandSender sender) {
        return permission.isEmpty() || sender.hasPermission(permission);
    }

    public abstract void execute(CommandSender sender, String label, String[] args);
}
