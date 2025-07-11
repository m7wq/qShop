package dev.m7wq.test;

import dev.m7wq.qshopapi.annotations.*;
import dev.m7wq.qshopapi.annotations.enums.ClickPurpose;
import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.main.enums.Capacity;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.Command;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


@Shop(title = "Black Market", capacity = Capacity.SIX_ROWS)
public class MyLovelyShop {

    /**
     * Single slot making
     * @Annotation receives slot-value
     */
    @Slot(1)
    Item test = Item.builder().name("item").price(3)
            .clickable(e -> e.getWhoClicked().sendMessage("HI!!!"))
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
    @AnnotatedClickable(purpose = ClickPurpose.PERFORM_COMMAND, forSlot = 1)
    Command<BukkitSource> command;

    // Open an inventory
    @Input("menu1")
    @AnnotatedClickable(purpose = ClickPurpose.OPEN_INVENTORY, forSlot = 2)
    Inventory inventory;

    // Purchase an item
    @Input("myItem")
    @AnnotatedClickable(purpose = ClickPurpose.DIRECT_PURCHASE, forSlot = 3)
    ItemStack item;

    /**
     * Making sub-shop
     * @Annotate @Shop Annotation like any shop
     * @Clickable You have to put clickable-purpose so you can make it open-able,
     * And define the slot of the item if you clicked on it open the sub-shop
     */
    @Shop(title = "White Market", capacity = Capacity.ONE_ROW)
    @AnnotatedClickable(purpose = ClickPurpose.OPEN_SUB_SHOP, forSlot = 3)
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
