package dev.imb11.smartitemhighlight.config;

import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConditionEditorScreen extends Screen {
    private final ArrayList<ConditionCardWidget> cards;
    private final Screen parent;

    public ConditionEditorScreen(Screen parent) {
        super(Component.empty());
        this.cards = new ArrayList<>();
        this.parent = parent;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    @Override
    protected void init() {
        super.init();

        int startX = 10;
        int startY = 10;
        int cardSpacingY = 75;
        int index = 0;

        // Assume this static method returns a list of loaded conditions.
        List<HighlightCondition> loadedConditions = HighlightConditionManager.getLoadedConditions();

        for (HighlightCondition condition : loadedConditions) {
            int cardY = startY + index * cardSpacingY;
            // Add the custom card widget.
            var card = this.addRenderableWidget(new ConditionCardWidget(startX, cardY, condition));

            // Add the Edit button at (13, 42) relative to card.
            card.addConnectedWidget(this.addRenderableWidget(Button.builder(Component.literal("Edit"), button -> {
                // Edit action logic here.
            }).bounds(startX + 13, cardY + 42, 79, 16).build()));

            // Add the Disable button at (98, 42) relative to card.
            card.addConnectedWidget(this.addRenderableWidget(Button.builder(condition.isEnabled() ? Component.translatable("highlight.enabled").withStyle(ChatFormatting.GREEN) : Component.translatable("highlight.disabled").withStyle(ChatFormatting.RED), button -> {
                condition.setEnabled(!condition.isEnabled());
                button.setMessage(condition.isEnabled() ? Component.translatable("highlight.enabled").withStyle(ChatFormatting.GREEN) : Component.translatable("highlight.disabled").withStyle(ChatFormatting.RED));
                try {
                    HighlightConditionManager.save(condition);
                } catch (IOException e) {
                    SystemToast.add(this.minecraft.getToastManager(), SystemToast.SystemToastId.LOW_DISK_SPACE, Component.translatable("highlight.disable_update_file.fail"), Component.translatable("highlight.disable_update_file.fail.msg"));
                }
            }).bounds(startX + 98, cardY + 42, 79, 16).build()));

            // Add the Delete button at (183, 42) relative to card.
            card.addConnectedWidget(this.addRenderableWidget(Button.builder(Component.literal("Delete"), button -> {
                boolean success = HighlightConditionManager.delete(card.getCondition());
                if (!success) {
                    assert this.minecraft != null;
                    SystemToast.add(this.minecraft.getToastManager(), SystemToast.SystemToastId.LOW_DISK_SPACE, Component.translatable("highlight.delete.fail"), Component.translatable("highlight.delete.fail.msg"));
                    return;
                }
                this.minecraft.setScreen(new ConditionEditorScreen(this.parent));
            }).bounds(startX + 183, cardY + 42, 79, 16).build()));

            // Add the Preview button at (267, 5) relative to card.
//            card.addConnectedWidget(this.addRenderableWidget(Button.builder(Component.literal("preview of R.F?"), button -> {
//                // Preview action logic here.
//            }).bounds(startX + 267, cardY + 5, 57, 53).build()));

            this.cards.add(card);

            index++;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
    }
}