package dev.imb11.smartitemhighlight.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.ConcurrentHashMap;

public class JavaCondition extends HighlightCondition {
    /**
     * A map containing the current trigger states of all registered JavaConditions.
     */
    public static ConcurrentHashMap<ResourceLocation, Boolean> TRIGGER_MAP = new ConcurrentHashMap<>();
    public static final ResourceLocation SERIALIZATION_ID = SmartItemHighlight.loc("java_triggered");
    public static final MapCodec<JavaCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
            extendCodec(instance)
                    .and(ResourceLocation.CODEC.fieldOf("trigger_id").forGetter(JavaCondition::getTriggerID))
                    .and(HighlightCondition.CODEC.fieldOf("sub_condition").forGetter(JavaCondition::getSubCondition))
                    .apply(instance, JavaCondition::new));

    private final ResourceLocation triggerID;
    private final HighlightCondition subCondition;

    public JavaCondition(boolean enabled, ResourceLocation renderFunction, ResourceLocation triggerID, HighlightCondition subCondition) {
        super(enabled, renderFunction);
        this.triggerID = triggerID;
        this.subCondition = subCondition;

        TRIGGER_MAP.putIfAbsent(this.triggerID, false);
    }

    public ResourceLocation getTriggerID() {
        return this.triggerID;
    }

    @Override
    public ResourceLocation getSerializationId() {
        return SERIALIZATION_ID;
    }

    private HighlightCondition getSubCondition() {
        return this.subCondition;
    }

    @Override
    public boolean shouldHighlightStack(ClientLevel level, ItemStack stack) {
        return TRIGGER_MAP.getOrDefault(this.triggerID, false) && this.subCondition.shouldHighlightStack(level, stack);
    }
}
