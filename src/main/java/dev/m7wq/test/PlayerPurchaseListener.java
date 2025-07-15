package dev.m7wq.test;

import dev.m7wq.qshopapi.listeners.PurchaseListener;
import dev.m7wq.qshopapi.payment.PlayerPurchaseEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PlayerPurchaseListener implements PurchaseListener {


    /**
     * @param player Who's going to purchase
     * @param price The price of the sale
     * @return boolean if purchase has success (true) or failed (false)
     */
    @Override
    public boolean purchase(Player player, int price) {

        // Basic example

        Plugin plugin = Bukkit.getPluginManager().getPlugin("Example");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),"data.yml"));

        ConfigurationSection section = config.getConfigurationSection("players-coins");

        int coins = section.getInt(player.getName());

        if (coins < price)
            return false;

        section.set(player.getName(), coins-price);
        return true;
    }
}
