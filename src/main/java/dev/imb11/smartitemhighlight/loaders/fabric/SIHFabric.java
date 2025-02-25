//? if fabric {
package dev.imb11.smartitemhighlight.loaders.fabric;

import dev.imb11.smartitemhighlight.Main;
import dev.imb11.smartitemhighlight.ModCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class SIHFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Main.initialize();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ModCommands.register(dispatcher));
    }
}
//?}