package dev.imb11.smartitemhighlight.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public interface HighlightCondition {
    Map<ResourceLocation, MapCodec<? extends HighlightCondition>> TYPES = Map.of();
    Codec<HighlightCondition> CODEC = ResourceLocation.CODEC.dispatch("type", HighlightCondition::getSerializationId, TYPES::get);
    ResourceLocation getSerializationId();

    /**
     * Whether the condition is true for a given stack.
     * @param stack The stack to check
     * @return If true the stack will be highlighted in the inventory.
     */
    boolean shouldHighlightStack(ClientLevel level, ItemStack stack);

    /**
     * Whether the condition is enabled or not.
     * @return If false, {@link HighlightCondition#shouldHighlightStack(ItemStack)} will be skipped and assumed to be false.
     */
    boolean isEnabled();
}
