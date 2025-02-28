package dev.imb11.smartitemhighlight.api.render.builtin;

import com.google.gson.JsonObject;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.Utils;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
                "easing", Utils.Easing.class
        );
    }

    @Override
    public void render(HighlightCondition condition, Level world, LivingEntity livingEntity, ItemStack stack, int seed, GuiGraphics graphics, int x, int y, int z) {
        Optional<JsonObject> renderOptions = condition.getRenderOptions();

        double time = System.currentTimeMillis() / 0.5D;
        float pulseSpeed = Utils.getOrDefault(renderOptions, "speed", 5);
        Utils.Easing easingType = Utils.Easing.valueOf(Utils.getOrDefault(renderOptions, "easing", Utils.Easing.easeInOutSine.name()));
        int color = Utils.getOrDefault(renderOptions, "color", 0xFFFF0000);

        double brightnessFactor = easingType.getFunction().apply(time * 0.0001 * pulseSpeed).doubleValue();
        int a = (int) (((color >> 24) & 0xFF) * brightnessFactor);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        color = (a << 24) | (r << 16) | (g << 8) | b;

        graphics.renderOutline(x, y,
                Utils.getOrDefault(renderOptions, "width", 16),
                Utils.getOrDefault(renderOptions, "height", 16),
                color
        );
    }
}
