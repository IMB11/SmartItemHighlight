// File: SIHConfigScreen.java
package dev.imb11.smartitemhighlight.config;

import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.Utils;
import dev.imb11.smartitemhighlight.api.TagList;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.condition.builtin.PlainItemCondition;
import dev.imb11.smartitemhighlight.api.render.builtin.PulseSlotOutlineRenderFunction;
import dev.imb11.smartitemhighlight.config.editor.EditorServer;
import dev.imb11.smartitemhighlight.mixins.ScreenAccessorMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SIHConfigScreen extends Screen {
    private final ArrayList<ConditionCardWidget> cards;
    private final Screen parent;
    private int texSize;
    private int sidebarButtonWidth;
    private int sideX;

    public SIHConfigScreen(Screen parent) {
        // Title is rendered custom on the side so we pass an empty Component here.
        super(Component.empty());
        this.cards = new ArrayList<>();
        this.parent = parent;
        HighlightConditionManager.load();
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
        this.cards.clear();

        int startX = 10;
        int startY = 10;
        int cardSpacingY = 75;
        int index = 0;
        int cardWidth = 332;
        int cardHeight = 65;

        // Load existing conditions as usual.
        List<HighlightCondition> loadedConditions = HighlightConditionManager.getLoadedConditions();

        for (HighlightCondition condition : loadedConditions) {
            int cardY = startY + index * cardSpacingY;
            // Create card widget with a maximum width to prevent overlapping the sidebar.
            var card = (new ConditionCardWidget(startX, cardY, cardWidth, cardHeight, condition));

            // Edit button.
            card.addConnectedWidget(new GreyButton(
                    startX + 13,
                    cardY + 42,
                    79,
                    16,
                    Component.translatable("config.edit_button"),
                    button -> {
                        Util.getPlatform().openUri("http://localhost:" + EditorServer.PORT + "/?id=" + condition.getFileID());
                    },
                    t -> {
                        return Component.translatable("config.edit_description");
                    }
            ));

// Disable button:
            card.addConnectedWidget(new GreyButton(
                    startX + 98,
                    cardY + 42,
                    79,
                    16,
                    condition.isEnabled()
                            ? Component.translatable("highlight.enabled").withStyle(ChatFormatting.GREEN)
                            : Component.translatable("highlight.disabled").withStyle(ChatFormatting.RED),
                    button -> {
                        condition.setEnabled(!condition.isEnabled());
                        button.setMessage(condition.isEnabled()
                                ? Component.translatable("highlight.enabled").withStyle(ChatFormatting.GREEN)
                                : Component.translatable("highlight.disabled").withStyle(ChatFormatting.RED));
                        try {
                            HighlightConditionManager.save(condition);
                        } catch (IOException e) {
                            SystemToast.add(this.minecraft.getToastManager(), SystemToast.SystemToastId.LOW_DISK_SPACE,
                                    Component.translatable("highlight.disable_update_file.fail"),
                                    Component.translatable("highlight.disable_update_file.fail.msg"));
                        }
                    },
                    t -> {
                        return condition.isEnabled()
                                ? Component.translatable("highlight.disable").withStyle(ChatFormatting.RED)
                                : Component.translatable("highlight.enable").withStyle(ChatFormatting.GREEN);
                    }
            ));

// Delete button:
            card.addConnectedWidget(new GreyButton(
                    startX + 183,
                    cardY + 42,
                    79,
                    16,
                    Component.translatable("config.delete_button"),
                    button -> {
                        boolean success = HighlightConditionManager.delete(card.getCondition());
                        if (!success) {
                            SystemToast.add(this.minecraft.getToastManager(), SystemToast.SystemToastId.LOW_DISK_SPACE,
                                    Component.translatable("highlight.delete.fail"),
                                    Component.translatable("highlight.delete.fail.msg"));
                            return;
                        }
                        this.minecraft.setScreen(new SIHConfigScreen(this.parent));
                    },
                    (t) -> {
                        return Component.translatable("config.delete_description");
                    }
            ));

            this.cards.add(card);
            index++;
        }

        // --- Side gap buttons and title ---
        int margin = 10;
        this.sideX = startX + cardWidth + margin;
        this.sidebarButtonWidth = this.width - sideX - margin;
        int buttonWidth = this.sidebarButtonWidth;

        int buttonHeight = 20;
        int bottomSpacing = 5;

        this.texSize = Math.min(64, buttonWidth);

        int topButtonsY = this.texSize + margin;

        this.addRenderableWidget(new GreyButton(
                sideX,
                topButtonsY,
                buttonWidth,
                buttonHeight,
                Component.translatable("config.create_button"),
                button -> {
                    PlainItemCondition newCondition = new PlainItemCondition(
                            false,
                            PulseSlotOutlineRenderFunction.ID,
                            new TagList<>(List.of(
                                    TagList.ofTag(ItemTags.PLANKS),
                                    TagList.ofObj(Items.BEDROCK, BuiltInRegistries.ITEM)
                            ))
                    );
                    newCondition.setFileID("plain_condition_" + System.currentTimeMillis());
                    newCondition.setEnabled(true);
                    try {
                        HighlightConditionManager.save(newCondition);
                    } catch (IOException e) {
                        SystemToast.add(this.minecraft.getToastManager(), SystemToast.SystemToastId.LOW_DISK_SPACE,
                                Component.translatable("config.create_fail_1"),
                                Component.translatable("config.create_fail_2"));
                        return;
                    }
                    this.minecraft.setScreen(new SIHConfigScreen(this.parent));
                },
                (t) -> {
                    return Component.translatable("config.create_description");
                }
        ));

        this.addRenderableWidget(new GreyButton(
                sideX,
                topButtonsY,
                buttonWidth,
                buttonHeight,
                Component.translatable("config.refresh_button"),
                button -> {
                    this.cards.forEach(ConditionCardWidget::randomizeStack);
                },
                (t) -> Component.translatable("config.refresh_description")
        ));

        // Done button:
        this.addRenderableWidget(new GreyButton(
                sideX,
                this.height - 30,
                buttonWidth,
                buttonHeight,
                Component.translatable("config.done_button"),
                button -> {
                    this.minecraft.setScreen(parent);
                },
                (t) -> {
                    return Component.translatable("config.done_description");
                }
        ));

        // Discord button:
        this.addRenderableWidget(new GreyButton(
                sideX,
                this.height - 55,
                buttonWidth,
                buttonHeight,
                Component.translatable("config.discord_button"),
                button -> {
                    Util.getPlatform().openUri("https://discord.imb11.dev/");
                },
                (t) -> {
                    return Component.translatable("config.discord_description");
                }
        ));

        // Ko-Fi button:
        this.addRenderableWidget(new GreyButton(
                sideX,
                this.height - 80,
                buttonWidth,
                buttonHeight,
                Component.translatable("config.kofi_button"),
                button -> {
                    Util.getPlatform().openUri("https://ko-fi.com/imb11");
                },
                (t) -> {
                    return Component.translatable("config.kofi_description");
                }
        ));

        for (ConditionCardWidget card : this.cards) {
            this.addRenderableWidget(card);
            card.getConnectedWidgets().forEach(this::addRenderableWidget);
        }
    }

    public float timeSinceLastSupporter = -1;
    public int supporterIndex = -1;

    public float scrollOffset = 0;
    public float targetScrollOffset = 0;
    public float maxScroll = 0;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        float cardSpacingY = 75.0f;
        float startY = 10.0f;
        float visibleArea = this.height - this.font.lineHeight - 2 - 15 - startY;
        float totalHeight = startY + cards.size() * cardSpacingY;
        maxScroll = Math.max(0, totalHeight - visibleArea);
        targetScrollOffset = Mth.clamp(targetScrollOffset - (float) scrollY * 20, 0, maxScroll);

        cards.forEach(card -> {
            int newY = card.getInitialY() - (int) targetScrollOffset;
            card.setY(newY);
        });
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);

        for (Renderable renderable : ((ScreenAccessorMixin) this).getRenderables()) {
            if (cards.contains(renderable) || !cards.stream().filter(card -> card.getConnectedWidgets().contains(renderable)).toList().isEmpty()) {
                continue;
            }
            renderable.render(graphics, mouseX, mouseY, delta);
        }

        if (timeSinceLastSupporter == -1) {
            timeSinceLastSupporter = Util.getMillis();
            supporterIndex = 0;
        }

        if (timeSinceLastSupporter + 2000 < Util.getMillis()) {
            supporterIndex++;
            if (supporterIndex >= SmartItemHighlight.SUPPORTERS.length) {
                supporterIndex = 0;
            }
            timeSinceLastSupporter = Util.getMillis();
        }

        String supporter = SmartItemHighlight.SUPPORTERS[supporterIndex];

        float timePassed = (Util.getMillis() - timeSinceLastSupporter) / 2000f;
        float fadeBetween = (Mth.sin(timePassed * (float) Math.PI) + 1) / 2;
        if (fadeBetween < 0.01) {
            fadeBetween = 0;
        }
        int alpha = (int) (fadeBetween * 255.0f);
        int color = (alpha << 24) | (0xFFFFFF);
        Component text = Component.literal(supporter).withStyle(ChatFormatting.GOLD).append(Component.translatable("config.supporter_message").withStyle(ChatFormatting.WHITE));
        int spaceBetween = (this.width / 2 - 40);
        int textTotalHeight = font.wordWrapHeight(text, spaceBetween - 20);
        int targetY = this.height - this.font.lineHeight - 2;
        int textY = targetY - (textTotalHeight / 2);

        Utils.drawTextWrapped(graphics, font, text, 5, textY, this.sideX - 10, color);

        // Render the mod icon
        ResourceLocation modIcon = SmartItemHighlight.loc("icon.png");
        // render it centered if texSize = 64 but the sidebar button width is greater than 64
        int iconX = this.sidebarButtonWidth > 64 ? this.sidebarButtonWidth / 2 - 32 : 0;
        Utils.renderTexture(graphics, modIcon, sideX + iconX, 5, this.texSize, this.texSize);
        graphics.renderOutline(sideX + iconX, 5, this.texSize, this.texSize, 0x7FFFFFFF);

        graphics.enableScissor(0, 0, this.sideX, this.height - this.font.lineHeight - 2 - 15);
        for (ConditionCardWidget card : cards) {
            card.render(graphics, mouseX, mouseY, delta);
            card.getConnectedWidgets().forEach(widget -> widget.render(graphics, mouseX, mouseY, delta));
        }
        graphics.disableScissor();

        // Scrollbar
        float startY = 10.0f;
        float visibleArea = this.height - this.font.lineHeight - 2 - 15 - startY;
        float totalHeight = startY + cards.size() * 75.0f;
        if (maxScroll > 0) {
            int scrollbarWidth = 6;
            int scrollbarHeight = (int) (visibleArea / totalHeight * visibleArea);
            int scrollbarY = (int) (targetScrollOffset / maxScroll * (visibleArea - scrollbarHeight));
            graphics.fill(this.sideX - scrollbarWidth, (int) (startY + scrollbarY), this.sideX - 5, (int) (startY + scrollbarY + scrollbarHeight), 0x5FFFFFFF);
        }
    }
}