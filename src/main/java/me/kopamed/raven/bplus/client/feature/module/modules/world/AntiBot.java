package me.kopamed.raven.bplus.client.feature.module.modules.world;

import java.util.HashMap;

import me.kopamed.raven.bplus.client.Raven;
import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.manager.ModuleManager;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import me.kopamed.raven.bplus.client.feature.module.modules.player.Freecam;
import me.kopamed.raven.bplus.helper.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiBot extends Module {
   private static final HashMap<EntityPlayer, Long> newEnt = new HashMap<>();
   private final long ms = 4000L;
   public static BooleanSetting a;

   public AntiBot() {
      super("AntiBot", "Stops modules from targeting bots", ModuleCategory.World, true);
      this.registerSetting(a = new BooleanSetting("Wait 80 ticks", false));
   }

   public void onDisable() {
      newEnt.clear();
   }

   @SubscribeEvent
   public void onEntityJoinWorld(EntityJoinWorldEvent event) {
      if(!Utils.Player.isPlayerInGame()) return;
      if (a.isToggled() && event.entity instanceof EntityPlayer && event.entity != mc.thePlayer) {
         newEnt.put((EntityPlayer)event.entity, System.currentTimeMillis());
      }
   }

   public void update() {
      if (a.isToggled() && !newEnt.isEmpty()) {
         long now = System.currentTimeMillis();
         newEnt.values().removeIf((e) -> {
            return e < now - 4000L;
         });
      }

   }

   public static boolean bot(Entity en) {
      if(!Utils.Player.isPlayerInGame() || mc.currentScreen != null) return false;
      if (Freecam.en != null && Freecam.en == en) {
         return true;
      } else if (ModuleManager.antiBot.isToggled()) {
         return false;
      } else if (!Utils.Client.isHyp()) {
         return false;
      } else if (a.isToggled() && !newEnt.isEmpty() && newEnt.containsKey(en)) {
         return true;
      } else if (en.getName().startsWith("§c")) {
         return true;
      } else {
         String n = en.getDisplayName().getUnformattedText();
         if (n.contains("§")) {
            return n.contains("[NPC] ");
         } else {
            if (n.isEmpty() && en.getName().isEmpty()) {
               return true;
            }

            if (n.length() == 10) {
               int num = 0;
               int let = 0;
               char[] var4 = n.toCharArray();

               for (char c : var4) {
                  if (Character.isLetter(c)) {
                     if (Character.isUpperCase(c)) {
                        return false;
                     }

                     ++let;
                  } else {
                     if (!Character.isDigit(c)) {
                        return false;
                     }

                     ++num;
                  }
               }

               return num >= 2 && let >= 2;
            }
         }

         return false;
      }
   }
}
