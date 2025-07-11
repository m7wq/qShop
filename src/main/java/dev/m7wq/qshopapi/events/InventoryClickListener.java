package dev.m7wq.qshopapi.events;

import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.storage.Shops;
import dev.m7wq.qshopapi.utils.TextHelper;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class InventoryClickListener implements Listener {

    Shops shops;

    @EventHandler
    public void onClick(InventoryClickEvent e){

        ItemStack currentItem = e.getCurrentItem();

        shops.getShops().forEach(shop ->{

            for (Item item : shop.getItems()){

                if (item.getSlot() == e.getSlot()
                && item.getType() == currentItem.getType()
                && TextHelper.format(item.getName()).equalsIgnoreCase(TextHelper.format(currentItem.getItemMeta().getDisplayName()))){

                    item.getClickable().click(e);
                    return;
                }

            }

            shop.getSubShops().forEach(subShop ->{
                for (Item item : shop.getItems()){

                    if (item.getSlot() == e.getSlot()
                            && item.getType() == currentItem.getType()
                            && item.getName() == currentItem.getItemMeta().getDisplayName()){

                        item.getClickable().click(e);

                    }

                }
            });


        });



    }
}
