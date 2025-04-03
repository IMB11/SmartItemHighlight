package dev.imb11.smartitemhighlight.api.render.builtin;

import com.google.gson.JsonObject;
import dev.imb11.mru.JSONUtils;
import dev.imb11.mru.RenderUtils;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class PulseSlotOutlineRenderFunction implements RenderFunction {
    public static final ResourceLocation ID = SmartItemHighlight.loc("pulse_slot_outline");

    @Override
    public Map<String, Class<?>> getSuggestedRenderOptions() {
        return Map.of(
                "width", Integer.class,
                "height", Integer.class,
                "color", Color.class,
                "speed", Integer.class,
                "easing", RenderUtils.Easing.class
        );
    }

    @Override
    public void render(HighlightCondition condition, ItemStack stack, int seed, GuiGraphics graphics, int x, int y, int z, boolean hasRenderedItem) {
        if (hasRenderedItem) return;

        Optional<JsonObject> renderOptions = condition.getRenderOptions();

        double time = System.currentTimeMillis() / 0.5D;
        float pulseSpeed = JSONUtils.getOrDefault(renderOptions, "speed", 5);
        RenderUtils.Easing easingType = RenderUtils.Easing.valueOf(JSONUtils.getOrDefault(renderOptions, "easing", RenderUtils.Easing.easeInOutSine.name()));
        int color = JSONUtils.getOrDefault(renderOptions, "color", 0xFFFF0000);

        double brightnessFactor = easingType.getFunction().apply(time * 0.0001 * pulseSpeed).doubleValue();
        int a = (int) (((color >> 24) & 0xFF) * brightnessFactor);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        color = (a << 24) | (r << 16) | (g << 8) | b;

        graphics.renderOutline(x, y,
                JSONUtils.getOrDefault(renderOptions, "width", 16),
                JSONUtils.getOrDefault(renderOptions, "height", 16),
                color
        );
    }
}
