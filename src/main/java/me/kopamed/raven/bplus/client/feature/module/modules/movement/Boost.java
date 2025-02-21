package me.kopamed.raven.bplus.client.feature.module.modules.movement;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.helper.manager.ModuleManager;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;

public class Boost extends Module {
   public static DescriptionSetting c;
   public static NumberSetting a;
   public static NumberSetting b;
   private int i = 0;
   private boolean t = false;

   public Boost() {
      super("Boost", ModuleCategory.Movement, 0);
      this.registerSetting(c = new DescriptionSetting("20 ticks are in 1 second"));
      this.registerSetting(a = new NumberSetting("Multiplier", 2.0D, 1.0D, 3.0D, 0.05D));
      this.registerSetting(b = new NumberSetting("Time (ticks)", 15.0D, 1.0D, 80.0D, 1.0D));
   }

   public void onEnable() {
      if (ModuleManager.timer.isToggled()) {
         this.t = true;
         ModuleManager.timer.disable();
      }

   }

   public void onDisable() {
      this.i = 0;
      if (Utils.Client.getTimer().timerSpeed != 1.0F) {
         Utils.Client.resetTimer();
      }

      if (this.t) {
         ModuleManager.timer.enable();
      }

      this.t = false;
   }

   public void update() {
      if (this.i == 0) {
         this.i = mc.thePlayer.ticksExisted;
      }

      Utils.Client.getTimer().timerSpeed = (float)a.getInput();
      if ((double)this.i == (double)mc.thePlayer.ticksExisted - b.getInput()) {
         Utils.Client.resetTimer();
         this.disable();
      }

   }
}
