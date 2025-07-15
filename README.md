
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
    <version>2.6</version>
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
    implementation 'com.github.m7wq:qShop:2.6'
}
```

## Usage

### Making a shop

```java
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
```

### Setup in Main Class
And examples of the usages of the ShopAPI

```java
package dev.m7wq.test;


import dev.m7wq.qshopapi.ShopAPI;
import dev.m7wq.qshopapi.entity.Input;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    ShopAPI shopAPI;

    @Override
    public void onEnable(){

        shopAPI = new ShopAPI(this);

        // Handle Data
        shopAPI.enable();
        shopAPI.setSerializer(new MyDataSerializer());

        // Listener
        shopAPI.setPurchaseListener(new PlayerPurchaseListener());

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
                Input.of(command)
        );

        // Registering Inventory Input:-

        // Making the menu
        Inventory inventory = Bukkit.createInventory(null,9,"Inventory");


        // Registering menu input
        shopAPI.registerInput("menu1", Input.of(inventory));

        // Registering ItemStack input:-

        // Making the item
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);

        // registering item input
        shopAPI.registerInput("myItem",Input.of(itemStack));

        // Registering an Edit
        // NOTE: This is built in by the way!!!
        // Edit will be effected in DisplayName & Lore
        shopAPI.registerEdit("%price%",((string, item) -> string.replace("%price%",String.valueOf(item.getPrice()))));

        // Register Purchase Listener


        //IMPORTANT: THOSE HAVE TO BE AT THE END BECAUSE THE INPUTS HAVE TO BE INITIALIZED

        // Registering a shop
        shopAPI.registerShop(new MyLovelyShop());

        // Get as inventory (Example) you can explore ShopInterface Object
        Inventory shop = shopAPI.getShop((shopInterface) -> shopInterface.getTitle().equalsIgnoreCase("LOVELY SHOP"));
    }

    @Override
    public void onDisable(){
        // Handle data
        shopAPI.disable()
    }
}
```

### Using `PlayerPurchaseEvent`

Built-in event for player purchase event while using **Direct Purchase**

```java
package dev.m7wq.test;

import dev.m7wq.qshopapi.listeners.PurchaseListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PlayerPurchaseListener implements PurchaseListener {


    /**
     * @param player Who's going to purchase
     * @param price The price of the sale
     * @return boolean if purchase has success (true) or failed (false)
     */
    @Override
    public boolean purchase(Player player, int price) {

        // Basic example

        Plugin plugin = Bukkit.getPluginManager().getPlugin("Example");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),"data.yml"));

        ConfigurationSection section = config.getConfigurationSection("players-coins");

        int coins = section.getInt(player.getName());

        if (coins < price) {
            player.sendMessage("You need "+price+" to purchase this");
            return false;
        }

        section.set(player.getName(), coins-price);
        player.sendMessage("Bought successfully");
        return true;
    }
}
```

### Implementing DataSerializer

This is for data-handling to save item status `Selected | UnSelected | notPurchased`

```java
package dev.m7wq.test;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import dev.m7wq.qshopapi.data.DataSerializer;
import dev.m7wq.qshopapi.entity.Item;
import dev.m7wq.qshopapi.listeners.enums.ItemStatus;


import java.lang.reflect.Type;
import java.util.HashMap;

public class MyDataSerializer extends DataSerializer {

    // Save it as JSON in your database or file etc...
    @Override
    public void serialize(HashMap<String, HashMap<Item, ItemStatus>> map) {

        Gson gson = new Gson();

        String jsonString = gson.toJson(map);

        // your serialize method
        // TIP: for sql datatype use TEXT not VARCHAR!!
        // TIP: for mongodb use Document.parse(jsonString)

    }

    @Override
    public HashMap<String, HashMap<Item, ItemStatus>> deserialize() {

        Gson gson = new Gson();

        String json = "deserialize json string"; // your deserialize method

        Type type = new TypeToken<HashMap<String, HashMap<Item, ItemStatus>>>(){}.getType();

        HashMap<String, HashMap<Item, ItemStatus>> map = gson.fromJson(json,type);

        return map;


    }
}
```

## Contributing

Contribute by submitting pull requests or opening issues on GitHub.
Feel free to adjust as needed for your specific implementation!
