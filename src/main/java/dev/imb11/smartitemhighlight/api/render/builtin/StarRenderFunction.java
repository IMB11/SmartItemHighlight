package dev.imb11.smartitemhighlight.api.render.builtin;

import com.google.gson.JsonObject;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.Utils;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;

public class StarRenderFunction implements RenderFunction {
    public static final ResourceLocation ID = SmartItemHighlight.loc("star");

    @Override
    public Map<String, Class<?>> getSuggestedRenderOptions() {
        return Map.of(
                "width", Integer.class,
                "height", Integer.class,
                "texture", ResourceLocation.class
        );
    }

    @Override
    public void render(HighlightCondition condition, ItemStack stack, int seed, GuiGraphics graphics, int x, int y, int z) {
        Optional<JsonObject> renderOptions = condition.getRenderOptions();

        ResourceLocation texture = ResourceLocation.parse(Utils.getOrDefault(renderOptions, "texture", "smartitemhighlight:textures/star.png"));

        Utils.renderTexture(graphics, texture, x, y, Utils.getOrDefault(renderOptions, "width", 16),
                Utils.getOrDefault(renderOptions, "height", 16));
    }
}
