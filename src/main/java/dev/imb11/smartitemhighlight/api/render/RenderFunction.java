package dev.imb11.smartitemhighlight.api.render;

import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface RenderFunction {
    Map<ResourceLocation, RenderFunction> RENDER_FUNCTION_REGISTRY = new HashMap<>();
    RenderFunction NONE = (condition, stack, seed, graphics, x, y, z) -> {};

    default Map<String, Class<?>> getSuggestedRenderOptions() {
        return Map.of();
    }

    void render(HighlightCondition condition, ItemStack stack, int seed, GuiGraphics graphics, int x, int y, int z);
}