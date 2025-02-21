package me.kopamed.raven.bplus.client.feature.module.modules.world;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;

public class PingSpoof extends Module {
    public static NumberSetting spoof;
    public static boolean toggled = false;

    public PingSpoof(){
        super("PingSpoof", ModuleCategory.World, 0);
        this.registerSetting(spoof = new NumberSetting("Increase ping by", 300, 1, 100000, 10));
    }

    @Override
    public void onEnable() {
        toggled = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        toggled = false;
        super.onDisable();
    }
}
