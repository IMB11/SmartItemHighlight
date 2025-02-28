package dev.imb11.smartitemhighlight.api.render;

import com.mojang.serialization.Codec;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface RenderFunction {
    Map<ResourceLocation, RenderFunction> RENDER_FUNCTION_REGISTRY = new HashMap<>();

    void render(HighlightCondition condition, Level world, LivingEntity livingEntity, ItemStack stack, int seed, GuiGraphics graphics, int x, int y, int z);
}