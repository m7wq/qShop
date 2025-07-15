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

    }
}
