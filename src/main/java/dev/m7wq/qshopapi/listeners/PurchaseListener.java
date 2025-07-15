package dev.m7wq.qshopapi.listeners;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public interface PurchaseListener {


    /**
     * @param player Who's going to purchase
     * @param price The pricee of the sale
     * @return boolean if purchase has succeed (true) or failed (false)
     */
    boolean purchase(Player player, int price);

}
