package me.kopamed.raven.bplus.client.feature.module.modules.movement;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;

public class VClip extends Module {
   public static NumberSetting a;

   public VClip() {
      super("VClip", ModuleCategory.Movement, 0);
      this.registerSetting(a = new NumberSetting("Distace", 2.0D, -10.0D, 10.0D, 0.5D));
   }

   public void onEnable() {
      if (a.getInput() != 0.0D) {
         mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + a.getInput(), mc.thePlayer.posZ);
      }

      this.disable();
   }
}
