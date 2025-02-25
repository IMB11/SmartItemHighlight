package dev.imb11.smartitemhighlight;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class Utils {
    public static void renderTexture(GuiGraphics drawContext, String path, int x, int y, int textureWidth, int textureHeight) {
        //? 1.21 {
        /*drawContext.blit(ResourceLocation.fromNamespaceAndPath(SmartItemHighlight.MOD_ID, path), x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        *///?} else {
        drawContext.blit(RenderType::guiTexturedOverlay, ResourceLocation.fromNamespaceAndPath(SmartItemHighlight.MOD_ID, path), x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        //?}
    }
}
