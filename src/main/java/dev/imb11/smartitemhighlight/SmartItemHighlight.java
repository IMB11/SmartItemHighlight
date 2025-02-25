package dev.imb11.smartitemhighlight;

import dev.imb11.smartitemhighlight.api.SIHEvent;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.events.ItemHighlightEvents;
import dev.imb11.smartitemhighlight.conditions.EnchantmentCondition;
import net.minecraft.client.renderer.RenderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

public class SmartItemHighlight {
    public static final String MOD_ID = "smartitemhighlight";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashSet<String> highlightedItems = new HashSet<>();

    public static void initialize() {
        HighlightCondition.TYPES.put(EnchantmentCondition.SERIALIZATION_ID, EnchantmentCondition.CODEC);

        ItemHighlightEvents.SHOULD_RENDER.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> highlightedItems.contains(stack.getItem().getDescriptionId()) ? SIHEvent.CallbackResult.SUCCESS : SIHEvent.CallbackResult.CONTINUE));
        ItemHighlightEvents.RENDER_HIGHLIGHT.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> {
            drawContext.fill(RenderType.gui(), x, y, x + 16, y + 16, Integer.MAX_VALUE);
            Utils.renderTexture(drawContext, "textures/star.png", x, y, 16, 16);
        }));
    }
}
