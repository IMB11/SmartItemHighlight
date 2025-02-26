package dev.imb11.smartitemhighlight.api.condition;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
    public interface RenderFunction {
        void render(GuiGraphics drawContext, int x, int y);
    }