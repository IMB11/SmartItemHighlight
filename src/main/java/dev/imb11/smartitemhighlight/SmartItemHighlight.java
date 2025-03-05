package dev.imb11.smartitemhighlight;

import dev.imb11.mru.LoaderUtils;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.condition.builtin.PlainItemCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import dev.imb11.smartitemhighlight.api.condition.builtin.EnchantmentCondition;
import dev.imb11.smartitemhighlight.api.events.ItemHighlightEvents;
import dev.imb11.smartitemhighlight.api.render.builtin.DefaultRenderFunction;
import dev.imb11.smartitemhighlight.api.render.builtin.PulseSlotOutlineRenderFunction;
import dev.imb11.smartitemhighlight.api.render.builtin.SlotOutlineRenderFunction;
import dev.imb11.smartitemhighlight.api.render.builtin.StarRenderFunction;
import dev.imb11.smartitemhighlight.config.HighlightConditionManager;
import net.minecraft.client.multiplayer.ClientLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.resources.ResourceLocation;

public class SmartItemHighlight {
    public static final String MOD_ID = "smartitemhighlight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_FOLDER = LoaderUtils.getConfigFolder(MOD_ID);

    public static void initialize() {
        ItemHighlightEvents.RENDER_HIGHLIGHT.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> {
            for (HighlightCondition loadedCondition : HighlightConditionManager.getLoadedConditions()) {
               if (loadedCondition.isEnabled())
                   if (loadedCondition.shouldHighlightStack((ClientLevel) world, stack)) {
                       ResourceLocation renderFunction = loadedCondition.getRenderFunction();
                       RenderFunction function = RenderFunction.RENDER_FUNCTION_REGISTRY.get(renderFunction);
                       if (function != null)
                           function.render(loadedCondition, world, livingEntity, stack, seed, drawContext, x, y, z);
                   }
            }
        }));

        HighlightConditionManager.register(EnchantmentCondition.SERIALIZATION_ID, EnchantmentCondition.CODEC);
        HighlightConditionManager.register(PlainItemCondition.SERIALIZATION_ID, PlainItemCondition.CODEC);

        RenderFunction.RENDER_FUNCTION_REGISTRY.put(DefaultRenderFunction.ID, new DefaultRenderFunction());
        RenderFunction.RENDER_FUNCTION_REGISTRY.put(StarRenderFunction.ID, new StarRenderFunction());
        RenderFunction.RENDER_FUNCTION_REGISTRY.put(SlotOutlineRenderFunction.ID, new SlotOutlineRenderFunction());
        RenderFunction.RENDER_FUNCTION_REGISTRY.put(PulseSlotOutlineRenderFunction.ID, new PulseSlotOutlineRenderFunction());
//        RenderFunction.RENDER_FUNCTION_REGISTRY.put(OutlineRenderFunction.ID, new OutlineRenderFunction());

        HighlightConditionManager.load();

        // TODO: need a is dev env LoaderUtils function added to MRU... maybe do this with the Sounds util stuff that needs moving.
        if (LoaderUtils.getGameDir().getFileName().toString().equals("run")) {
            StringBuilder markdownRenderFunctionDocs = new StringBuilder("""
                    # Render Function Documentation
                    This document outlines each registered render function's various optional `renderOptions` for reference.
                    """);
            for (Map.Entry<ResourceLocation, RenderFunction> resourceLocationRenderFunctionEntry : RenderFunction.RENDER_FUNCTION_REGISTRY.entrySet()) {
                ResourceLocation id = resourceLocationRenderFunctionEntry.getKey();
                RenderFunction function = resourceLocationRenderFunctionEntry.getValue();
                markdownRenderFunctionDocs.append("\n# `%s`\n%s".formatted(id.toString(), function.getSuggestedRenderOptions().entrySet().stream().map(entry -> String.format("- `%s` of type `%s`\n", entry.getKey(), entry.getValue().getTypeName())).collect(Collectors.joining())));
            }

            try {
                Files.writeString(CONFIG_FOLDER.resolve("render-function-docs.md"), markdownRenderFunctionDocs.toString(), StandardCharsets.UTF_16);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}