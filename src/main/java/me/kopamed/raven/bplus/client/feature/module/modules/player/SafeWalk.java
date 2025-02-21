package me.kopamed.raven.bplus.client.feature.module.modules.player;

import me.kopamed.raven.bplus.client.Raven;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.RangeSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import me.kopamed.raven.bplus.helper.utils.CoolDown;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.module.modules.client.SelfDestruct;
import me.kopamed.raven.bplus.client.feature.module.Module;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class SafeWalk extends Module {
   public static BooleanSetting doShift;
   public static BooleanSetting blocksOnly;
   public static BooleanSetting shiftOnJump;
   public static BooleanSetting onHold;
   public static BooleanSetting showBlockAmount;
   public static BooleanSetting lookDown;
   public static RangeSetting pitchRange;
   public static NumberSetting blockShowMode;
   public static DescriptionSetting blockShowModeDesc;
   public static RangeSetting shiftTime;

   private static boolean shouldBridge = false;
   private static boolean isShifting = false;
   private boolean allowedShift;
   private final CoolDown shiftTimer = new CoolDown(0);

   public SafeWalk() {
      super("SafeWalk", ModuleCategory.Player, 0);
      this.registerSetting(doShift = new BooleanSetting("Shift", false));
      this.registerSetting(shiftOnJump = new BooleanSetting("Shift during jumps", false));
      this.registerSetting(shiftTime = new RangeSetting("Shift time: (s)", 10, 20, 0, 500, 1));
      this.registerSetting(onHold = new BooleanSetting("On shift hold", false));
      this.registerSetting(blocksOnly = new BooleanSetting("Blocks only", true));
      this.registerSetting(showBlockAmount = new BooleanSetting("Show amount of blocks", true));
      this.registerSetting(blockShowMode = new NumberSetting("Block display info:", 2D, 1D, 2D, 1D));
      this.registerSetting(blockShowModeDesc = new DescriptionSetting("Mode: "));
      this.registerSetting(lookDown = new BooleanSetting("Only when looking down", true));
      this.registerSetting(pitchRange = new RangeSetting("Pitch min range:", 70D, 85, 0D, 90D, 1D));
   }

   public void onDisable() {
      if (doShift.isToggled() && Utils.Player.playerOverAir()) {
         this.setShift(false);
      }

      shouldBridge = false;
      isShifting = false;
   }

   public void guiUpdate() {
      blockShowModeDesc.setDesc(Utils.md + BlockAmountInfo.values()[(int)blockShowMode.getInput() - 1]);
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if(!Utils.Client.currentScreenMinecraft())
         return;

      if (!Utils.Player.isPlayerInGame()) {
         return;
      }

      boolean shiftTimeSettingActive = shiftTime.getInputMax() > 0;

      if(doShift.isToggled()) {
         if(lookDown.isToggled()) {
            if(mc.thePlayer.rotationPitch < pitchRange.getInputMin() || mc.thePlayer.rotationPitch > pitchRange.getInputMax()) {
               shouldBridge = false;
               if(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                  setShift(true);
               }
               return;
            }
         }
         if (onHold.isToggled()) {
            if  (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
               shouldBridge = false;
               return;
            }
         }

         if (blocksOnly.isToggled()) {
            ItemStack i = mc.thePlayer.getHeldItem();
            if (i == null || !(i.getItem() instanceof ItemBlock)) {
               if (isShifting) {
                  isShifting = false;
                  this.setShift(false);
               }

               return;
            }
         }

         if (mc.thePlayer.onGround) {
            if (Utils.Player.playerOverAir()) {
               // code fo the timer
               if(shiftTimeSettingActive){ // making sure that the player has set the value so some number
                  shiftTimer.setCooldown(Utils.Java.randomInt(shiftTime.getInputMin(), shiftTime.getInputMax() + 0.1));
                  shiftTimer.start();
               }

               isShifting = true;
               this.setShift(true);
               shouldBridge = true;
            }
            else if (mc.thePlayer.isSneaking() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && onHold.isToggled()) { // if player is smeaking and shiftDown and holdSetting turned on
               isShifting = false;
               shouldBridge = false;
               this.setShift(false);
            }
            else if(onHold.isToggled() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) { // if shiftDown and holdSetting turned on
               isShifting = false;
               shouldBridge = false;
               this.setShift(false);
            }
            else if(mc.thePlayer.isSneaking() && (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && onHold.isToggled()) && (!shiftTimeSettingActive|| shiftTimer.hasTimeElapsed())) {
               isShifting = false;
               this.setShift(false);
               shouldBridge = true;
            }
            else if(mc.thePlayer.isSneaking() && !onHold.isToggled()  && (!shiftTimeSettingActive|| shiftTimer.hasTimeElapsed())) {
               isShifting = false;
               this.setShift(false);
               shouldBridge = true;
            }
         }

         else if (shouldBridge && mc.thePlayer.capabilities.isFlying) {
            this.setShift(false);
            shouldBridge = false;
         }

         else if (shouldBridge && Utils.Player.playerOverAir() && shiftOnJump.isToggled()) {
            isShifting = true;
            this.setShift(true);
         } else {
            // rn we are in the air and we are not flying, meaning that we are in a jump. and since shiftOnJump is turned off, we just unshift and uhh... nyoooom
            isShifting = false;
            this.setShift(false);
         }
      }
   }

   @SubscribeEvent
   public void r(TickEvent.RenderTickEvent e) {
      if(!showBlockAmount.isToggled() || !Utils.Player.isPlayerInGame()) return;
      if (e.phase == TickEvent.Phase.END && !SelfDestruct.destructed) {
         if (mc.currentScreen == null) {
            if(shouldBridge) {
               ScaledResolution res = new ScaledResolution(mc);

               int totalBlocks = 0;
               if(BlockAmountInfo.values()[(int)blockShowMode.getInput() - 1] == BlockAmountInfo.BLOCKS_IN_CURRENT_STACK) {
                  totalBlocks = Utils.Player.getBlockAmountInCurrentStack(mc.thePlayer.inventory.currentItem);
               } else {
                  for (int slot = 0; slot < 36; slot++){
                     totalBlocks += Utils.Player.getBlockAmountInCurrentStack(slot);
                  }
               }

               if(totalBlocks <= 0){
                  return;
               }

               int rgb;
               if (totalBlocks < 16.0D) {
                  rgb = Color.red.getRGB();
               } else if (totalBlocks < 32.0D) {
                  rgb = Color.orange.getRGB();
               } else if (totalBlocks < 128.0D) {
                  rgb = Color.yellow.getRGB();
               } else if (totalBlocks > 128.0D) {
                  rgb = Color.green.getRGB();
               } else {
                  rgb = Color.black.getRGB();
               }

               String t = totalBlocks + " blocks";
               int x = res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(t) / 2;
               int y;
               if(Raven.client.getDebugManager().isDebugging()) {
                  y = res.getScaledHeight() / 2 + 17 + mc.fontRendererObj.FONT_HEIGHT;
               } else {
                  y = res.getScaledHeight() / 2 + 15;
               }
               mc.fontRendererObj.drawString(t, (float)x, (float)y, rgb, false);
            }
         }
      }
   }

   private void setShift(boolean sh) {
      KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), sh);
   }

   public enum BlockAmountInfo {
      BLOCKS_IN_TOTAL,
      BLOCKS_IN_CURRENT_STACK
   }
}
