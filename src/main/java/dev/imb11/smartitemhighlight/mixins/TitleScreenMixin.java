package dev.imb11.smartitemhighlight.mixins;

import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Pseudo
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component title) {
        super(title);
    }

    /**
     * SmartItemHighlight writes the documentation for all currently registered render
     * functions after the game has fully loaded to ensure that all mods get the chance
     * to be shown in the documentation if they register custom render functions.
     */
    @Inject(method = "init", at = @At("HEAD"), cancellable = false)
    public void writeRenderFunctionDocumentation(CallbackInfo ci) {
        CompletableFuture.runAsync(() -> {
            StringBuilder markdownRenderFunctionDocs = new StringBuilder("""
                    # Render Function Documentation
                    This document outlines each registered render function's various optional `renderOptions` for reference.
                    """);

            for (Map.Entry<ResourceLocation, RenderFunction> resourceLocationRenderFunctionEntry : RenderFunction.RENDER_FUNCTION_REGISTRY.entrySet()) {
                ResourceLocation id = resourceLocationRenderFunctionEntry.getKey();
                RenderFunction function = resourceLocationRenderFunctionEntry.getValue();
                markdownRenderFunctionDocs.append("\n## `%s`\n%s".formatted(id.toString(), function.getSuggestedRenderOptions().entrySet().stream().map(entry -> String.format("- `%s` of type `%s`\n", entry.getKey(), entry.getValue().getTypeName())).collect(Collectors.joining())));
            }

            try {
                Files.writeString(SmartItemHighlight.CONFIG_FOLDER.resolve("render-function-docs.md"), markdownRenderFunctionDocs.toString(), StandardCharsets.UTF_16);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
