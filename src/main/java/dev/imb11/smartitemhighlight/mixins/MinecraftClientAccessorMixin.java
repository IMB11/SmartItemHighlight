package dev.imb11.smartitemhighlight.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphics.class)
public interface MinecraftClientAccessorMixin {
    @Accessor
    Minecraft getMinecraft();
}
