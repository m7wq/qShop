package dev.m7wq.qshopapi;

import dev.velix.imperat.BukkitImperat;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.Imperat;
import dev.velix.imperat.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ActionBase {

    BukkitImperat imperat;

    public ActionBase(Plugin plugin){
        imperat = BukkitImperat.builder(plugin).build();
    }

    public void registerCommand(Command<BukkitSource> command){
        imperat.registerCommand(command);
    }
}
