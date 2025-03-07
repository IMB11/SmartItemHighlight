package dev.imb11.smartitemhighlight.config;

import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.injection.Inject;

public class ConditionEditorScreen extends Screen {
    protected ConditionEditorScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();


    }
}
