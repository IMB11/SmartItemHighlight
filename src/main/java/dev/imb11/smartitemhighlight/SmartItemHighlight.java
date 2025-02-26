package dev.imb11.smartitemhighlight;

import dev.imb11.mru.LoaderUtils;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import dev.imb11.smartitemhighlight.api.condition.builtin.EnchantmentCondition;
import dev.imb11.smartitemhighlight.api.events.ItemHighlightEvents;
import dev.imb11.smartitemhighlight.api.render.builtin.DefaultRenderFunction;
import dev.imb11.smartitemhighlight.api.render.builtin.OutlineRenderFunction;
import dev.imb11.smartitemhighlight.api.render.builtin.StarRenderFunction;
import dev.imb11.smartitemhighlight.config.HighlightConditionManager;
import net.minecraft.client.multiplayer.ClientLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

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

        RenderFunction.RENDER_FUNCTION_REGISTRY.put(DefaultRenderFunction.ID, new DefaultRenderFunction());
        RenderFunction.RENDER_FUNCTION_REGISTRY.put(StarRenderFunction.ID, new StarRenderFunction());
        RenderFunction.RENDER_FUNCTION_REGISTRY.put(OutlineRenderFunction.ID, new OutlineRenderFunction());

        HighlightConditionManager.load();
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}