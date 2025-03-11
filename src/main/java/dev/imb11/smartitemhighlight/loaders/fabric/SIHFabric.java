//? if fabric {
/*package dev.imb11.smartitemhighlight.loaders.fabric;

import dev.imb11.smartitemhighlight.ModCommands;
import dev.imb11.smartitemhighlight.SmartItemHighlight;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class SIHFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SmartItemHighlight.initialize();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ModCommands.register(dispatcher));
    }
}
*///?}