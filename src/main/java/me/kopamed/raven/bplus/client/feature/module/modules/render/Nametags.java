package me.kopamed.raven.bplus.client.feature.module.modules.render;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.NumberSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;
import me.kopamed.raven.bplus.client.feature.setting.settings.BooleanSetting;
import me.kopamed.raven.bplus.client.feature.module.modules.world.AntiBot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
   public static NumberSetting a;
   public static BooleanSetting b;
   public static BooleanSetting c;
   public static BooleanSetting d;
   public static BooleanSetting rm;
   public static BooleanSetting e;

   public Nametags() {
      super("Nametags", ModuleCategory.Render, 0);
      this.registerSetting(a = new NumberSetting("Offset", 0.0D, -40.0D, 40.0D, 1.0D));
      this.registerSetting(b = new BooleanSetting("Rect", true));
      this.registerSetting(c = new BooleanSetting("Show health", true));
      this.registerSetting(d = new BooleanSetting("Show invis", true));
      this.registerSetting(rm = new BooleanSetting("Remove tags", false));
   }

   @SubscribeEvent
   public void r(Pre e) {
      if (rm.isToggled()) {
         e.setCanceled(true);
      } else {
         if (e.entity instanceof EntityPlayer && e.entity != mc.thePlayer && e.entity.deathTime == 0) {
            EntityPlayer en = (EntityPlayer)e.entity;
            if (!d.isToggled() && en.isInvisible()) {
               return;
            }

            if (AntiBot.bot(en) || en.getDisplayNameString().isEmpty()) {
               return;
            }

            e.setCanceled(true);
            String str = en.getDisplayName().getFormattedText();
            if (c.isToggled()) {
               double r = en.getHealth() / en.getMaxHealth();
               String h = (r < 0.3D ? "§c" : (r < 0.5D ? "§6" : (r < 0.7D ? "§e" : "§a"))) + Utils.Java.round(en.getHealth(), 1);
               str = str + " " + h;
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate((float)e.x + 0.0F, (float)e.y + en.height + 0.5F, (float)e.z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            float f1 = 0.02666667F;
            GlStateManager.scale(-f1, -f1, f1);
            if (en.isSneaking()) {
               GlStateManager.translate(0.0F, 9.374999F, 0.0F);
            }

            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = (int)(-a.getInput());
            int j = mc.fontRendererObj.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            if (b.isToggled()) {
               worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
               worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               tessellator.draw();
            }

            GlStateManager.enableTexture2D();
            mc.fontRendererObj.drawString(str, -mc.fontRendererObj.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
         }

      }
   }
}
