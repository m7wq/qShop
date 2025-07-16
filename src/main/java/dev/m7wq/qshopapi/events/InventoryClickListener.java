package dev.m7wq.qshopapi.events;

import dev.m7wq.qshopapi.ShopAPI;
import dev.m7wq.qshopapi.annotations.settings.enums.ShopStatus;
import dev.m7wq.qshopapi.data.DataStorage;
import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.listeners.enums.ItemStatus;
import dev.m7wq.qshopapi.storage.Shops;
import dev.m7wq.qshopapi.utils.ShopUtil;
import dev.m7wq.qshopapi.utils.TextHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


public class InventoryClickListener implements Listener {

    public InventoryClickListener(Shops shops, ShopAPI api) {
        this.shops = shops;
        this.api = api;
    }

    Shops shops;
    ShopAPI api;

    @EventHandler
    public void onClick(InventoryClickEvent e){



        @Nullable ItemStack currentItem = e.getCurrentItem();

        if (currentItem == null || currentItem.getItemMeta() == null || currentItem.getItemMeta().getDisplayName() == null)
            return;

        if (!(e.getWhoClicked() instanceof Player player))
            return;



        shops.getShops().forEach(shop -> {

            if (shop.getTitle().equalsIgnoreCase(e.getInventory().getTitle())) {

                if(shop.getSettings().isCancelClickEvent())
                    e.setCancelled(true);

                for (Item item : shop.getItems()) {


                    String currentItemName = TextHelper.format(currentItem.getItemMeta().getDisplayName());

                    String itemName = TextHelper.format(item.getName());

                    if (item.getSlot() == e.getSlot() && item.getType() == currentItem.getType()) {


                        if (currentItemName.equalsIgnoreCase(itemName)) {

                            DataStorage storage = api.getStorage();

                            // That's mean that its direct purchase
                            if (!storage.containsKey(shop.getTitle())){
                                item.getListener().onClick(player,item);
                                return;
                            }

                            HashMap<Item,ItemStatus> statusMap = storage.get(shop.getTitle());

                            ItemStatus itemStatus = statusMap.get(item);

                            if (itemStatus == ItemStatus.NOT_PURCHASED){

                                boolean purchased = api.getPurchaseListener().purchase(player,item.getPrice());

                                if (purchased) {
                                    statusMap.put(item, ItemStatus.SELECTED);
                                    item.getListener().onSelect(player,item);
                                }

                            }else if (itemStatus == ItemStatus.SELECTED){
                                statusMap.put(item, ItemStatus.UN_SELECTED);
                                item.getListener().onUnSelect(player,item);
                            } else if (itemStatus == ItemStatus.UN_SELECTED) {
                                statusMap.put(item,ItemStatus.SELECTED);
                                item.getListener().onSelect(player,item);


                            }

                            ShopUtil.updateShop(player,shop,api.getEdits()); // refresh the shop


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
