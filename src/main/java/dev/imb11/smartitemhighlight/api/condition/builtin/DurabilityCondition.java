package dev.imb11.smartitemhighlight.api.condition.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DurabilityCondition extends HighlightCondition {
    public static final ResourceLocation SERIALIZATION_ID = SmartItemHighlight.loc("durability");
    public static final MapCodec<DurabilityCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
            extendCodec(instance)
                    .and(Codec.INT.fieldOf("durabilityLevel").forGetter(DurabilityCondition::getDurabilityLevel))
                    .and(ComparisonType.CODEC.fieldOf("comparisonType").forGetter(DurabilityCondition::getComparisonType))
                    .apply(instance, DurabilityCondition::new));

    private final int durabilityLevel;
    private final ComparisonType comparisonType;

    public DurabilityCondition(boolean enabled, ResourceLocation renderFunction, int durabilityLevel, ComparisonType comparisonType) {
        super(enabled, renderFunction);
        this.durabilityLevel = durabilityLevel;
        this.comparisonType = comparisonType;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public int getDurabilityLevel() {
        return durabilityLevel;
    }

    @Override
    public ResourceLocation getSerializationId() {
        return SERIALIZATION_ID;
    }

    @Override
    public boolean shouldHighlightStack(ClientLevel level, ItemStack stack) {
        if (stack.isDamageableItem()) {
            int durability = stack.getMaxDamage() - stack.getDamageValue();
            return switch (comparisonType) {
                case EQUAL -> durability == getDurabilityLevel();
                case NOT_EQUAL -> durability != getDurabilityLevel();
                case GREATER_THAN -> durability > getDurabilityLevel();
                case LESS_THAN -> durability < getDurabilityLevel();
                case GREATER_THAN_EQUAL_TO -> durability >= getDurabilityLevel();
                case LESS_THAN_EQUAL_TO -> durability <= getDurabilityLevel();
            };
        } else {
            return false;
        }
    }
}
