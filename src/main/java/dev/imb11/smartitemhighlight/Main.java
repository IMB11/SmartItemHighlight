package dev.imb11.smartitemhighlight;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

public class Main {
    public enum loaders {
        FORGE,
        NEOFORGE,
        FABRIC
    }

    public static final String MOD_ID = "smart-item-highlight";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashSet<String> highlightedItems = new HashSet<>();

    public static void initialize(loaders loader) {
        ModCommands.initialize();

        Api.SHOULD_HIGHLIGHT.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> highlightedItems.contains(stack.getDescriptionId()) ? Event.CALLBACK_RESULT.SUCCESS : Event.CALLBACK_RESULT.CONTINUE));
        Api.HIGHLIGHT_ITEM.register(((drawContext, livingEntity, world, stack, x, y, seed, z) -> {
            drawContext.fill(RenderType.gui(), x, y, x + 16, y + 16, Integer.MAX_VALUE);
//            if (loader.equals(loaders.FABRIC))
            drawContext.blit(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "textures/star.png"), x, y, 0, 0, 16, 16, 16, 16);
//            else
//                drawContext.blit(RenderType::guiTextured, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "textures/star.png"), x, y, 0, 0, 16, 16, 16, 16);
        }));
    }
}
