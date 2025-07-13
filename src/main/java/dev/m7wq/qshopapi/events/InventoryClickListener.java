package dev.m7wq.qshopapi.events;

import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.storage.Shops;
import dev.m7wq.qshopapi.utils.TextHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class InventoryClickListener implements Listener {

    public InventoryClickListener(Shops shops) {
        this.shops = shops;
    }

    Shops shops;

    @EventHandler
    public void onClick(InventoryClickEvent e){



        @NotNull ItemStack currentItem = e.getCurrentItem();



        shops.getShops().forEach(shop -> {

            if (shop.getTitle().equalsIgnoreCase(e.getInventory().getTitle())) {

                for (Item item : shop.getItems()) {


                    String currentItemName = TextHelper.format(currentItem.getItemMeta().getDisplayName());

                    String itemName = TextHelper.format(item.getName());

                    if (item.getSlot() == e.getSlot() && item.getType() == currentItem.getType()) {


                        if (currentItemName.equalsIgnoreCase(itemName)) {
                            item.getClickable().click(e);
                        }


                    }


                }


                shop.getSubShops().forEach(subShop -> {
                    for (Item item : subShop.getItems()) {


                    }
                });
            }

        });






    }
}
