package me.kopamed.raven.bplus.client.feature.module.modules.render;

import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TargetHUD extends Module {
    public static BooleanSetting editPosition;
    public static int height, width;
    public static FontRenderer fr;
    ScaledResolution sr;
    public TargetHUD() {
        super("Target HUD", ModuleCategory.Render, 0);
        sr = new ScaledResolution(Minecraft.getMinecraft());
        height = sr.getScaledHeight();
        width = sr.getScaledWidth();
        fr = mc.fontRendererObj;
    }

    @SubscribeEvent
    public void r(RenderGameOverlayEvent ev) {
        if (ev.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS || !Utils.Player.isPlayerInGame()) return;
        ////System.out.println("render");
        if (mc.currentScreen != null || mc.gameSettings.showDebugInfo) {
            return;
        }
        height = sr.getScaledHeight();
        width = sr.getScaledWidth();
        ////System.out.println("render");
        ////System.out.println("left " + (int)(width * 0.65) + " || top " + (int)(height * 0.65) + " || right " + (int)(width * 0.75)  + " || bottom " + (int)(height * 0.65));
        Gui.drawRect(width - 10, 0,width, height, 0x90000000);
        Gui.drawRect((int)(width * 0.65), (int)(height * 0.65), (int)(width * 0.75), (int)(height * 0.75),0xffff4500);
    }
}
