package dev.imb11.smartitemhighlight.mixins;

import dev.imb11.smartitemhighlight.Api;
import dev.imb11.smartitemhighlight.Event;
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
            at=@At("HEAD")
    )
    private void renderItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci){
        GuiGraphics drawContext = ((GuiGraphics) (Object) this);
        if (Api.SHOULD_HIGHLIGHT.invoker.test(drawContext, entity, level, stack, x, y, seed, guiOffset).equals(Event.CALLBACK_RESULT.SUCCESS))
            Api.HIGHLIGHT_ITEM.invoker.apply(drawContext, entity, level, stack, x, y, seed, guiOffset);
    }
}
