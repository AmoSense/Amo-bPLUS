package me.kopamed.raven.bplus.client.feature.module.modules.blatant;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.manager.ModuleManager;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;
import net.minecraft.client.settings.KeyBinding;

public class BHop extends Module {
   public static NumberSetting a;
   private final double bspd = 0.0025D;

   public BHop() {
      super("Bhop", "Allows you to BunnyHop around", ModuleCategory.Blatant);
      this.registerSetting(a = new NumberSetting("Speed", 2.0D, 1.0D, 15.0D, 0.2D));
   }

   public void update() {
      if (!ModuleManager.fly.isToggled() && Utils.Player.isMoving() && !mc.thePlayer.isInWater()) {
         KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
         mc.thePlayer.noClip = true;
         if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
         }

         mc.thePlayer.setSprinting(true);
         double spd = 0.0025D * a.getInput();
         double m = (float)(Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + spd);
         Utils.Player.bop(m);
      }
   }
}
