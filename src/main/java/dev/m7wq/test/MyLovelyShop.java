package dev.m7wq.test;

import dev.m7wq.qshopapi.annotations.*;
import dev.m7wq.qshopapi.annotations.enums.ClickPurpose;
import dev.m7wq.qshopapi.annotations.settings.Settings;
import dev.m7wq.qshopapi.annotations.settings.enums.ShopStatus;
import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.listeners.enums.StatusDisplay;
import dev.m7wq.qshopapi.main.enums.Capacity;
import dev.m7wq.qshopapi.listeners.ItemListener;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.ObjectStreamException;
import java.util.Arrays;


@Shop(title = "Black Market", capacity = Capacity.SIX_ROWS)
@Settings(
        cancelClickEvent = true, // Default -> true
        status = ShopStatus.SELECTABLE // Default -> DIRECT
)
public class MyLovelyShop {

    /**
     * Selectable Item if ShopStatus is SELECTABLE
     * @%status% this is built-in placeholder that'd be replaced with item-status (selected, not-selected)
     * @ItemListener let the developer listen and implement the following events
     * @StatusDisplay let you define the label of the item status
     */
    @Slot(0) // -- Example
    Item SelectableItem = Item.builder().name("test").lore(Arrays.asList("%status%"))
            .listener(new ItemListener() {
                @Override
                public void onSelect(Player player, Item item) {
                    player.sendMessage("You selected"+item.getName());
                }

                @Override
                public void onUnSelect(Player player, Item item) {
                    player.sendMessage("You un-selected"+item.getName());
                }
            }).display(
                    StatusDisplay.builder()
                            .selected("&eSelected") // Default &aSELECTED
                            .unSelected("&eUn-Selected") // Default &cNOT SELECTED
                            .notPurchased("&eNone") // Default &cNONE
                            .build()
            )
            .build();

    // ----- OR -----

    @Slot(0)
    Item directPurchableItem = Item.builder().name("test")
            .listener(new ItemListener() {
                @Override
                public void onClick(Player player, Item item) {
                    player.sendMessage("You have bought: "+item.getName());
                }
            }).build();


    /**
     * Single slot making
     * @Annotation receives slot-value
     */
    @Slot(1)
    Item test = Item.builder().name("item").price(3)
            .build();

    /**
     * @Purpose For several Items,
     * You have to define the slot in the object
     */
    @Slots
    Item[] items = new Item[]{
            Item.builder().name("item1").slot(2).build(),
            Item.builder().name("item2").slot(3).price(5).build()
    };

    /**
     * @Usage of input system,
     * @Input annotation replaces the value of the variable with the registered input
     * @AnnotatedClickable annotation complete the purpose when `forSlot` get clicked
     * @Dependencies qShopAPI supports Imperat for command handling
     */

    // Perform a command
    @Input("command1")
    @Clickable(purpose = ClickPurpose.PERFORM_COMMAND, forSlot = 1)
    Command<BukkitSource> command;

    // Open an inventory
    @Input("menu1")
    @Clickable(purpose = ClickPurpose.OPEN_INVENTORY, forSlot = 2)
    Inventory inventory;

    // Purchase an item
    @Input("myItem")
    @Clickable(purpose = ClickPurpose.DIRECT_PURCHASE, forSlot = 3)
    ItemStack item;

    /**
     * Making sub-shop
     * @Annotate @Shop Annotation like any shop
     * @Clickable You have to put clickable-purpose so you can make it open-able,
     * And define the slot of the item if you clicked on it open the sub-shop
     */
    @Shop(title = "White Market", capacity = Capacity.ONE_ROW)
    @Clickable(purpose = ClickPurpose.OPEN_SUB_SHOP, forSlot = 3)
    public static class WhiteMarket{

        @Slot(1)
        Item test = Item.builder().build();

        @Slots
        Item[] items = new Item[]{
                Item.builder().name("item1").slot(3).build(),
                Item.builder().name("item2").slot(4).build()
        };

    }
}
