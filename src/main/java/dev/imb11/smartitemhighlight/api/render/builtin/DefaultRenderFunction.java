package dev.imb11.smartitemhighlight.api.render.builtin;

import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DefaultRenderFunction implements RenderFunction {
    public static final ResourceLocation ID = SmartItemHighlight.loc("default");

    @Override
    public void render(HighlightCondition condition, Level world, LivingEntity livingEntity, ItemStack stack, int seed, GuiGraphics drawContext, int x, int y, int z) {
        drawContext.fill(RenderType.gui(), x, y, x + 16, y + 16, Integer.MAX_VALUE);
    }
}
