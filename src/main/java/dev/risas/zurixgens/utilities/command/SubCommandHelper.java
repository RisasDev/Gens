package dev.risas.zurixgens.utilities.command;

import dev.risas.zurixgens.utilities.ChatUtil;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class SubCommandHelper {

    public String build(String template, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            template = template.replace(entry.getKey(), entry.getValue());
        }
        return template;
    }

    public String getSubCommandFormat(String label, Map<String, SubCommand> subCommands, String title) {
        String subcommandsFormatted = subCommands.entrySet().stream()
                .map(entry -> {
                    Map<String, String> placeholders = Map.of(
                            "%label%", label,
                            "%subcommand%", entry.getKey() + entry.getValue().getParametersFormatted(),
                            "%description%", entry.getValue().getDescription()
                    );
                    return build(" &7● &e/%label% %subcommand% &8- &f%description%", placeholders);
                })
                .collect(Collectors.joining("\n"));

        return """
                %line%
                &6&l%title%
                &7
                &f<> &7= &fRequired &7| &f[] &7= &fOptional
                &7
                %subcommands%
                %line%
                """
                .replace("%title%", title)
                .replace("%subcommands%", subcommandsFormatted)
                .replace("%line%", ChatUtil.NORMAL_LINE);
    }

    @SafeVarargs
    public <K, V> LinkedHashMap<K, V> of(Map.Entry<K, V>... entries) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();

        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }
}