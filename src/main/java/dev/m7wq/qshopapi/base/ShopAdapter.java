package dev.m7wq.qshopapi.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.avaje.ebean.validation.NotNull;

import dev.m7wq.qshopapi.annotations.Lore;
import dev.m7wq.qshopapi.annotations.Size;
import dev.m7wq.qshopapi.annotations.Title;
import dev.m7wq.qshopapi.object.Contents;
import dev.m7wq.qshopapi.object.Item;
import dev.m7wq.qshopapi.object.Shop;

public class  ShopAdapter {

    /**
     * Title annotation represents metadata of shop's title.
     * Size annotation represents metadata of shop's inventory size
     * Lore annotation represents metadata of lore which included by every item on shop
     *
     * @return Customized and structured Shop entity
     */
    @Title(title = "Default Title")
    @Size(size = 9)
    @Lore(lore = {"&aThis Is Shop's Item","&cBuy Me!","&eCost: %cost%"})
    public @NotNull Shop getShop(){
        return new Shop();
    }

    /**
     * Retrieves the contents of the shop.
     *
     * @return an object of type Contents, which contains an array of items.
     *         Each item is represented by an Item object, including the item itself,
     *         its slot position in the inventory, and its cost.
     */
    public @NotNull Contents getContents(){
        return Contents.of(
            new Item[]{
                new Item(new ItemStack(Material.COOKIE), 5, 1)
            }
        );
    }

}
