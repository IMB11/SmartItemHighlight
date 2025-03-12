package dev.imb11.smartitemhighlight.loaders.fabric;

//? if fabric {

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.imb11.smartitemhighlight.config.SIHConfigScreen;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new SIHConfigScreen(parent);
    }
}
//?}