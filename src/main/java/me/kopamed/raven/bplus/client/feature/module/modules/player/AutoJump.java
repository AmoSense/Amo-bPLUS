package me.kopamed.raven.bplus.client.feature.module.modules.player;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoJump extends Module {
   public static BooleanSetting b;
   private boolean c = false;

   public AutoJump() {
      super("AutoJump", ModuleCategory.Player, 0);
      this.registerSetting(b = new BooleanSetting("Cancel when shifting", true));
   }

   public void onDisable() {
      this.ju(this.c = false);
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if (Utils.Player.isPlayerInGame()) {
         if (mc.thePlayer.onGround && (!b.isToggled() || !mc.thePlayer.isSneaking())) {
            if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX / 3.0D, -1.0D, mc.thePlayer.motionZ / 3.0D)).isEmpty()) {
               this.ju(this.c = true);
            } else if (this.c) {
               this.ju(this.c = false);
            }
         } else if (this.c) {
            this.ju(this.c = false);
         }

      }
   }

   private void ju(boolean ju) {
      KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), ju);
   }
}
