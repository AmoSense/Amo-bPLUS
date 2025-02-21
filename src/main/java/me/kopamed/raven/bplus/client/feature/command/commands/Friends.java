package me.kopamed.raven.bplus.client.feature.command.commands;

import me.kopamed.raven.bplus.client.feature.command.Command;
import me.kopamed.raven.bplus.client.feature.module.modules.combat.AimAssist;
import me.kopamed.raven.bplus.client.visual.clickgui.raven.CommandLine;
import net.minecraft.entity.Entity;

public class Friends extends Command {
    public Friends() {
        super("friends", "Allows you to manage and view<br>your friends list", 1, 2, new String[]{"add / remove / list", "Player's name"}, new String[] {"f", "amigos", "lonely4ever"});
    }

    @Override
    public void onCall(String[] args){
        if (args == null){
            listFriends();
        }

        else if(args[1].equalsIgnoreCase("list")) {
            listFriends();
        }

        else if(args.length == 3){
            if(args[1].equalsIgnoreCase("add")){
                boolean added = AimAssist.addFriend(args[2]);
                if (added) {
                    CommandLine.print("&aSuccessfully added ", 1);
                    CommandLine.print(args[2], 0);
                    CommandLine.print("&ato your friends list!", 0);
                } else {
                    CommandLine.print("&eAn error occurred!", 1);
                }
            }
            else if(args[1].equalsIgnoreCase("remove")){
                boolean removed = AimAssist.removeFriend(args[2]);
                if (removed) {
                    CommandLine.print("&aSuccessfully removed ", 1);
                    CommandLine.print(args[2], 0);
                    CommandLine.print("&afrom your friends list!", 0);
                } else {
                    CommandLine.print("&eAn error occurred!", 1);
                }
            }
        }
        else {
            this.incorrectArgs();
        }
    }

    public void listFriends(){
        if(AimAssist.getFriends().isEmpty()){
            CommandLine.print("&eYou have no friends.", 1);
        }
        else {
            CommandLine.print("&aHere are your friends:", 1);
            for (Entity entity : AimAssist.getFriends()){
                CommandLine.print(entity.getName(), 0);
            }
        }
    }
}
