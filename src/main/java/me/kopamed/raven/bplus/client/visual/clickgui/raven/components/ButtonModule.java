package me.kopamed.raven.bplus.client.visual.clickgui.raven.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import me.kopamed.raven.bplus.client.visual.clickgui.raven.Component;
import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.modules.client.Gui;
import me.kopamed.raven.bplus.client.feature.setting.Setting;
import me.kopamed.raven.bplus.client.feature.setting.settings.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class ButtonModule extends Component {
   private final int c1 = (new Color(0, 85, 255)).getRGB();
   private final int c2 = (new Color(154, 2, 255)).getRGB();
   private final int c3 = (new Color(175, 143, 233) ).getRGB();
   public Module mod;
   public ButtonCategory category;
   public int o;
   private final ArrayList<Component> settings;
   public boolean po;

   public ButtonModule(Module mod, ButtonCategory p, int o) {
      this.mod = mod;
      this.category = p;
      this.o = o;
      this.settings = new ArrayList();
      this.po = false;
      int y = o + 12;
      if (!mod.getSettings().isEmpty()) {
         for (Setting v : mod.getSettings()) {
            if (v instanceof NumberSetting) {
               NumberSetting n = (NumberSetting) v;
               ButtonSlider s = new ButtonSlider(n, this, y);
               this.settings.add(s);
               y += 12;
            } else if (v instanceof BooleanSetting) {
               BooleanSetting b = (BooleanSetting) v;
               ButtonTick c = new ButtonTick(mod, b, this, y);
               this.settings.add(c);
               y += 12;
            } else if (v instanceof DescriptionSetting) {
               DescriptionSetting d = (DescriptionSetting) v;
               ButtonDesc m = new ButtonDesc(d, this, y);
               this.settings.add(m);
               y += 12;
            } else if (v instanceof RangeSetting) {
               RangeSetting n = (RangeSetting) v;
               ButtonMinMaxSlider s = new ButtonMinMaxSlider(n, this, y);
               this.settings.add(s);
               y += 12;
            }else if (v instanceof ComboSetting) {
               ComboSetting n = (ComboSetting) v;
               ButtonMode s = new ButtonMode(n, this, y);
               this.settings.add(s);
               y += 12;
            }

         }
      }

   }

   public void setModuleStartAt(int n) {
      this.o = n;
      int y = this.o + 16;
      Iterator var3 = this.settings.iterator();

      while(true) {
         while(var3.hasNext()) {
            Component co = (Component)var3.next();
            co.setModuleStartAt(y);
            if (co instanceof ButtonSlider  || co instanceof ButtonMinMaxSlider) {
               y += 16;
            } else if (co instanceof ButtonTick  || co instanceof ButtonDesc || co instanceof ButtonMode) {
               y += 12;
            }
         }

         return;
      }
   }

   public static void e() {
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
   }

   public static void f() {
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
      GL11.glEdgeFlag(true);
   }

   public static void g(int h) {
      float a = 0.0F;
      float r = 0.0F;
      float g = 0.0F;
      float b = 0.0F;

      GL11.glColor4f(r, g, b, a);
   }

   public static void v(float x, float y, float x1, float y1, int t, int b) {
      e();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      g(t);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      g(b);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      f();
   }

   public void draw() {
      v((float)this.category.getX(), (float)(this.category.getY() + this.o), (float)(this.category.getX() + this.category.getWidth()), (float)(this.category.getY() + 15 + this.o), this.mod.isToggled() ? this.c2 : -12829381, this.mod.isToggled() ? this.c2 : -12302777);
      GL11.glPushMatrix();
      // module text button
      //int button_rgb = Gui.guiTheme.getInput() == 3.0D ? (this.mod.isToggled() ? this.c1 : Color.lightGray.getRGB()) : (Gui.guiTheme.getInput() == 4.0D? (this.mod.isToggled() ? this.c3 : Color.lightGray.getRGB()) : Color.lightGray.getRGB());
      //Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.mod.getName(), (float)(this.category.getX() + this.category.getWidth() / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.mod.getName()) / 2), (float)(this.category.getY() + this.o + 4), button_rgb);
      GL11.glPopMatrix();
      if (this.po && !this.settings.isEmpty()) {
         for (Component c : this.settings) {
            c.draw();
         }
      }

   }

   public int getHeight() {
      if (!this.po) {
         return 16;
      } else {
         int h = 16;
         Iterator var2 = this.settings.iterator();

         while(true) {
            while(var2.hasNext()) {
               Component c = (Component)var2.next();
               if (c instanceof ButtonSlider || c instanceof ButtonMinMaxSlider) {
                  h += 16;
               } else if (c instanceof ButtonTick  || c instanceof ButtonDesc || c instanceof ButtonMode) {
                  h += 12;
               }
            }

            return h;
         }
      }
   }

   public void update(int x, int y) {
      if (!this.settings.isEmpty()) {
         for (Component c : this.settings) {
            c.update(x, y);
         }
      }

   }

   public void mouseDown(int x, int y, int b) {

      if (this.ii(x, y) && b == 0) {
         this.mod.toggle();
      }

      if (this.ii(x, y) && b == 1) {
         this.po = !this.po;
         this.category.r3nd3r();
      }

      for (Component c : this.settings) {
         c.mouseDown(x, y, b);
      }

   }

   public void mouseReleased(int x, int y, int b) {
      for (Component c : this.settings) {
         c.mouseReleased(x, y, b);
      }

   }

   public void keyTyped(char t, int k) {
      for (Component c : this.settings) {
         c.keyTyped(t, k);
      }

   }

   public boolean ii(int x, int y) {
      return x > this.category.getX() && x < this.category.getX() + this.category.getWidth() && y > this.category.getY() + this.o && y < this.category.getY() + 16 + this.o;
   }
}
