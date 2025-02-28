package dev.imb11.smartitemhighlight.api.condition.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Optional;

public class EnchantmentCondition extends HighlightCondition {
    public static final ResourceLocation SERIALIZATION_ID = SmartItemHighlight.loc("enchantment");
    public static final MapCodec<EnchantmentCondition> CODEC = RecordCodecBuilder.<EnchantmentCondition>mapCodec(instance ->
            extendCodec(instance)
                    .and(ResourceLocation.CODEC.fieldOf("enchantment").forGetter(EnchantmentCondition::getEnchantment))
                    .and(Codec.INT.optionalFieldOf("level").forGetter(EnchantmentCondition::getLevel))
                    .and(ComparisonType.CODEC.optionalFieldOf("comparisonType").forGetter(EnchantmentCondition::getComparisonType))
                    .apply(instance, EnchantmentCondition::new));

    public EnchantmentCondition(boolean enabled, ResourceLocation renderFunction, ResourceLocation enchantment, Optional<Integer> level, Optional<ComparisonType> comparisonType) {
        super(enabled, renderFunction);
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
                //? if 1.21.4 {
                int itemLevel = stack.getEnchantments().getLevel(enchantmentRegistry.wrapAsHolder(enchantmentOptional.get()));
                //?} else {
                /*// This is a mess and can probably be improved
                int itemLevel = stack.getEnchantments().getLevel(stack.getEnchantments().entrySet().stream().filter(h -> h.getKey().value().equals(enchantmentOptional.get())).findFirst().orElseThrow().getKey());
                *///?}
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