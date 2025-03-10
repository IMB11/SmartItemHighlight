package dev.imb11.smartitemhighlight.api.condition;

import com.google.gson.JsonObject;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;

public abstract class HighlightCondition {
    @ApiStatus.Internal
    public static final Map<ResourceLocation, MapCodec<? extends HighlightCondition>> TYPES = new HashMap<>();

    public static <T extends HighlightCondition> Products.P2<RecordCodecBuilder.Mu<T>, Boolean, ResourceLocation> extendCodec(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
                Codec.BOOL.fieldOf("enabled").forGetter(HighlightCondition::isEnabled),
                ResourceLocation.CODEC.fieldOf("renderFunction").forGetter(HighlightCondition::getRenderFunction)
        );
    }

    public static final Codec<HighlightCondition> CODEC = ResourceLocation.CODEC.dispatch("type", HighlightCondition::getSerializationId, TYPES::get);

    protected @Nullable String fileID;
    protected final ResourceLocation renderFunction;
    protected Optional<JsonObject> renderOptions = Optional.empty();
    protected boolean enabled;

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public HighlightCondition(boolean enabled, ResourceLocation renderFunction) {
        this.enabled = enabled;
        this.renderFunction = renderFunction;
    }

    public void setRenderOptions(JsonObject renderOptions) {
        this.renderOptions = Optional.of(renderOptions);
    }

    public Optional<JsonObject> getRenderOptions() {
        return renderOptions;
    }

    public abstract ResourceLocation getSerializationId();

    public abstract boolean shouldHighlightStack(ClientLevel level, ItemStack stack);

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ResourceLocation getRenderFunction() {
        return renderFunction;
    }

    public String getFileID() {
        return fileID == null ? String.valueOf(this.hashCode()) : fileID;
    }

    public enum RenderType {
        TEXTURED,
        TEXTURE_TINTABLE,
        CUSTOM
    }
}