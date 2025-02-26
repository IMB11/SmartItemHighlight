package dev.imb11.smartitemhighlight.config;

import dev.imb11.mru.LoaderUtils;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;

import java.nio.file.Path;
import java.util.ArrayList;

public class HighlightConditionManager {
    private static final Path CONFIG_PATH = SmartItemHighlight.CONFIG_FOLDER.resolve("highlight-conditions.json");
    private static final ArrayList<HighlightCondition> LOADED_CONDITIONS = new ArrayList<>();

    private static ArrayList<HighlightCondition> createDefault() {
        return new ArrayList<>() {

        };
    }

    public static void load() {
        if (!CONFIG_PATH.toFile().exists()) {
            CONFIG_PATH.toFile().mkdirs();

        }
    }
}
