package dev.imb11.smartitemhighlight.mixins;

import dev.imb11.smartitemhighlight.mixin_io.GuiGraphicsAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.*;

@Mixin(GuiGraphics.class)
public class MinecraftClientAccessorMixin implements GuiGraphicsAccessor {
    @Final
    @Shadow private Minecraft minecraft;

    @Unique
    public Minecraft SIH$getClient() {
        return minecraft;
    }
}
