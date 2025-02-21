package me.kopamed.raven.bplus.client.feature.module.modules.other;

import me.kopamed.raven.bplus.client.feature.module.Module;
import me.kopamed.raven.bplus.client.feature.module.ModuleCategory;
import me.kopamed.raven.bplus.client.feature.setting.settings.DescriptionSetting;
import me.kopamed.raven.bplus.helper.utils.Utils;
import net.minecraft.util.ChatComponentText;

public class FakeChat extends Module {
   public static DescriptionSetting a;
   public static String msg = "&eThis is a fake chat message.";
   public static final String command = "fakechat";
   public static final String c4 = "&cInvalid message.";

   public FakeChat() {
      super("Fake Chat", ModuleCategory.Misc, 0);
      this.registerSetting(a = new DescriptionSetting(Utils.Java.uf("command") + ": " + command + " [msg]"));
   }

   public void onEnable() {
      if (msg.contains("\\n")) {
         String[] split = msg.split("\\\\n");

         for (String s : split) {
            this.sm(s);
         }
      } else {
         this.sm(msg);
      }

      this.disable();
   }

   private void sm(String txt) {
      mc.thePlayer.addChatMessage(new ChatComponentText(Utils.Client.reformat(txt)));
   }
}
