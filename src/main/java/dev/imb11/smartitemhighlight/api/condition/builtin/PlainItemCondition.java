package dev.imb11.smartitemhighlight.api.condition.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.TagList;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PlainItemCondition extends HighlightCondition {
    public static final ResourceLocation SERIALIZATION_ID = SmartItemHighlight.loc("matches_item");
    public static final MapCodec<PlainItemCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
            extendCodec(instance)
                    .and(TagList.getCodec(Registries.ITEM).fieldOf("items").forGetter(PlainItemCondition::getMatchingItems))
                    .apply(instance, PlainItemCondition::new));
    private final TagList<Item> matchingItems;

    public PlainItemCondition(boolean enabled, ResourceLocation renderFunction, TagList<Item> matchingItems) {
        super(enabled, renderFunction);
        this.matchingItems = matchingItems;
    }

    private TagList<Item> getMatchingItems() {
        return this.matchingItems;
    }

    @Override
    public ResourceLocation getSerializationId() {
        return SERIALIZATION_ID;
    }

    @Override
    public boolean shouldHighlightStack(ClientLevel level, ItemStack stack) {
        return this.matchingItems.isValid(stack.getItem());
    }
}
