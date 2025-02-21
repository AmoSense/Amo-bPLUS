package me.kopamed.raven.bplus.client.feature.module.modules.hotkey;

import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.module.Module;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.*;

public class Weapon extends Module {
    public Weapon() {
        super("Weapon", ModuleCategory.Hotkeys, 0);
    }

    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame())
            return;

        int index = -1;
        double damage = -1;

        for (int slot = 0; slot <= 8; slot++) {
            ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
            if(itemInSlot == null)
                continue;
            for (AttributeModifier mooommHelp :itemInSlot.getAttributeModifiers().values()){
                if(mooommHelp.getAmount() > damage) {
                    damage = mooommHelp.getAmount();
                    index = slot;
                }
            }


        }
        if(index > -1 && damage > -1) {
            if (mc.thePlayer.inventory.currentItem != index) {
                Utils.Player.hotkeyToSlot(index);
            }
        }
        this.disable();
    }
}
