package me.kopamed.raven.bplus.client.feature.module.modules.combat;

import java.lang.reflect.Field;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class DelayRemover extends Module {
   public static DescriptionSetting a;
   private Field l = null;

   public DelayRemover() {
      super("Delay Remover", ModuleCategory.Combat, 0);
      this.registerSetting(a = new DescriptionSetting("Gives you 1.7 hitreg."));
   }

   public void onEnable() {
      try {
         this.l = Minecraft.class.getDeclaredField("field_71429_W");
      } catch (Exception var4) {
         try {
            this.l = Minecraft.class.getDeclaredField("leftClickCounter");
         } catch (Exception var3) {
         }
      }

      if (this.l != null) {
         this.l.setAccessible(true);
      } else {
         this.disable();
      }

   }

   @SubscribeEvent
   public void a(PlayerTickEvent b) {
      if (Utils.Player.isPlayerInGame() && this.l != null) {
         if (!mc.inGameHasFocus || mc.thePlayer.capabilities.isCreativeMode) {
            return;
         }

         try {
            this.l.set(mc, 0);
         } catch (IllegalAccessException | IndexOutOfBoundsException var3) {
         }
      }

   }
}
