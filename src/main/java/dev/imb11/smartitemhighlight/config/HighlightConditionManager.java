package dev.imb11.smartitemhighlight.config;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.condition.builtin.EnchantmentCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HighlightConditionManager {
    private static final Path CONFIG_PATH = SmartItemHighlight.CONFIG_FOLDER.resolve("highlight-conditions.json");
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    public static ArrayList<HighlightCondition> getLoadedConditions() {
        return LOADED_CONDITIONS;
    }

    private static ArrayList<HighlightCondition> LOADED_CONDITIONS = new ArrayList<>();

    private static <T extends HighlightCondition> List<T> createDefault() {
        return List.of(
                (T) new EnchantmentCondition(true, Optional.empty(), ResourceLocation.fromNamespaceAndPath(SmartItemHighlight.MOD_ID, "default"),
                        Enchantments.EFFICIENCY.location(), Optional.of(1), Optional.of(ComparisonType.EQUAL))
        );
    }

    public static void save() {
        ArrayList<JsonElement> elements = new ArrayList<>();
        for (HighlightCondition loadedCondition : LOADED_CONDITIONS) {
            DataResult<JsonElement> result = HighlightCondition.CODEC.encodeStart(JsonOps.INSTANCE, loadedCondition);
            result.ifSuccess(elements::add);
        }

        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(elements), StandardCharsets.UTF_16);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() {
        if (!CONFIG_PATH.toFile().exists()) {
            SmartItemHighlight.CONFIG_FOLDER.toFile().mkdirs();
            LOADED_CONDITIONS = new ArrayList<>(createDefault());
            save();
            return;
        }

        try {
            LOADED_CONDITIONS.clear();
            String jsonData = Files.readString(CONFIG_PATH, StandardCharsets.UTF_16);
            JsonArray array = GSON.fromJson(jsonData, JsonArray.class);
            for (JsonElement element : array) {
                DataResult<HighlightCondition> conditionDataResult = HighlightCondition.CODEC.parse(JsonOps.INSTANCE, element);
                conditionDataResult.ifSuccess(condition -> {
                    if (element.getAsJsonObject().has("renderOptions")) {
                        condition.setRenderOptions(element.getAsJsonObject().getAsJsonObject("renderOptions"));
                    }

                    LOADED_CONDITIONS.add(condition);
                }).ifError(err -> SmartItemHighlight.LOGGER.info("Failed to decode HighlightCondition: {}", err.message()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(ResourceLocation serializationId, MapCodec<EnchantmentCondition> codec) {
        HighlightCondition.TYPES.put(serializationId, codec);
    }
}
