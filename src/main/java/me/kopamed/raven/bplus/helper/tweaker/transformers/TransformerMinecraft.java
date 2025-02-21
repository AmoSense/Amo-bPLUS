package me.kopamed.raven.bplus.helper.tweaker.transformers;

import me.kopamed.raven.bplus.helper.tweaker.ASMTransformerClass;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TransformerMinecraft implements Transformer {

   public String[] getClassName() {
      return new String[]{"net.minecraft.client.Minecraft"};
   }

   public void transform(ClassNode classNode, String transformedName) {
      for (MethodNode m : classNode.methods) {
         String n = this.mapMethodName(classNode, m);
         if (n.equalsIgnoreCase("runTick") || n.equalsIgnoreCase("func_71407_l")) {
            AbstractInsnNode[] arr = m.instructions.toArray();
            System.out.println("Inserting into: " + m.name);

            for (int i = 0; i < arr.length; ++i) {
               AbstractInsnNode ins = arr[i];
               if (i == 39) {
                  System.out.println("Inserting into 39 " + ins.toString());
                  m.instructions.insert(ins, this.getEventInsn());
               } else if (i >= 40 && i <= 45) {
                  System.out.println("Removing " + ins.toString());
                  m.instructions.remove(ins);
               } else if (i == 46) {
                  System.out.println("Returning");
                  return;
               }
            }

            return;
         }
      }

   }

   private InsnList getEventInsn() {
      InsnList insnList = new InsnList();
      insnList.add(new MethodInsnNode(184, ASMTransformerClass.eventHandlerClassName, "onTick", "()V", false));
      return insnList;
   }
}
