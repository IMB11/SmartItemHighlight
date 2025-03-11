package dev.imb11.smartitemhighlight;

import dev.imb11.mru.LoaderUtils;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.condition.builtin.*;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import dev.imb11.smartitemhighlight.api.events.ItemHighlightEvents;
import dev.imb11.smartitemhighlight.api.render.builtin.DefaultRenderFunction;
import dev.imb11.smartitemhighlight.api.render.builtin.PulseSlotOutlineRenderFunction;
import dev.imb11.smartitemhighlight.api.render.builtin.SlotOutlineRenderFunction;
import dev.imb11.smartitemhighlight.api.render.builtin.StarRenderFunction;
import dev.imb11.smartitemhighlight.config.HighlightConditionManager;
import net.minecraft.client.multiplayer.ClientLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.minecraft.resources.ResourceLocation;

public class SmartItemHighlight {
    public static final String MOD_ID = "smartitemhighlight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Path getConfigFolder() {
        Path currentPath = Paths.get("").toAbsolutePath();
        while (currentPath.endsWith("SmartItemHighlight"))
            currentPath = currentPath.resolve("../");
        if (currentPath.resolve("LocalFile.txt").toFile().exists())
            return currentPath.resolve("run/config/smartitemhighlights");
        return LoaderUtils.getConfigFolder(MOD_ID);
    }

    public static final Path CONFIG_FOLDER = getConfigFolder();

    public static void initialize() {
        ItemHighlightEvents.RENDER_HIGHLIGHT.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> {
            for (HighlightCondition loadedCondition : HighlightConditionManager.getLoadedConditions()) {
                if (loadedCondition.isEnabled())
                    if (loadedCondition.shouldHighlightStack((ClientLevel) world, stack)) {
                        ResourceLocation renderFunction = loadedCondition.getRenderFunction();
                        RenderFunction function = RenderFunction.RENDER_FUNCTION_REGISTRY.get(renderFunction);
                        if (function != null)
                            function.render(loadedCondition, stack, seed, drawContext, x, y, z);
                    }
            }
        }));

        HighlightConditionManager.register(EnchantmentCondition.SERIALIZATION_ID, EnchantmentCondition.CODEC);
        HighlightConditionManager.register(PlainItemCondition.SERIALIZATION_ID, PlainItemCondition.CODEC);
        HighlightConditionManager.register(DurabilityCondition.SERIALIZATION_ID, DurabilityCondition.CODEC);
        HighlightConditionManager.register(FoodCondition.SERIALIZATION_ID, FoodCondition.CODEC);
        HighlightConditionManager.register(PickupCondition.SERIALIZATION_ID, PickupCondition.CODEC);

        RenderFunction.RENDER_FUNCTION_REGISTRY.put(DefaultRenderFunction.ID, new DefaultRenderFunction());
        RenderFunction.RENDER_FUNCTION_REGISTRY.put(StarRenderFunction.ID, new StarRenderFunction());
        RenderFunction.RENDER_FUNCTION_REGISTRY.put(SlotOutlineRenderFunction.ID, new SlotOutlineRenderFunction());
        RenderFunction.RENDER_FUNCTION_REGISTRY.put(PulseSlotOutlineRenderFunction.ID, new PulseSlotOutlineRenderFunction());
//        RenderFunction.RENDER_FUNCTION_REGISTRY.put(OutlineRenderFunction.ID, new OutlineRenderFunction());

        HighlightConditionManager.load();
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}