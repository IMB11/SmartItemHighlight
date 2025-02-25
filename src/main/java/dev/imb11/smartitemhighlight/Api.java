package dev.imb11.smartitemhighlight;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Api {
    @FunctionalInterface
    public interface ShouldHighlight {
        Event.CALLBACK_RESULT test(GuiGraphics drawContext, LivingEntity entity, Level world, ItemStack stack, int x, int y, int seed, int guiOffset);
    }

    public static final Event<ShouldHighlight> SHOULD_HIGHLIGHT = new Event<>(
            callbacks -> (drawContext, livingEntity, world, stack, x, y, seed, guiOffset) -> {
                for (ShouldHighlight callback : callbacks) {
                    Event.CALLBACK_RESULT result = callback.test(drawContext, livingEntity, world, stack, x, y, seed, guiOffset);
                    if (!result.equals(Event.CALLBACK_RESULT.CONTINUE))
                        return result;
                }
                return Event.CALLBACK_RESULT.FAIL;
            });

    @FunctionalInterface
    public interface HighlightItem {
        void apply(GuiGraphics drawContext, LivingEntity entity, Level world, ItemStack stack, int x, int y, int seed, int guiOffset);
    }

    public static final Event<HighlightItem> HIGHLIGHT_ITEM = new Event<>(
            callbacks -> (drawContext, livingEntity, world, stack, x, y, seed, guiOffset) -> {
                for (HighlightItem callback : callbacks)
                    callback.apply(drawContext, livingEntity, world, stack, x, y, seed, guiOffset);
            });
}
