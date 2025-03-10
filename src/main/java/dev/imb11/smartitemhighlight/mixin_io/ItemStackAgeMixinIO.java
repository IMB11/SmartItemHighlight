package dev.imb11.smartitemhighlight.mixin_io;

import net.minecraft.client.multiplayer.ClientLevel;

public interface ItemStackAgeMixinIO {
    int SIH$getInventoryTicks();
    boolean SIH$isInPlayerInventory(ClientLevel level);
}
