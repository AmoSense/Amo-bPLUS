package me.kopamed.raven.bplus.client.feature.module.modules.blatant;

import java.util.TimerTask;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BedAura extends Module {
   public static NumberSetting r;
   private java.util.Timer t;
   private BlockPos m = null;
   private final long per = 600L;

   public BedAura() {
      super("BedAura", ModuleCategory.Blatant);
      this.registerSetting(r = new NumberSetting("Range", 5.0D, 2.0D, 10.0D, 1.0D));
   }

   public void onEnable() {
      (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 600L);
   }

   public void onDisable() {
      if (this.t != null) {
         this.t.cancel();
         this.t.purge();
         this.t = null;
      }

      this.m = null;
   }

   public TimerTask t() {
      return new TimerTask() {
         public void run() {
            int ra = (int)BedAura.r.getInput();

            for(int y = ra; y >= -ra; --y) {
               for(int x = -ra; x <= ra; ++x) {
                  for(int z = -ra; z <= ra; ++z) {
                     if (Utils.Player.isPlayerInGame()) {
                        BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double) x, Module.mc.thePlayer.posY + (double) y, Module.mc.thePlayer.posZ + (double) z);
                        boolean bed = Module.mc.theWorld.getBlockState(p).getBlock() == Blocks.bed;
                        if (BedAura.this.m == p) {
                           if (!bed) {
                              BedAura.this.m = null;
                           }
                        } else if (bed) {
                           BedAura.this.mi(p);
                           BedAura.this.m = p;
                           break;
                        }
                     }
                  }
               }
            }

         }
      };
   }

   private void mi(BlockPos p) {
      mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, p, EnumFacing.NORTH));
      mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, p, EnumFacing.NORTH));
   }
}
