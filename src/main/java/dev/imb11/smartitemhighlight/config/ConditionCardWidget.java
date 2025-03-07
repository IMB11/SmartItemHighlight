package dev.imb11.smartitemhighlight.config;

import dev.imb11.smartitemhighlight.api.TagList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;

import java.util.ArrayList;

public class ConditionCardWidget extends AbstractWidget {
    private final HighlightCondition condition;
    private final ArrayList<AbstractWidget> connectedWidgets;

    public ConditionCardWidget(int x, int y, HighlightCondition condition) {
        super(x, y, 332, 65, Component.empty());
        this.condition = condition;
        this.connectedWidgets = new ArrayList<>();
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
        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFFA0A0A0);
        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x19000000);
        String name = condition.getSerializationId().toString();
        graphics.drawString(font, name, this.getX() + 13, this.getY() + 9, 0xFFFFFF, false);
        String meta = condition.getClass().getSimpleName();
        graphics.drawString(font, meta, this.getX() + 13, this.getY() + 28, 0x999999, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}