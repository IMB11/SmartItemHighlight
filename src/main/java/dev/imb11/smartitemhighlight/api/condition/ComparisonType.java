package dev.imb11.smartitemhighlight.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ComparisonType implements StringRepresentable {
    GREATER_THAN(">"), LESS_THAN("<"),
    GREATER_THAN_EQUAL_TO(">="), LESS_THAN_EQUAL_TO("<="),
    EQUAL("="), NOT_EQUAL("!=");

    public static final Codec<ComparisonType> CODEC = StringRepresentable.fromEnum(ComparisonType::values);
    private final String value;

    ComparisonType(String value) {
        this.value = value;
    }

    @Override
    public @NotNull String getSerializedName() {
        return value;
    }
}
