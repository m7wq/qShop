
# qShop

qShop is a customizable shop-maker for Minecraft servers with a flexible API for various in-game shops.

## Installation

[![](https://jitpack.io/v/m7wq/qShop.svg)](https://jitpack.io/#m7wq/qShop)

### Maven

Add this to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.m7wq</groupId>
    <artifactId>qShop</artifactId>
    <version>2.0</version>
</dependency>
```

### Gradle

Add this to your `build.gradle`:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.m7wq:qShop:2.0'
}
```

## Usage

### Making a shop

```java
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
```

### Setup in Main Class
And examples of the usages of the ShopAPI

```java
package dev.m7wq.test;


import dev.m7wq.qshopapi.ShopAPI;
import dev.m7wq.qshopapi.entity.Input;
import dev.m7wq.qshopapi.main.ShopInterface;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){

        ShopAPI shopAPI = new ShopAPI(this);

        // Registering a shop
        shopAPI.registerShop(new MyLovelyShop());

        // Get as inventory
        Inventory shop = shopAPI.getShop("ShopTitle");

        // registering command input:-

        /**
         * Or you can use annotated command class
         * @see https://docs.velix.dev/Imperat/
         * for more info about imperat
         */
        Command<BukkitSource> command = Command.<BukkitSource>create("lol").build();
        command.setDefaultUsageExecution((source,context)->{
            source.reply("&aHello there!");
        });

        // Register the input
        shopAPI.registerInput(
                "command1",
                new Input<Command<BukkitSource>>().of(command)
        );

        // Registering Inventory Input:-

        // Making the menu
        Inventory inventory = Bukkit.createInventory(null,9,"Inventory");


        // Registering menu input
        shopAPI.registerInput("menu1", new Input<Inventory>().of(inventory));

        // Registering ItemStack input:-

        // Making the item
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);

        // registering item input
        shopAPI.registerInput("myItem",new Input<ItemStack>().of(itemStack));

        // Registering an Edit
        // NOTE: This is built in by the way!!!
        // Edit will be effected in DisplayName & Lore
        shopAPI.registerEdit("%price%",((string, item) -> string.replace("%price%",String.valueOf(item.getPrice()))));


        Bukkit.getPluginManager().registerEvents(new PlayerPurchaseListener(),this);


    }
}

```

### Using `PlayerPurchaseEvent`

Built-in event for player purchase event while using `ClickPurpose.DIRECT_PURCHASE`

```java

package dev.m7wq.test;

import dev.m7wq.qshopapi.payment.PlayerPurchaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPurchaseListener implements Listener {

    @EventHandler
    public void onPurchase(PlayerPurchaseEvent event){

        int price = event.getPrice();
        Player player = event.getPlayer();

        // Handle your implementation...


    }

}
```

## Contributing

Contribute by submitting pull requests or opening issues on GitHub.
Feel free to adjust as needed for your specific implementation!
