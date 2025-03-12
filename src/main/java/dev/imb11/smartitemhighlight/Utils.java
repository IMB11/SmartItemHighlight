package dev.imb11.smartitemhighlight;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import static java.lang.Math.*;

import java.util.Optional;

// TODO: Move all of this to MRU!!
public class Utils {
    public static void renderTexture(GuiGraphics drawContext, ResourceLocation texture, int x, int y, int textureWidth, int textureHeight) {
        //? 1.21 {
        /*drawContext.blit(texture, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        *///?} else {
        drawContext.blit(RenderType::guiTexturedOverlay, texture, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        //?}
    }

    public static int getOrDefault(Optional<JsonObject> object, String fieldName, int defaultValue) {
        return object.filter(jsonObject -> jsonObject.has(fieldName))
                .map(jsonObject -> jsonObject.get(fieldName).getAsInt())
                .orElse(defaultValue);
    }

    public static boolean getOrDefault(Optional<JsonObject> object, String fieldName, boolean defaultValue) {
        return object.filter(jsonObject -> jsonObject.has(fieldName))
                .map(jsonObject -> jsonObject.get(fieldName).getAsBoolean())
                .orElse(defaultValue);
    }

    public static String getOrDefault(Optional<JsonObject> object, String fieldName, String defaultValue) {
        return object.filter(jsonObject -> jsonObject.has(fieldName))
                .map(jsonObject -> jsonObject.get(fieldName).getAsString())
                .orElse(defaultValue);
    }

    public static void drawTextWrapped(GuiGraphics context, Font textRenderer, FormattedText text, int x, int y, int width, int color) {
        for(Iterator<FormattedCharSequence> var7 = textRenderer.split(text, width).iterator(); var7.hasNext(); y += 9) {
            FormattedCharSequence orderedText = var7.next();
            context.drawString(textRenderer, orderedText, x, y, color, false);
            Objects.requireNonNull(textRenderer);
        }
    }

    // https://easings.net/
    // https://gist.github.com/dev-hydrogen/21a66f83f0386123e0c0acf107254843
    public static enum Easing {
        easeInSine(x -> 1 - cos(x * PI) / 2),
        easeOutSine(x -> sin(x * PI) / 2),
        easeInOutSine(x -> -(cos(PI * x) - 1) / 2),
        easeInQuad(x -> x * x),
        easeOutQuad(x -> 1 - (1 - x) * (1 - x)),
        easeInOutQuad(x -> x < 0.5 ? 2 * x * x : 1 - pow(-2 * x + 2, 2) / 2),
        easeInCubic(x -> x * x * x),
        easeOutCubic(x -> 1 - pow(1 - x, 3)),
        easeInOutCubic(x -> x < 0.5 ? 4 * x * x * x : 1 - pow(-2 * x + 2, 3) / 2),
        easeInQuart(x -> x * x * x * x),
        easeOutQuart(x -> 1 - pow(1 - x, 4)),
        easeInOutQuart(x -> x < 0.5 ? 8 * x * x * x * x : 1 - pow(-2 * x + 2, 4) / 2),
        easeInQuint(x -> x * x * x * x * x),
        easeOutQuint(x -> 1 - pow(1 - x, 5)),
        easeInOutQuint(x -> x < 0.5 ? 16 * x * x * x * x * x : 1 - pow(-2 * x + 2, 5) / 2),
        easeInExpo(x -> x == 0 ? 0 : pow(2, 10 * x - 10)),
        easeOutExpo(x -> x == 1 ? 1 : 1 - pow(2, -10 * x)),
        easeInOutExpo(x -> x == 0 ? 0 : x == 1 ? 1 : x < 0.5 ? pow(2, 20 * x - 10) / 2 : (2 - pow(2, -20 * x + 10)) / 2),
        easeInCirc(x -> 1 - sqrt(1 - pow(x, 2))),
        easeOutCirc(x -> sqrt(1 - pow(x - 1, 2))),
        easeInOutCirc(x -> x < 0.5 ? (1 - sqrt(1 - pow(2 * x, 2))) / 2 : (sqrt(1 - pow(-2 * x + 2, 2)) + 1) / 2),
        easeInBack(x -> 2.70158 * x * x * x - 1.70158 * x * x),
        easeOutBack(x -> 1 + 2.70158 * pow(x - 1, 3) + 1.70158 * pow(x - 1, 2)),
        easeInOutBack(x -> x < 0.5 ? (pow(2 * x, 2) * ((1.70158 * 1.525 + 1) * 2 * x - 1.70158 * 1.525)) / 2 : (pow(2 * x - 2, 2) * ((1.70158 * 1.525 + 1) * (x * 2 - 2) + 1.70158 * 1.525) + 2) / 2),
        easeInElastic(x -> x == 0 ? 0 : x == 1 ? 1 : -pow(2, 10 * x - 10) * sin((x * 10 - 10.75) * ((2 * PI) / 3))),
        easeOutElastic(x -> x == 0 ? 0 : x == 1 ? 1 : pow(2, -10 * x) * sin((x * 10 - 0.75) * ((2 * PI) / 3)) + 1),
        easeInOutElastic(x -> x == 0 ? 0 : x == 1 ? 1 : x < 0.5 ? -(pow(2, 20 * x - 10) * sin((20 * x - 11.125) * ((2 * PI) / 4.5))) / 2 : (pow(2, -20 * x + 10) * sin((20 * x - 11.125) * ((2 * PI) / 4.5))) / 2 + 1);

        final Function<Double, Number> function;
        Easing(Function<Double, Number> function) {
            this.function = function;
        }
        public Function<Double, Number> getFunction() {
            return function;
        }
    }
}
