package dev.imb11.smartitemhighlight;

import dev.imb11.mru.LoaderUtils;
import dev.imb11.smartitemhighlight.api.SIHEvent;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import dev.imb11.smartitemhighlight.api.condition.builtin.EnchantmentCondition;
import dev.imb11.smartitemhighlight.api.events.ItemHighlightEvents;
import dev.imb11.smartitemhighlight.api.render.builtin.StarRenderFunction;
import dev.imb11.smartitemhighlight.config.HighlightConditionManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class SmartItemHighlight {
    public static final String MOD_ID = "smartitemhighlight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_FOLDER = LoaderUtils.getConfigFolder(MOD_ID);

    public static HashSet<String> highlightedItems = new HashSet<>();

    public static void initialize() {
        ItemHighlightEvents.SHOULD_RENDER.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> {
            // Do we really need this event anymore now that its being handled by the HighlightCondition class?
            return SIHEvent.CallbackResult.SUCCESS;
        }));

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

        RenderFunction.RENDER_FUNCTION_REGISTRY.put(StarRenderFunction.ID, new StarRenderFunction());

        HighlightConditionManager.load();
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}