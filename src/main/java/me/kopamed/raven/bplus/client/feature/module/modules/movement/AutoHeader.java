package me.kopamed.raven.bplus.client.feature.module.modules.movement;

import io.netty.util.internal.ThreadLocalRandom;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class AutoHeader extends Module {
    public static DescriptionSetting desc;
    public static BooleanSetting cancelDuringShift, onlyWhenHoldingSpacebar;
    public static NumberSetting pbs;
    private double startWait;

    public AutoHeader() {
        super("AutoHeadHitter", ModuleCategory.Movement, 0);
        this.registerSetting(desc = new DescriptionSetting("Spams spacebar when under blocks"));
        this.registerSetting(cancelDuringShift = new BooleanSetting("Cancel if snkeaing", true));
        this.registerSetting(onlyWhenHoldingSpacebar = new BooleanSetting("Only when holding jump", true));
        this.registerSetting(pbs = new NumberSetting("Jump Presses per second", 12, 1, 20, 1));

        boolean jumping = false;
    }

    @Override
    public void onEnable(){
        startWait = System.currentTimeMillis();
        super.onEnable();
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if (!Utils.Player.isPlayerInGame() || mc.currentScreen != null)
            return;
        if (cancelDuringShift.isToggled() && mc.thePlayer.isSneaking())
            return;

        if(onlyWhenHoldingSpacebar.isToggled()){
            if(!Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())){
                return;
            }
        }


        if (Utils.Player.playerUnderBlock() && mc.thePlayer.onGround){
            if(startWait + (1000 / ThreadLocalRandom.current().nextDouble(pbs.getInput() - 0.543543, pbs.getInput() + 1.32748923)) < System.currentTimeMillis()){
                mc.thePlayer.jump();
                startWait = System.currentTimeMillis();
            }
        }

    }
}
