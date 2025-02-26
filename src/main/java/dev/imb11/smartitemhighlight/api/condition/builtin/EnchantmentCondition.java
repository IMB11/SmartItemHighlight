package dev.imb11.smartitemhighlight.api.condition.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

public class EnchantmentCondition extends HighlightCondition {
    public static final ResourceLocation SERIALIZATION_ID = SmartItemHighlight.loc("enchantment");
    public static final MapCodec<EnchantmentCondition> CODEC = RecordCodecBuilder.<EnchantmentCondition>mapCodec(instance ->
            extendCodec(instance)
                    .and(ResourceLocation.CODEC.fieldOf("enchantment").forGetter(EnchantmentCondition::getEnchantment))
                    .and(Codec.INT.optionalFieldOf("level").forGetter(EnchantmentCondition::getLevel))
                    .and(ComparisonType.CODEC.optionalFieldOf("comparisonType").forGetter(EnchantmentCondition::getComparisonType))
            .apply(instance, EnchantmentCondition::new));

    public EnchantmentCondition(boolean enabled, Optional<ResourceLocation> overlay, ResourceLocation renderFunction, ResourceLocation enchantment, Optional<Integer> level, Optional<ComparisonType> comparisonType) {
        super(enabled, overlay, renderFunction);
        this.enchantment = enchantment;
        this.level = level;
        this.comparisonType = comparisonType;
    }

    public ResourceLocation getEnchantment() {
        return enchantment;
    }

    public Optional<Integer> getLevel() {
        return level;
    }

    public Optional<ComparisonType> getComparisonType() {
        return comparisonType;
    }

    public final ResourceLocation enchantment;
    public final Optional<Integer> level;
    public final Optional<ComparisonType> comparisonType;

    @Override
    public ResourceLocation getSerializationId() {
        return SERIALIZATION_ID;
    }

    @Override
    public boolean shouldHighlightStack(ClientLevel level, ItemStack stack) {
        if (stack.isEnchanted()) {
            //? if 1.21.4 {
            final Registry<Enchantment> enchantmentRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Optional<Enchantment> enchantmentOptional = enchantmentRegistry.getOptional(enchantment);
            //?} else {
            /*final HolderLookup.RegistryLookup<Enchantment> enchantmentRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Optional<Enchantment> enchantmentOptional = enchantmentRegistry.get(ResourceKey.create(Registries.ENCHANTMENT, enchantment)).map(Holder.Reference::value);
            *///?}
            if (enchantmentOptional.isEmpty()) {
                this.enabled = false;
                SmartItemHighlight.LOGGER.error("Error! {} does not reference a valid enchantment. Disabling until the next config reload.", this);
                return false;
            } else {
                int itemLevel = stack.getEnchantments().getLevel(enchantmentRegistry.wrapAsHolder(enchantmentOptional.get()));

                if (this.level.isEmpty()) {
                    // If level is not present, we only check for the presence of the enchantment.
                    return this.comparisonType.filter(type -> type == ComparisonType.EQUAL).isPresent();
                } else {
                    int conditionLevel = this.level.get();
                    return this.comparisonType.map(type -> switch (type) {
                        case EQUAL -> itemLevel == conditionLevel;
                        case NOT_EQUAL -> itemLevel != conditionLevel;
                        case GREATER_THAN -> itemLevel > conditionLevel;
                        case LESS_THAN -> itemLevel < conditionLevel;
                        case LESS_THAN_EQUAL_TO -> itemLevel <= conditionLevel;
                        case GREATER_THAN_EQUAL_TO -> itemLevel >= conditionLevel;
                    }).orElse(false);
                }
            }
        } else {
            // If there are no enchantments, we can only allow true if the comparison type is NOT_EQUAL and if level is not present.
            // Essentially, no level but NOT_EQUAL means we should treat it as "Does not have enchantment."
            return this.comparisonType.filter(type -> type == ComparisonType.NOT_EQUAL).isPresent()
                    && this.level.isEmpty();
        }
    }

    @Override
    public String toString() {
        return "EnchantmentCondition{" +
                "enchantment=" + enchantment +
                '}';
    }
}