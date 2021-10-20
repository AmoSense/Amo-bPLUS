package me.kopamed.lunarkeystrokes.clickgui.raven.components;

import me.kopamed.lunarkeystrokes.clickgui.raven.Component;
import me.kopamed.lunarkeystrokes.module.setting.settings.Mode;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ButtonMode extends Component {
    private final int c = (new Color(30, 144, 255)).getRGB();
    private final Mode mode;
    private final ButtonModule p;
    private final int x;
    private final int y;
    private int o;
    private boolean registeredClick = false;
    private boolean md = false;

    public ButtonMode(Mode desc, ButtonModule b, int o) {
        this.mode = desc;
        this.p = b;
        this.x = b.category.getX() + b.category.getWidth();
        this.y = b.category.getY() + b.o;
        this.o = o;
    }

    public void draw() {
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        int bruhWidth = (int) (Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.mode.getName() + ": ") * 0.5);
        Minecraft.getMinecraft().fontRendererObj.drawString(this.mode.getName() + ": ", (float) ((this.p.category.getX() + 4) * 2), (float) ((this.p.category.getY() + this.o + 4) * 2), 0xffffffff, true);
        Minecraft.getMinecraft().fontRendererObj.drawString(this.mode.getMode(), (float) ((this.p.category.getX() + 4 + bruhWidth) * 2), (float) ((this.p.category.getY() + this.o + 4) * 2), this.c, true);
        GL11.glPopMatrix();
    }

    public void setModuleStartAt(int n) {
        this.o = n;
    }


    public void mouseDown(int x, int y, int b) {
        System.out.println("before cchange: " + this.mode.getMode());
        this.mode.nextMode();
        System.out.println("after cchange: " + this.mode.getMode());
    }

    private boolean i(int x, int y) {
        return x > this.x && x < this.x + this.p.category.getWidth() && y > this.y && y < this.y + 11;
    }
}
