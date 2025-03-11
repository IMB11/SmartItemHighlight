package dev.imb11.smartitemhighlight.config;

import dev.imb11.smartitemhighlight.api.TagList;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class ConditionCardWidget extends AbstractWidget {
    private final int fakeWidth;
    private final int fakeHeight;
    private final ItemStack randomStack;
    private final RenderFunction renderFunction;

    public HighlightCondition getCondition() {
        return condition;
    }

    public ArrayList<AbstractWidget> getConnectedWidgets() {
        return connectedWidgets;
    }

    private final HighlightCondition condition;
    private final ArrayList<AbstractWidget> connectedWidgets;

    public ConditionCardWidget(int x, int y, HighlightCondition condition) {
        super(x, y, 1, 1, Component.empty());
        this.condition = condition;
        this.connectedWidgets = new ArrayList<>();
        this.fakeWidth = 332;
        this.fakeHeight = 65;
        this.randomStack = new ItemStack(BuiltInRegistries.ITEM.getRandom(Minecraft.getInstance().font.random).get());
        this.renderFunction = RenderFunction.RENDER_FUNCTION_REGISTRY.getOrDefault(this.condition.getRenderFunction(), RenderFunction.NONE);
    }

    public void addConnectedWidget(AbstractWidget widget) {
        this.connectedWidgets.add(widget);
    }

    @Override
    public void setX(int x) {
        int currentX = getX();
        for (AbstractWidget connectedWidget : connectedWidgets) {
            int offset = connectedWidget.getX() - currentX;
            connectedWidget.setX(x + offset);
        }
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        int currentY = getY();
        for (AbstractWidget connectedWidget : connectedWidgets) {
            int offset = connectedWidget.getY() - currentY;
            connectedWidget.setY(y + offset);
        }
        super.setY(y);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        var font = Minecraft.getInstance().font;
        graphics.fill(this.getX(), this.getY(), this.getX() + this.fakeWidth, this.getY() + this.fakeHeight, 0x7F000000);
        graphics.renderOutline(this.getX(), this.getY(), this.fakeWidth, this.fakeHeight, 0x7F000000);
        String name = condition.getFileID() + ".json";
        graphics.drawString(font, name, this.getX() + 13, this.getY() + 9, 0xFFFFFF, false);
        String meta = condition.getSerializationId().toString();
        graphics.drawString(font, meta, this.getX() + 13, this.getY() + 9 + font.lineHeight + 2, 0x999999, false);

        // Render preview slot at startX + 267, cardY + 5, 57, 53
        ResourceLocation inventoryTexture = InventoryScreen.INVENTORY_LOCATION;
        int u = 7, v = 83;
        int uW = 18, uH = 18;
        int x = this.getX() + 280 - 9;
        int y = this.getY() + 5;
        int w = 18, h = 18;
        graphics.pose().pushPose();
        float sf = 54f / 18f;
        graphics.pose().scale(sf, sf, 1f);
        //? if 1.21 {
        /*graphics.blitSprite( inventoryTexture, (int) (x / sf), (int) (y / sf), u, v, uW, uH, 18, 18, 256, 256, 0xFFFFFFFF);
        *///?} else {
        graphics.blit(RenderType::guiTexturedOverlay, inventoryTexture, (int) (x / sf), (int) (y / sf), u, v, uW, uH, 18, 18, 256, 256, 0xFFFFFFFF);
        //?}
        graphics.renderItem(this.randomStack, (int) ((x + 2) / (sf)), (int) ((y + 3) / (sf)), 0, 25);
        graphics.pose().translate(0, 0, 500);
        renderFunction.render(this.condition, this.randomStack, 0, graphics, (int) ((x + 2) / sf), (int) ((y + 3) / sf), 25);
        graphics.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}