package dev.imb11.smartitemhighlight.api.condition.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.ComparisonType;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class FoodCondition extends HighlightCondition {
    public static final ResourceLocation SERIALIZATION_ID = SmartItemHighlight.loc("food");
    public static final MapCodec<FoodCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
            extendCodec(instance)
                    .and(ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("nutrition").forGetter(FoodCondition::getNutrition))
                    .and(ComparisonType.CODEC.optionalFieldOf("nutrition_comparison_type").forGetter(FoodCondition::getNutritionComparisonType))
                    .and(Codec.FLOAT.optionalFieldOf("saturation").forGetter(FoodCondition::getSaturation))
                    .and(ComparisonType.CODEC.optionalFieldOf("saturation_comparison_type").forGetter(FoodCondition::getSaturationComparisonType))
                    .and(Codec.BOOL.optionalFieldOf("still_edible_if_full").forGetter(FoodCondition::isStillEdibleIfFull))
                    .apply(instance, FoodCondition::new));

    private final Optional<Integer> nutrition;
    private final Optional<ComparisonType> nutritionComparisonType;
    private final Optional<Float> saturation;
    private final Optional<ComparisonType> saturationComparisonType;
    private final Optional<Boolean> stillEdibleIfFull;

    public FoodCondition(boolean enabled, ResourceLocation renderFunction, Optional<Integer> nutrition, Optional<ComparisonType> nutritionComparisonType, Optional<Float> saturation, Optional<ComparisonType> saturationComparisonType, Optional<Boolean> stillEdibleIfFull) {
        super(enabled, renderFunction);
        this.nutrition = nutrition;
        this.nutritionComparisonType = nutritionComparisonType;
        this.saturation = saturation;
        this.saturationComparisonType = saturationComparisonType;
        this.stillEdibleIfFull = stillEdibleIfFull;
    }

    public final Optional<Integer> getNutrition() {
        return this.nutrition;
    }

    @Override
    public ResourceLocation getSerializationId() {
        return SERIALIZATION_ID;
    }

    @Override
    public boolean shouldHighlightStack(ClientLevel level, ItemStack stack) {
        if (stack.getComponents().has(DataComponents.FOOD)) {
            AtomicBoolean finalResult = new AtomicBoolean(true);

            FoodProperties properties = stack.getComponents().get(DataComponents.FOOD);
            assert properties != null;

            getNutrition().ifPresent(nutritionValue -> {
                var comparisonType = getNutritionComparisonType().orElse(ComparisonType.EQUAL);

                finalResult.set(finalResult.get() && (switch (comparisonType) {
                    case EQUAL -> properties.nutrition() == nutritionValue;
                    case NOT_EQUAL -> properties.nutrition() != nutritionValue;
                    case LESS_THAN -> properties.nutrition() < nutritionValue;
                    case GREATER_THAN -> properties.nutrition() > nutritionValue;
                    case LESS_THAN_EQUAL_TO -> properties.nutrition() <= nutritionValue;
                    case GREATER_THAN_EQUAL_TO -> properties.nutrition() >= nutritionValue;
                }));
            });

            getSaturation().ifPresent(saturationValue -> {
                var comparisonType = getSaturationComparisonType().orElse(ComparisonType.EQUAL);

                finalResult.set(finalResult.get() && (switch (comparisonType) {
                    case EQUAL -> properties.saturation() == saturationValue;
                    case NOT_EQUAL -> properties.saturation() != saturationValue;
                    case LESS_THAN -> properties.saturation() < saturationValue;
                    case GREATER_THAN -> properties.saturation() > saturationValue;
                    case LESS_THAN_EQUAL_TO -> properties.saturation() <= saturationValue;
                    case GREATER_THAN_EQUAL_TO -> properties.saturation() >= saturationValue;
                }));
            });

            isStillEdibleIfFull().ifPresent(isEdibleIfFull -> {
                finalResult.set(finalResult.get() && (properties.canAlwaysEat() && isEdibleIfFull));
            });

            return finalResult.get();
        } else {
            return false;
        }
    }

    public Optional<ComparisonType> getNutritionComparisonType() {
        return nutritionComparisonType;
    }

    public Optional<Float> getSaturation() {
        return saturation;
    }

    public Optional<ComparisonType> getSaturationComparisonType() {
        return saturationComparisonType;
    }

    public Optional<Boolean> isStillEdibleIfFull() {
        return stillEdibleIfFull;
    }
}
