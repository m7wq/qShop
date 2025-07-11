package dev.m7wq.test;

import dev.m7wq.qshopapi.payment.PlayerPurchaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPurchaseListener implements Listener {

    @EventHandler
    public void onPurchase(PlayerPurchaseEvent event){

        int price = event.getPrice();
        Player player = event.getPlayer();

        // Handle your implementation...


    }

}
