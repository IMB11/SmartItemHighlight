package dev.imb11.smartitemhighlight.api.render.builtin;

import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StarRenderFunction implements RenderFunction {
    public static final ResourceLocation ID = SmartItemHighlight.loc("star");

    @Override
    public void render(HighlightCondition condition, Level world, LivingEntity livingEntity, ItemStack stack, int seed, GuiGraphics drawContext, int x, int y, int z) {

    }
}
