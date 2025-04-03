package dev.imb11.smartitemhighlight.mixins;

import dev.imb11.smartitemhighlight.api.events.ItemHighlightEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class HighlightItemMixin {
    @Inject(
            method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
            at = @At("HEAD")
    )
    private void renderItemBefore(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
        ItemHighlightEvents.RENDER_HIGHLIGHT.invoker.apply(((GuiGraphics) (Object) this), entity, level, stack, x, y, seed, guiOffset, false);
    }
    @Inject(
            method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
            at = @At("TAIL")
    )
    private void renderItemAfter(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
        ItemHighlightEvents.RENDER_HIGHLIGHT.invoker.apply(((GuiGraphics) (Object) this), entity, level, stack, x, y, seed, guiOffset, true);
    }
}
