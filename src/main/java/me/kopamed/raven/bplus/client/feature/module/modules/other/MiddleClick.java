package me.kopamed.raven.bplus.client.feature.module.modules.other;

import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import me.kopamed.raven.bplus.client.feature.module.modules.combat.AimAssist;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Method;

public class MiddleClick extends Module {
    public static NumberSetting action;
    public static BooleanSetting showHelp;
    public static DescriptionSetting actionDesc;
    int prevSlot;
    public static boolean a;
    private boolean hasClicked;
    public Method cpsCap;

    public MiddleClick() {
        super("Middleclick", ModuleCategory.Misc, 0);
        this.registerSetting(showHelp = new BooleanSetting("Show friend help in chat", true));
        this.registerSetting(action = new NumberSetting("Value:", 1,1, 3, 1));
        this.registerSetting(actionDesc = new DescriptionSetting("Mode: PEARL_THROW"));
    }

    public void guiUpdate() {
        actionDesc.setDesc(Utils.md + actions.values()[(int)(action.getInput() -1)]);
    }


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        if(!Utils.Player.isPlayerInGame()) return;

        if(Mouse.isButtonDown(2) && !hasClicked) {
            switch (actions.values()[(int)(action.getInput() -1)]){
                case PEARL_THROW:
                    for (int slot = 0; slot <= 8; slot++) {
                        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
                        if(itemInSlot != null && itemInSlot.getItem() instanceof ItemEnderPearl) {
                            prevSlot = mc.thePlayer.inventory.currentItem;
                            mc.thePlayer.inventory.currentItem = slot;
                            try {
                                cpsCap = mc.getClass().getDeclaredMethod("func_147121_ag");
                                cpsCap.setAccessible(true);
                                cpsCap.invoke(mc);
                            } catch (Exception var3) {
                                try {
                                    cpsCap = mc.getClass().getDeclaredMethod("rightClickMouse");
                                    cpsCap.setAccessible(true);
                                    cpsCap.invoke(mc);
                                } catch (Exception var4) {
                                    //var3.printStackTrace();
                                }
                                //var3.printStackTrace();
                            }
                            mc.thePlayer.inventory.currentItem = prevSlot;
                            hasClicked = true;
                            return;
                        }
                    }
                    break;

                case ADD_FRIEND:
                    addFriend();
                    if(showHelp.isToggled()) showHelpMessage();
                    break;

                case REMOVE_FRIEND:
                    removeFriend();
                    if(showHelp.isToggled()) showHelpMessage();
                    break;

            }
            hasClicked = true;
        } else if(!Mouse.isButtonDown(2) && hasClicked) {
            hasClicked = false;
        }
    }

    private void showHelpMessage() {
        if(showHelp.isToggled()) {
            Utils.Player.sendMessageToSelf("Run 'help friends' in CommandLine to find out how to add, remove and view friends.");
        }
    }

    private void removeFriend() {
        Entity player = mc.objectMouseOver.entityHit;
        if(player == null) {
            Utils.Player.sendMessageToSelf("Please aim at a player/entity when removing them.");
        } else {
            if (AimAssist.removeFriend(player)) {
                Utils.Player.sendMessageToSelf("Successfully removed " + player.getName() + " from friends list!");
            } else {
                Utils.Player.sendMessageToSelf(player.getName() + " was not found in the friends list!");
            }
        }
    }

    private void addFriend() {
        Entity player = mc.objectMouseOver.entityHit;
        if(player == null) {
            Utils.Player.sendMessageToSelf("Please aim at a player/entity when adding them.");
        }
        else {
            AimAssist.addFriend(player);
            Utils.Player.sendMessageToSelf("Successfully added " + player.getName() + " to friends list.");
        }
    }

    public enum actions {
        PEARL_THROW,
        ADD_FRIEND,
        REMOVE_FRIEND
    }
}
