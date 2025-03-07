package dev.imb11.smartitemhighlight.mixins;

import dev.imb11.smartitemhighlight.mixin_io.ItemStackAgeMixinIO;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackAgeMixin implements ItemStackAgeMixinIO {
    @Unique
    private int SIH$ticksInInventory = 0;

    @Unique
    private int SIH$entityTickCount = 0;

    @Unique
    private long SIH$lastDayTime = 0;

    @Inject(
            method = "inventoryTick",
            at = @At("HEAD")
    )
    private void inventoryTick(Level level, Entity entity, int inventorySlot, boolean isCurrentItem, CallbackInfo ci) {
        if (level.isClientSide()) {
            SIH$lastDayTime = level.getDayTime();
            if (entity instanceof Player) {
                if (SIH$entityTickCount + 1 != entity.tickCount)
                    SIH$ticksInInventory = 0;
                SIH$entityTickCount = entity.tickCount;
                SIH$ticksInInventory++;
            } else {
                SIH$entityTickCount = 0;
                SIH$ticksInInventory = 0;
            }
        }
    }

    @Override
    public int SIH$getInventoryTicks() {
        return SIH$ticksInInventory;
    }

    @Override
    public boolean SIH$isInPlayerInventory(ClientLevel level) {
        return Math.abs(level.getDayTime() - SIH$lastDayTime) < 10 && SIH$ticksInInventory > 0 && SIH$entityTickCount > 0;
    }
}
