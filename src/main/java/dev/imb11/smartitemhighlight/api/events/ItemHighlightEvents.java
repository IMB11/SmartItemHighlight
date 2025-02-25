package dev.imb11.smartitemhighlight.api.events;

import dev.imb11.smartitemhighlight.api.SIHEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ItemHighlightEvents {
    SIHEvent<ShouldRenderHighlightCallback> SHOULD_RENDER = new SIHEvent<>(
            callbacks -> (drawContext, livingEntity, world, stack, x, y, seed, guiOffset) -> {
                for (ShouldRenderHighlightCallback callback : callbacks) {
                    SIHEvent.CallbackResult result = callback.test(drawContext, livingEntity, world, stack, x, y, seed, guiOffset);
                    if (!result.equals(SIHEvent.CallbackResult.CONTINUE))
                        return result;
                }
                return SIHEvent.CallbackResult.FAIL;
            });
    SIHEvent<HighlightItem> RENDER_HIGHLIGHT = new SIHEvent<>(
            callbacks -> (drawContext, livingEntity, world, stack, x, y, seed, guiOffset) -> {
                for (HighlightItem callback : callbacks)
                    callback.apply(drawContext, livingEntity, world, stack, x, y, seed, guiOffset);
            });

    @FunctionalInterface
    interface HighlightItem {
        void apply(GuiGraphics drawContext, LivingEntity entity, Level world, ItemStack stack, int x, int y, int seed, int guiOffset);
    }

    @FunctionalInterface
    interface ShouldRenderHighlightCallback {
        SIHEvent.CallbackResult test(GuiGraphics drawContext, LivingEntity entity, Level world, ItemStack stack, int x, int y, int seed, int guiOffset);
    }
}
