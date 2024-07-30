package dev.m7wq.qshopapi.object;

import org.bukkit.inventory.ItemStack;

public class Item {

    /**
     * The ItemStack representing the item.
     * This attribute holds the actual item, including its type, amount, and any additional metadata.
     */

    private ItemStack itemStack;

    /**
     * The slot in the inventory where this item should be placed.
     * The slot is represented as an integer, typically indicating the position in a container or GUI.
     */

    private int slot;

    /**
     * The cost of the item.
     * This integer represents the price required to purchase the item, which could correspond to currency, points, or any other unit of value.
     */
    private int cost;

    /**
     * Constructor to create a new Item instance.
     * 
     * @param itemStack the ItemStack representing the item, including its properties and metadata.
     * @param slot the slot position in the inventory where this item will be placed.
     * @param cost the cost associated with the item.
     */
    
    public Item(ItemStack itemStack, int slot, int cost) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.cost = cost;
    }

    /**
     * Retrieves the ItemStack associated with this item.
     * 
     * @return the ItemStack object, which includes details about the item such as type, amount, and metadata.
     */

    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Sets the ItemStack for this item.
     * 
     * @param itemStack the new ItemStack to associate with this item. This allows updating the item's properties.
     */

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Retrieves the slot position for this item in the inventory.
     * 
     * @return an integer representing the slot position.
     */

    public int getSlot() {
        return slot;
    }

    /**
     * Sets the slot position for this item in the inventory.
     * 
     * @param slot the new slot position. This updates where the item should appear in the inventory.
     */

    public void setSlot(int slot) {
        this.slot = slot;
    }

    /**
     * Retrieves the cost of this item.
     * 
     * @return the cost as an integer, indicating the price required to acquire this item.
     */

    public int getCost() {
        return cost;
    }

    /**
     * Sets the cost for this item.
     * 
     * @param cost the new cost as an integer. This updates the price required for purchasing the item.
     */
    
    public void setCost(int cost) {
        this.cost = cost;
    }
}


    
    
