package dev.imb11.smartitemhighlight;

import dev.imb11.smartitemhighlight.api.SIHEvent;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.events.ItemHighlightEvents;
import dev.imb11.smartitemhighlight.conditions.EnchantmentCondition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

public class SmartItemHighlight {
    public static final String MOD_ID = "smart-item-highlight";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashSet<String> highlightedItems = new HashSet<>();

    public static void initialize() {
        ModCommands.initialize();

        HighlightCondition.TYPES.put(EnchantmentCondition.SERIALIZATION_ID, EnchantmentCondition.CODEC);

        ItemHighlightEvents.SHOULD_RENDER.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> highlightedItems.contains(stack.getItem().getDescriptionId()) ? SIHEvent.CallbackResult.SUCCESS : SIHEvent.CallbackResult.CONTINUE));
        ItemHighlightEvents.RENDER_HIGHLIGHT.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> {
            drawContext.fill(RenderType.gui(), x, y, x + 16, y + 16, Integer.MAX_VALUE);

            drawContext.pose().pushPose();
//            drawContext.pose().translate(0, 0, -1500);
            drawContext.blit(RenderType::guiTextured, ResourceLocation.fromNamespaceAndPath(SmartItemHighlight.MOD_ID, "textures/star.png"), x, y, 0, 0, 16, 16, 16, 16);
            drawContext.pose().popPose();
        }));
    }
}
