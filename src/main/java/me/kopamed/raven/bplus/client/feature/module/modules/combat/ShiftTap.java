package me.kopamed.raven.bplus.client.feature.module.modules.combat;

import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.RangeSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import me.kopamed.raven.bplus.helper.utils.CoolDown;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.module.modules.world.AntiBot;
import me.kopamed.raven.bplus.client.feature.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class ShiftTap extends Module {
    public static NumberSetting range, eventType, chance;
    public static DescriptionSetting eventTypeDesc;
    public static BooleanSetting onlyPlayers;
    public static RangeSetting actionTicks, onceEvery, postDelay;
    public static boolean comboing, hitCoolDown, alreadyHit, waitingForPostDelay;
    public static int hitTimeout, hitsWaited;
    public static CoolDown actionTimer = new CoolDown(0), postDelayTimer = new CoolDown(0);

    public ShiftTap(){
        super("ShiftTap", ModuleCategory.Combat, 0);
        this.registerSetting(onlyPlayers = new BooleanSetting("Only combo players", true));
        this.registerSetting(actionTicks = new RangeSetting("Action Time (MS)",  25, 55, 1, 500, 1));
        this.registerSetting(onceEvery =  new RangeSetting("Once every ... hits", 1, 1, 1, 10, 1));
        this.registerSetting(postDelay =  new RangeSetting("Post delay (MS)", 25, 55, 1, 500, 1));
        this.registerSetting(chance =  new NumberSetting("Chance %", 100, 0, 100, 1));
        this.registerSetting(range = new NumberSetting("Range: ", 3, 1, 6, 0.05));
        this.registerSetting(eventType = new NumberSetting("Value: ", 2, 1, 2, 1));
        this.registerSetting(eventTypeDesc = new DescriptionSetting("Mode: POST"));
    }


    public void guiUpdate() {
        eventTypeDesc.setDesc(Utils.md + Utils.Modes.SprintResetTimings.values()[(int) eventType.getInput() - 1]);
    }


    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if(!Utils.Player.isPlayerInGame())
            return;

        if(waitingForPostDelay){
            if(postDelayTimer.hasTimeElapsed()){
                waitingForPostDelay = false;
                comboing = true;
                startCombo();
                actionTimer.start();
            }
            return;
        }

        if(comboing) {
            if(actionTimer.hasTimeElapsed()){
                comboing = false;
                finishCombo();
                return;
            }else {
                return;
            }
        }



        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof Entity && Mouse.isButtonDown(0)) {
            Entity target = mc.objectMouseOver.entityHit;
            //////////System.out.println(target.hurtResistantTime);
            if(target.isDead) {
                return;
            }

            if (mc.thePlayer.getDistanceToEntity(target) <= range.getInput()) {
                if ((target.hurtResistantTime >= 10 && Utils.Modes.SprintResetTimings.values()[(int) eventType.getInput() - 1] == Utils.Modes.SprintResetTimings.POST) || (target.hurtResistantTime <= 10 && Utils.Modes.SprintResetTimings.values()[(int) eventType.getInput() - 1] == Utils.Modes.SprintResetTimings.PRE)) {

                    if (onlyPlayers.isToggled()){
                        if (!(target instanceof EntityPlayer)){
                            return;
                        }
                    }

                    if(AntiBot.bot(target)){
                        return;
                    }


                    if (hitCoolDown && !alreadyHit) {
                        //////////System.out.println("coolDownCheck");
                        hitsWaited++;
                        if(hitsWaited >= hitTimeout){
                            //////////System.out.println("hiit cool down reached");
                            hitCoolDown = false;
                            hitsWaited = 0;
                        } else {
                            //////////System.out.println("still waiting for cooldown");
                            alreadyHit = true;
                            return;
                        }
                    }

                    //////////System.out.println("Continued");

                    if(!(chance.getInput() == 100 || Math.random() <= chance.getInput() / 100))
                        return;

                    if(!alreadyHit){
                        //////////System.out.println("Startring combo code");
                        guiUpdate();
                        if(onceEvery.getInputMin() == onceEvery.getInputMax()) {
                            hitTimeout =  (int)onceEvery.getInputMin();
                        } else {

                            hitTimeout = ThreadLocalRandom.current().nextInt((int)onceEvery.getInputMin(), (int)onceEvery.getInputMax());
                        }
                        hitCoolDown = true;
                        hitsWaited = 0;

                        actionTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(actionTicks.getInputMin(),  actionTicks.getInputMax()+0.01));

                        if(postDelay.getInputMax() != 0){
                            postDelayTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(postDelay.getInputMin(),  postDelay.getInputMax()+0.01));
                            postDelayTimer.start();
                            waitingForPostDelay = true;
                        } else {
                            comboing = true;
                            startCombo();
                            actionTimer.start();
                            //////////System.out.println("Combo started");
                            alreadyHit = true;
                        }

                        //////////System.out.println("Combo started");
                        alreadyHit = true;
                    }
                } else {
                    if(alreadyHit){
                        //////////System.out.println("UnHit");
                    }
                    alreadyHit = false;
                    //////////System.out.println("REEEEEEE");
                }
            }
        }
    }

    private static void finishCombo() {
        if(!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()))
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
    }

    private static void startCombo() {
        if(Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            KeyBinding.onTick(mc.gameSettings.keyBindSneak.getKeyCode());
        }
    }
}
