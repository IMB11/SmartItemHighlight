package dev.imb11.smartitemhighlight.api.render.builtin;

import com.google.gson.JsonObject;
import dev.imb11.mru.JSONUtils;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class SlotOutlineRenderFunction implements RenderFunction {
    public static final ResourceLocation ID = SmartItemHighlight.loc("slot_outline");

    @Override
    public Map<String, Class<?>> getSuggestedRenderOptions() {
        return Map.of(
                "width", Integer.class,
                "height", Integer.class,
                "color", Color.class
        );
    }

    @Override
    public void render(HighlightCondition condition, ItemStack stack, int seed, GuiGraphics graphics, int x, int y, int z, boolean hasRenderedItem) {
        if (hasRenderedItem) return;

        Optional<JsonObject> renderOptions = condition.getRenderOptions();

        graphics.renderOutline(x, y,
                JSONUtils.getOrDefault(renderOptions, "width", 16),
                JSONUtils.getOrDefault(renderOptions, "height", 16),
                JSONUtils.getOrDefault(renderOptions, "color", 0xFFFF0000)
        );
    }
}
