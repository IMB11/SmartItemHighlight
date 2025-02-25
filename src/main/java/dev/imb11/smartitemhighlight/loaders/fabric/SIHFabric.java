//? if fabric {
package dev.imb11.smartitemhighlight.loaders.fabric;

import dev.imb11.smartitemhighlight.SmartItemHighlight;
import net.fabricmc.api.ClientModInitializer;

public class SIHFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SmartItemHighlight.initialize();
    }
}
//?}