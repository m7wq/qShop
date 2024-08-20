package dev.m7wq.qshopapi.base;

import org.bukkit.entity.Player;

import com.avaje.ebean.validation.NotNull;

import dev.m7wq.qshopapi.object.Contents;
import dev.m7wq.qshopapi.object.Lore;


public interface ShopAdapter {

    /**
     * Retrieves the contents of the shop.
     * 
     * @return an object of type Contents, which contains an array of items.
     *         Each item is represented by an Item object, including the item itself,
     *         its slot position in the inventory, and its cost.
     */
    @NotNull Contents getContents();

    /**
     * Retrieves the title of the inventory.
     * 
     * @return a String representing the title of the inventory. 
     *         The title should not contain any formatted color codes.
     */
    @NotNull String getTitle();
        
    /**
     * Retrieves the size of the inventory.
     * 
     * @return an Integer representing the size of the inventory. 
     *         The size should be one of the following values: 9, 18, 27, 36, 45, or 54.
     *         These values represent the number of slots in the inventory.
     */
    @NotNull Integer getSize();

    /**
     * Retrieves the lore for items in the shop.
     * 
     * @return a Lore object containing a formatted list of lore lines.
     *         Each line may include formatting with the '&' character.
     *         The placeholder %cost% will be replaced with the item's price.
     *         Returns null if individual items have their own specific lore.
     */
    Lore getLore();

    /**
     * Retrieves the default lore applied to items after purchase.
     * 
     * @return a Lore object representing the lore that will be added to an item after it is purchased.
     *         The lore can include formatting and color codes, which will be displayed to the player.
     *         
     */
    @NotNull Lore getDefaultLore();
}
