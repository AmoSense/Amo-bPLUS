package me.kopamed.raven.bplus.client.feature.module.modules.player;

import java.lang.reflect.Field;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FastPlace extends Module {
   public static NumberSetting a;
   public static BooleanSetting b;
   public static Field r = null;

   public FastPlace() {
      super("FastPlace", ModuleCategory.Player, 0);
      this.registerSetting(a = new NumberSetting("Delay", 0.0D, 0.0D, 4.0D, 1.0D));
      this.registerSetting(b = new BooleanSetting("Blocks only", true));

      try {
         r = mc.getClass().getDeclaredField("field_71467_ac");
      } catch (Exception var4) {
         try {
            r = mc.getClass().getDeclaredField("rightClickDelayTimer");
         } catch (Exception var3) {
         }
      }

      if (r != null) {
         r.setAccessible(true);
      }

   }

   public void onEnable() {
      if (r == null) {
         this.disable();
      }

   }

   @SubscribeEvent
   public void a(PlayerTickEvent e) {
      if (e.phase == Phase.END) {
         if (Utils.Player.isPlayerInGame() && mc.inGameHasFocus && r != null) {
            if (b.isToggled()) {
               ItemStack item = mc.thePlayer.getHeldItem();
               if (item == null || !(item.getItem() instanceof ItemBlock)) {
                  return;
               }
            }

            try {
               int c = (int)a.getInput();
               if (c == 0) {
                  r.set(mc, 0);
               } else {
                  if (c == 4) {
                     return;
                  }

                  int d = r.getInt(mc);
                  if (d == 4) {
                     r.set(mc, c);
                  }
               }
            } catch (IllegalAccessException var4) {
            } catch (IndexOutOfBoundsException var5) {
            }
         }

      }
   }
}
