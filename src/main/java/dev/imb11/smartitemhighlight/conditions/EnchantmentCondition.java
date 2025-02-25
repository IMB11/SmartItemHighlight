package dev.imb11.smartitemhighlight.conditions;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

public class EnchantmentCondition implements HighlightCondition {
    public static final ResourceLocation SERIALIZATION_ID = ResourceLocation.fromNamespaceAndPath(SmartItemHighlight.MOD_ID, "enchantment");
    public static final MapCodec<EnchantmentCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").forGetter(EnchantmentCondition::isEnabled),
            ResourceLocation.CODEC.fieldOf("enchantment").forGetter(EnchantmentCondition::getEnchantment),
            Codec.INT.optionalFieldOf("level").forGetter(EnchantmentCondition::getLevel),
            ComparisonType.CODEC.optionalFieldOf("comparisonType").forGetter(EnchantmentCondition::getComparisonType)
    ).apply(instance, EnchantmentCondition::new));

    public boolean enabled;

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

    public EnchantmentCondition(boolean enabled, ResourceLocation enchantment, Optional<Integer> level, Optional<ComparisonType> comparisonType) {
        this.enabled = enabled;
        this.enchantment = enchantment;
        this.level = level;
        this.comparisonType = comparisonType;
    }

    public EnchantmentCondition(boolean enabled, ResourceLocation enchantment, ComparisonType comparisonType) {
        this(enabled, enchantment, Optional.empty(), Optional.of(comparisonType));
    }

    public EnchantmentCondition(boolean enabled, ResourceLocation enchantment, Integer level) {
        this(enabled, enchantment, Optional.of(level), Optional.empty());
    }

    public EnchantmentCondition(boolean enabled, ResourceLocation enchantment) {
        this(enabled, enchantment, Optional.empty(), Optional.empty());
    }

    @Override
    public ResourceLocation getSerializationId() {
        return SERIALIZATION_ID;
    }

    @Override
    public boolean shouldHighlightStack(ClientLevel level, ItemStack stack) {
        if (stack.isEnchanted()) {
            final Registry<Enchantment> enchantmentRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Optional<Enchantment> enchantmentOptional = enchantmentRegistry.getOptional(enchantment);

            if (enchantmentOptional.isEmpty()) {
                this.enabled = false;
                SmartItemHighlight.LOGGER.error("Error! {} does not reference a valid enchantment. Disabling until the next config reload.", this);
                return false;
            } else {
                int itemLevel = stack.getEnchantments().getLevel(Holder.direct(enchantmentOptional.get()));

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
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String toString() {
        return "EnchantmentCondition{" +
                "enchantment=" + enchantment +
                '}';
    }
}
