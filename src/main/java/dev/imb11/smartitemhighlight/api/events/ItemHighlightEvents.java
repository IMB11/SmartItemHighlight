package dev.imb11.smartitemhighlight.api.events;

import dev.imb11.smartitemhighlight.api.SIHEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ItemHighlightEvents {
    SIHEvent<HighlightItem> RENDER_HIGHLIGHT = new SIHEvent<>(
            callbacks -> (drawContext, livingEntity, world, stack, x, y, seed, guiOffset) -> {
                for (HighlightItem callback : callbacks)
                    callback.apply(drawContext, livingEntity, world, stack, x, y, seed, guiOffset);
            });

    @FunctionalInterface
    interface HighlightItem {
        void apply(GuiGraphics drawContext, LivingEntity entity, Level world, ItemStack stack, int x, int y, int seed, int guiOffset);
    }
}
