package dev.imb11.smartitemhighlight.api.render.builtin;

import com.google.gson.JsonObject;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.Utils;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class DefaultRenderFunction implements RenderFunction {
    public static final ResourceLocation ID = SmartItemHighlight.loc("default");

    @Override
    public Map<String, Class<?>> getSuggestedRenderOptions() {
        return Map.of(
                "width", Integer.class,
                "height", Integer.class,
                "color", Color.class
        );
    }

    @Override
    public void render(HighlightCondition condition, ItemStack stack, int seed, GuiGraphics graphics, int x, int y, int z) {
        Optional<JsonObject> renderOptions = condition.getRenderOptions();

        graphics.fill(RenderType.gui(), x, y,
                Utils.getOrDefault(renderOptions, "width", 16) + x,
                Utils.getOrDefault(renderOptions, "height", 16) + y,
                Utils.getOrDefault(renderOptions, "color", Integer.MAX_VALUE));
    }
}
