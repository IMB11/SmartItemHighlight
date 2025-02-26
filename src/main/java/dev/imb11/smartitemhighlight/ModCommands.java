package dev.imb11.smartitemhighlight;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Leaving all this for now not because it's currently useful, but because we might want to add commands later and there's no reason to have to resetup this stuff.
        dispatcher.register(Commands.literal("highlight")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ItemStack stack = player.getMainHandItem();
                    if (stack.isEmpty()) stack = player.getOffhandItem();
                    if (stack.isEmpty()) {
                        context.getSource().sendFailure(Component.literal("You must be holding an item to mark it for highlighting"));
                        return 0;
                    }
                    if (SmartItemHighlight.highlightedItems.contains(stack.getItem().getDescriptionId())) {
                        context.getSource().sendFailure(Component.literal("This item is already being highlighted"));
                        return 0;
                    }
                    SmartItemHighlight.highlightedItems.add(stack.getItem().getDescriptionId());
                    context.getSource().sendSuccess(() -> Component.literal("Highlight added"), false);
                    return 1;
                }));
        dispatcher.register(Commands.literal("unhighlight")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ItemStack stack = player.getMainHandItem();
                    if (stack.isEmpty()) stack = player.getOffhandItem();
                    if (stack.isEmpty()) {
                        context.getSource().sendFailure(Component.literal("You must be holding an item to mark it for no highlighting"));
                        return 0;
                    }
                    if (!SmartItemHighlight.highlightedItems.contains(stack.getItem().getDescriptionId())) {
                        context.getSource().sendFailure(Component.literal("This item isn't being highlighted"));
                        return 0;
                    }
                    SmartItemHighlight.highlightedItems.remove(stack.getItem().getDescriptionId());
                    context.getSource().sendSuccess(() -> Component.literal("Highlight removed"), false);
                    return 1;
                }));
    }
}
