package dev.m7wq.qshopapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.avaje.ebean.validation.NotNull;
import dev.m7wq.qshopapi.base.ShopAdapter;
import dev.m7wq.qshopapi.messages.Messager;
import dev.m7wq.qshopapi.object.Item;
import net.md_5.bungee.api.ChatColor;

public class qShopAPI {

    // Instance variable for handling message configurations and texts
    private Messager messages;
    
    // Static instance of qShopAPI for singleton pattern implementation
    private static qShopAPI instance;

    /**
     * Loads the qShopAPI instance and initializes necessary objects.
     * This method should be called to set up the API with the required
     * message configurations before use.
     * 
     * @param messages the Messager object containing message configurations,
     *                 such as purchase success and insufficient balance messages.
     */

    public void load(Messager messages) {

        instance = new qShopAPI();

        this.messages = messages;
    }

    /**
     * Retrieves the singleton instance of qShopAPI.
     * 
     * @return the qShopAPI instance.
     * @throws IllegalStateException if the instance has not been initialized by calling load().
     */

    public static qShopAPI getInstance() {
        if (instance == null)
            throw new IllegalStateException("qShopAPI instance is not initialized correctly, " +
                                            "please try calling the method qShopAPI#load");
        return instance;
    }

    /**
     * Handles the purchase of an item using a material-based balance system.
     * 
     * @param defaultLore the default lore applied to the item after purchase.
     * @param event       the InventoryClickEvent triggered by the player's click in the shop.
     * @param balance     the Material used as the currency for transactions (e.g., gold ingots).
     */

    public void handlePurchase( List<String> defaultLore, InventoryClickEvent event, Material balance) {

        if (!(event.getWhoClicked() instanceof Player))return;

        Player player = (Player) event.getWhoClicked();

        ItemStack item = event.getCurrentItem();

        Integer price = extractFirstInteger(item.getItemMeta().getLore());


        if (!player.getInventory().contains(balance, price)) {
            player.sendMessage(colorize(messages.getNoEnoughBalanceMessage()));

            return;
        }

        ItemStack finalItem = new ItemStack(item.getType());

        ItemMeta meta = item.getItemMeta();

        handleLore:{
            if (defaultLore==null) {


                meta.setLore(null);

                break handleLore;


            }

            meta.setLore(defaultLore);
        }

        player.getInventory().removeItem(new ItemStack(balance, price));
        finalItem.setItemMeta(meta);
        player.sendMessage(colorize(messages.getPurchasedMessage()));
        player.getInventory().addItem(finalItem);
    }

    /**
     * Handles the purchase of an item using a ShopAdapter-based balance system.
     * 
     * @param adapter     the ShopAdapter interface providing the balance system.
     * @param defaultLore the default lore applied to the item after purchase.
     * @param event       the InventoryClickEvent triggered by the player's click in the shop.
     */

    public void handlePurchase(ShopAdapter adapter, List<String> defaultLore, InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player))return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        Integer price = extractFirstInteger(item.getItemMeta().getLore());

        if (adapter.getBalance(player) < price) {
            player.sendMessage(colorize(messages.getNoEnoughBalanceMessage()));
            return;
        }

        ItemStack finalItem = new ItemStack(item.getType());
        ItemMeta meta = item.getItemMeta();

        handleLore:{
            if (defaultLore == null) {
                meta.setLore(null);
                break handleLore;
            }
            meta.setLore(defaultLore);
        }

        adapter.removeBalance(player, price);
        finalItem.setItemMeta(meta);
        player.sendMessage(colorize(messages.getPurchasedMessage()));
        player.getInventory().addItem(finalItem);
    }

    /**
     * Extracts the first integer found in a list of strings.
     * This is typically used to fetch the price of an item from its lore.
     * 
     * @param array the list of strings, such as the lore lines of an item.
     * @return the first integer found, or null if none are found.
     */

    private @NotNull Integer extractFirstInteger(List<String> array) {
        Pattern pattern = Pattern.compile("\\d+");

        for (String str : array) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            }
        }
        return null; // Consider returning a default value or throwing an exception
    }

    /**
     * Constructs the shop GUI based on the provided ShopAdapter interface.
     * 
     * @param shop the implementation of ShopAdapter, providing shop details such as items and title.
     * @return the constructed Inventory representing the shop's GUI.
     * @throws NullPointerException    if the shop or any of its components are null.
     * @throws IllegalStateException if the inventory size or title is not properly defined.
     */

    public Inventory handleShopGui(ShopAdapter shop) throws NullPointerException, IllegalStateException {

        Inventory inventory = Bukkit.createInventory(null, shop.getSize(), shop.getTitle());

        for (Item item : shop.getContents().getItems()) {

            if (shop.getLore() != null) {

                ItemMeta meta = item.getItemStack().getItemMeta();
                meta.setLore(shop.getLore().getList(item));

            }

            inventory.setItem(item.getSlot(), item.getItemStack());
        }

        return inventory;
    }

    /**
     * Utility method for creating an ItemStack with specific attributes.
     * 
     * @param name         the display name of the item.
     * @param loreList     the lore (description) of the item.
     * @param material     the material type of the item.
     * @param enchAndLevel pairs of Enchantment and Integer representing the enchantment and its level.
     * @return the constructed ItemStack with the specified attributes.
     */

    public ItemStack createItem(String name, List<String> loreList, Material material, Object... enchAndLevel) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(this.colorize(name));

        if (loreList != null) {
            List<String> colorizedLore = new ArrayList<>();
            for (String s : loreList) {
                colorizedLore.add(this.colorize(s));
            }
            meta.setLore(colorizedLore);
        }

        if (enchAndLevel != null) {
            for (int i = 0; i < enchAndLevel.length; i += 2) {
                Enchantment enchantment = (Enchantment) enchAndLevel[i];
                Integer level = (Integer) enchAndLevel[i + 1];
                meta.addEnchant(enchantment, level, false);
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Utility method for colorizing strings using Minecraft color codes.
     * 
     * @param s the string to colorize.
     * @return the colorized string.
     */

    public String colorize(String s) {

        return ChatColor.translateAlternateColorCodes('&', s);
    }
}


