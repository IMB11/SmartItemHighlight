package dev.imb11.smartitemhighlight.api.render.builtin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import dev.imb11.smartitemhighlight.api.condition.HighlightCondition;
import dev.imb11.smartitemhighlight.api.render.RenderFunction;
import dev.imb11.smartitemhighlight.mixin_io.GuiGraphicsAccessor;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;

import java.util.List;

public class OutlineRenderFunction implements RenderFunction {
    public static final ResourceLocation ID = SmartItemHighlight.loc("outline");

    private final static List<Pair<Integer, Integer>> offsets = List.of(Pair.of(1, 0), Pair.of(-1, 0), Pair.of(0, 1), Pair.of(0, -1));

    @Override
    public void render(HighlightCondition condition, Level world, LivingEntity livingEntity, ItemStack stack, int seed, GuiGraphics drawContext, int x, int y, int z) {
        Minecraft minecraft = ((GuiGraphicsAccessor) drawContext).SIH$getClient();

        //? if 1.20 {
        /*BakedModel model = minecraft.getItemRenderer().getModel(stack, world, livingEntity, seed);
         *///?} else {
        // None finished, I'm having trouble finding the baked model in 1.21.4
        ResourceLocation resourceLocation = stack.get(DataComponents.ITEM_MODEL);
        minecraft.getItemRenderer().
        BakedModel model = minecraft.getModelManager().getModel(new ModelResourceLocation(resourceLocation,"inventory"));
        //?}


        SpriteContents sprite = model.getParticleIcon().contents();

        drawContext.fill(x, y, x, y, 0xFF00FF00);

        for (int sx = 0; sx < sprite.width(); sx++)
            for (int sy = 0; sy < sprite.height(); sy++)
                if (sprite.isTransparent(0, sx, sy))
                    for (Pair<Integer, Integer> offset : offsets)
                        if (sx + offset.first() >= 0 && sy + offset.second() >= 0 && sx + offset.first() < sprite.width() && sy + offset.second() < sprite.height() && !sprite.isTransparent(0, sx + offset.first(), sy + offset.second())) {
                            drawContext.fill(x + sx, y + sy, x + sx + 1, y + sy + 1, z, 0xFFFF0000);
                            break;
                        }
    }
}
