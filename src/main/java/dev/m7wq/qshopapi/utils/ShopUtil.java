package dev.m7wq.qshopapi.utils;

import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.main.ShopInterface;
import dev.m7wq.qshopapi.storage.Edits;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@UtilityClass
public class ShopUtil {

    public Inventory toInventory(ShopInterface shopInterface, Edits edits){

        Inventory inventory = Bukkit.createInventory(null,shopInterface.getCapacity().getSize(),shopInterface.getTitle());


        for (Item item : shopInterface.getItems()){

            ItemTextHelper itemTextHelper = new ItemTextHelper(item);

            ItemStack itemStack = new ItemStack(item.getType());
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(itemTextHelper.format(item.getName(), edits));
            meta.setLore(itemTextHelper.format(item.getLore(), edits));

            inventory.setItem(item.getSlot(),itemStack);

        }

        return inventory;


    }

    public Inventory toInventory(ShopInterface shopInterface){

        Inventory inventory = Bukkit.createInventory(null,shopInterface.getCapacity().getSize(),shopInterface.getTitle());


        for (Item item : shopInterface.getItems()){

            ItemStack itemStack = new ItemStack(item.getType());
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(TextHelper.format(item.getName()));
            meta.setLore(TextHelper.format(item.getLore()));

            inventory.setItem(item.getSlot(),itemStack);

        }

        return inventory;


    }

}
