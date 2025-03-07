package dev.imb11.smartitemhighlight.api.condition.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.mixin_io.ItemStackAgeMixinIO;
import dev.imb11.smartitemhighlight.mixins.ItemStackAgeMixin;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PickupCondition extends HighlightCondition {
    public static final ResourceLocation SERIALIZATION_ID = SmartItemHighlight.loc("pickup");
    public static final MapCodec<PickupCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
            extendCodec(instance)
                    .and(Codec.INT.fieldOf("inventoryTime").forGetter(PickupCondition::getInventoryTime))
                    .and(ComparisonType.CODEC.fieldOf("comparisonType").forGetter(PickupCondition::getComparisonType))
                    .apply(instance, PickupCondition::new));

    private final int inventoryTime;
    private final ComparisonType comparisonType;

    public PickupCondition(boolean enabled, ResourceLocation renderFunction, int inventoryTime, ComparisonType comparisonType) {
        super(enabled, renderFunction);
        this.inventoryTime = inventoryTime;
        this.comparisonType = comparisonType;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public int getInventoryTime() {
        return inventoryTime;
    }

    @Override
    public ResourceLocation getSerializationId() {
        return SERIALIZATION_ID;
    }

    @Override
    public boolean shouldHighlightStack(ClientLevel level, ItemStack stack) {
        ItemStackAgeMixinIO mixinStack = (ItemStackAgeMixinIO) (Object) stack;
        assert mixinStack != null;
        int timeInSeconds = mixinStack.SIH$getInventoryTicks() / 20;
        return mixinStack.SIH$isInPlayerInventory(level) && switch (comparisonType) {
            case EQUAL -> timeInSeconds == getInventoryTime();
            case NOT_EQUAL -> timeInSeconds != getInventoryTime();
            case GREATER_THAN -> timeInSeconds > getInventoryTime();
            case LESS_THAN -> timeInSeconds < getInventoryTime();
            case GREATER_THAN_EQUAL_TO -> timeInSeconds >= getInventoryTime();
            case LESS_THAN_EQUAL_TO -> timeInSeconds <= getInventoryTime();
        };
    }
}
