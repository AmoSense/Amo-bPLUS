package me.kopamed.raven.bplus.client.feature.module.modules.hotkey;

import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import net.minecraft.item.*;

public class Healing extends Module {
    private final BooleanSetting preferSlot;
    private final NumberSetting hotbarSlotPreference;
    private final NumberSetting itemMode;
    private final DescriptionSetting modeDesc;
    public Healing() {
        super("Healing", ModuleCategory.Hotkeys, 0);

        this.registerSetting(preferSlot = new BooleanSetting("Prefer a slot", false));
        this.registerSetting(hotbarSlotPreference = new NumberSetting("Prefer wich slot", 8, 1, 9, 1));
        this.registerSetting(itemMode = new NumberSetting("Value:", 1, 1,4, 1));
        this.registerSetting(modeDesc = new DescriptionSetting("Mode: SOUP"));
    }

    public void guiUpdate() {
        modeDesc.setDesc(Utils.md + HealingItems.values()[(int) itemMode.getInput() - 1]);
    }

    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame())
            return;

        if (preferSlot.isToggled()) {
            int preferedSlot = (int) hotbarSlotPreference.getInput() - 1;


            if(HealingItems.values()[(int) itemMode.getInput() - 1] == HealingItems.SOUP && isSoup(preferedSlot)) {
                mc.thePlayer.inventory.currentItem = preferedSlot;
                this.disable();
                return;
            } else if(HealingItems.values()[(int) itemMode.getInput() - 1] == HealingItems.GAPPLE && isGapple(preferedSlot)){
                mc.thePlayer.inventory.currentItem = preferedSlot;
                this.disable();
                return;
            } else if(HealingItems.values()[(int) itemMode.getInput() - 1] == HealingItems.FOOD && isFood(preferedSlot)){
                mc.thePlayer.inventory.currentItem = preferedSlot;
                this.disable();
                return;
            } else if(HealingItems.values()[(int) itemMode.getInput() - 1] == HealingItems.ALL && (isGapple(preferedSlot) || isFood(preferedSlot) || isSoup(preferedSlot))){
                mc.thePlayer.inventory.currentItem = preferedSlot;
                this.disable();
                return;
            }

        }

        for (int slot = 0; slot <= 8; slot++) {
            if(HealingItems.values()[(int) itemMode.getInput() - 1] == HealingItems.SOUP && isSoup(slot)) {
                mc.thePlayer.inventory.currentItem = slot;
                this.disable();
                return;
            } else if(HealingItems.values()[(int) itemMode.getInput() - 1] == HealingItems.GAPPLE && isGapple(slot)){
                mc.thePlayer.inventory.currentItem = slot;
                this.disable();
                return;
            } else if(HealingItems.values()[(int) itemMode.getInput() - 1] == HealingItems.FOOD && isFood(slot)){
                mc.thePlayer.inventory.currentItem = slot;
                this.disable();
                return;
            } else if(HealingItems.values()[(int) itemMode.getInput() - 1] == HealingItems.ALL && (isGapple(slot) || isFood(slot) || isSoup(slot))){
                mc.thePlayer.inventory.currentItem = slot;
                this.disable();
                return;
            }
        }
        this.disable();
    }

    public static boolean checkSlot(int slot) {
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);

        return itemInSlot != null && itemInSlot.getDisplayName().equalsIgnoreCase("ladder");
    }

    public enum HealingItems {
        SOUP,
        GAPPLE,
        //NOTCH_APPLE,
        //HEAD,
        FOOD,
        ALL
    }

    public boolean isSoup(int slot){
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
        if(itemInSlot == null)
            return false;
        return itemInSlot.getItem() instanceof ItemSoup;
    }

    public boolean isGapple(int slot){
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
        if(itemInSlot == null)
            return false;

        return itemInSlot.getItem() instanceof ItemAppleGold;
    }

    public boolean isHead(int slot){
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
        if(itemInSlot == null)
            return false;

        return itemInSlot.getItem() instanceof Item;
    }

    public boolean isFood(int slot){
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
        if(itemInSlot == null)
            return false;

        return itemInSlot.getItem() instanceof ItemFood;
    }
}
