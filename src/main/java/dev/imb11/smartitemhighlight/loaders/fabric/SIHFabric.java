//? if fabric {
package dev.imb11.smartitemhighlight.loaders.fabric;

import dev.imb11.smartitemhighlight.Main;
import net.fabricmc.api.ClientModInitializer;

public class SIHFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Main.initialize(Main.loaders.FABRIC);
    }
}
//?}