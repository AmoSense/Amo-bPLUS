package me.kopamed.raven.bplus.client.feature.module.modules.render;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;

public class CustomFOV extends Module {
    public static NumberSetting FOV;

    public CustomFOV(){
        super("CustomFOV", ModuleCategory.Render, 0);
        this.registerSetting(FOV = new NumberSetting("FOV:", 180, 0, 420, 1));
    }


    public void update() {
        mc.gameSettings.fovSetting = (int)FOV.getInput();
    }
}
