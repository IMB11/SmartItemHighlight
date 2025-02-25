package dev.imb11.smartitemhighlight;

import net.minecraft.client.renderer.RenderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

public class Main {
    public static final String MOD_ID = "smartitemhighlight";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashSet<String> highlightedItems = new HashSet<>();

    public static void initialize() {
        Api.SHOULD_HIGHLIGHT.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> highlightedItems.contains(stack.getItem().getDescriptionId()) ? Event.CALLBACK_RESULT.SUCCESS : Event.CALLBACK_RESULT.CONTINUE));
        Api.HIGHLIGHT_ITEM.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> {
            drawContext.fill(RenderType.gui(), x, y, x + 16, y + 16, Integer.MAX_VALUE);
            Utils.renderTexture(drawContext, "textures/star.png", x, y, 16, 16);
        }));
    }
}
