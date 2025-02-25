//? if neoforge {
/*package dev.imb11.smartitemhighlight.loaders.neoforge;

import dev.imb11.smartitemhighlight.Main;
import dev.imb11.smartitemhighlight.ModCommands;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod("smartitemhighlight")
public class SIHNeoForge {
    public SIHNeoForge() {
        Main.initialize();

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}
*///?}
