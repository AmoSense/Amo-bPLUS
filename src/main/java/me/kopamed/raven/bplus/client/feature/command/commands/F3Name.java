package me.kopamed.raven.bplus.client.feature.command.commands;

import me.kopamed.raven.bplus.client.visual.clickgui.raven.CommandLine;
import me.kopamed.raven.bplus.client.feature.command.Command;
import me.kopamed.raven.bplus.client.feature.module.modules.client.ClientNameSpoof;

public class F3Name extends Command {
    public F3Name() {
        super("f3name", "Changes the client's name in f3", 1, 1,  new String[] {"New client name"},  new String[] {"f3n"});
    }

    public void onCall(String[] args){
        if(args == null){
            this.incorrectArgs();
            return;
        }
        String wut = args[1];
        if(args.length > 2){
            for(int i = 2; i < args.length; i++){
                wut += " " + args[i];
            }
        }
        ClientNameSpoof.newName = wut;
        CommandLine.print("§aSet client name to " + wut, 1);
    }
}
