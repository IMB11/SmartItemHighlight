package dev.imb11.smartitemhighlight.config;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.TagList;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.condition.builtin.EnchantmentCondition;
import dev.imb11.smartitemhighlight.api.condition.builtin.PlainItemCondition;
import dev.imb11.smartitemhighlight.api.render.builtin.StarRenderFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HighlightConditionManager {
    private static final Path CONDITIONS_PATH = SmartItemHighlight.CONFIG_FOLDER.resolve("conditions/");
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    public static ArrayList<HighlightCondition> getLoadedConditions() {
        return LOADED_CONDITIONS;
    }

    public static String getRawFile(String id) throws IOException {
        Path filePath = CONDITIONS_PATH.resolve(id + ".json");
        return Files.readString(filePath, StandardCharsets.UTF_16);
    }

    private static ArrayList<HighlightCondition> LOADED_CONDITIONS = new ArrayList<>();

    private static HighlightCondition readFromFile(Path filePath) throws IOException {
        JsonElement element = GSON.fromJson(Files.readString(filePath, StandardCharsets.UTF_16), JsonElement.class);
        DataResult<HighlightCondition> conditionDataResult = HighlightCondition.CODEC.parse(JsonOps.INSTANCE, element);
        var result = conditionDataResult.getOrThrow();
        if (element.getAsJsonObject().has("renderOptions")) {
            result.setRenderOptions(element.getAsJsonObject().getAsJsonObject("renderOptions"));
        }
        return result;
    }

    private static void writeToFile(Path filePath, HighlightCondition condition) throws IOException {
        DataResult<JsonElement> result = HighlightCondition.CODEC.encodeStart(JsonOps.INSTANCE, condition);
        JsonElement element = result.getOrThrow();
        Files.writeString(filePath, GSON.toJson(element), StandardCharsets.UTF_16);
    }

    private static void createDefaultExamples() {
        try {
            CONDITIONS_PATH.toFile().mkdirs();
            var examplesFolder = CONDITIONS_PATH.resolve("examples/");
            examplesFolder.toFile().mkdirs();

            writeToFile(examplesFolder.resolve("example_enchantment_condition.json"), new EnchantmentCondition(true, ResourceLocation.fromNamespaceAndPath(SmartItemHighlight.MOD_ID, "default"),
                    Enchantments.EFFICIENCY.location(), Optional.of(1), Optional.of(ComparisonType.EQUAL)));
            writeToFile(examplesFolder.resolve("example_plain_item_condition.json"), new PlainItemCondition(true, StarRenderFunction.ID, new TagList<>(List.of(
                    TagList.ofTag(ItemTags.AXES),
                    TagList.ofObj(Items.COOKED_BEEF, BuiltInRegistries.ITEM)
            ))));

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void load() {
        if (!CONDITIONS_PATH.toFile().exists()) {
            createDefaultExamples();
            return;
        }

        try {
            LOADED_CONDITIONS.clear();

            for (String conditionFilename : CONDITIONS_PATH.toFile().list((dir, name) -> name.endsWith(".json"))) {
                var cond = readFromFile(CONDITIONS_PATH.resolve(conditionFilename));
                cond.setFileID(conditionFilename.replace(".json", ""));
                LOADED_CONDITIONS.add(cond);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(ResourceLocation serializationId, MapCodec<? extends HighlightCondition> codec) {
        HighlightCondition.TYPES.put(serializationId, codec);
    }

    public static boolean delete(HighlightCondition condition) {
        LOADED_CONDITIONS.remove(condition);
        return CONDITIONS_PATH.resolve(condition.getFileID() + ".json").toFile().delete();
    }

    public static void save(HighlightCondition condition) throws IOException {
        writeToFile(CONDITIONS_PATH.resolve(condition.getFileID() + ".json"), condition);
    }
}
