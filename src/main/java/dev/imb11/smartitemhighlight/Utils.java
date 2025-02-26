package dev.imb11.smartitemhighlight;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Utils {
    public static void renderTexture(GuiGraphics drawContext, String path, int x, int y, int textureWidth, int textureHeight) {
        //? 1.21 {
        /*drawContext.blit(ResourceLocation.fromNamespaceAndPath(SmartItemHighlight.MOD_ID, path), x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        *///?} else {
        drawContext.blit(RenderType::guiTexturedOverlay, ResourceLocation.fromNamespaceAndPath(SmartItemHighlight.MOD_ID, path), x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        //?}
    }
}
