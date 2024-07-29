
# qShop

qShop is a customizable shop plugin for Minecraft servers, providing a flexible API for developers to create in-game shops with various currencies and features.

## Installation

### Maven

Add the following repository and dependency to your `pom.xml`:

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
    <version>1.0v</version>
</dependency>
```

### Gradle

Add the following to your `build.gradle`:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.m7wq:qShop:1.0v'
}
```

## Usage

## Setting Up in the Main Class

In your plugin's main class, you got to initialize and load the API.

### Example Implementation
```java
public class MyPlugin extends JavaPlugin {

    private qShopAPI shopAPI;

    @Override
    public void onEnable() {
        // Initialize qShopAPI with your custom messages
        Messager messager = new Messager().of("&aPurchase successful!", "&cYou don't have enough balance!");
        qShopAPI.getInstance().load(messager);

  
    }


}
```

### Implementing the `ShopAdapter`

To create a custom shop, implement the `ShopAdapter` interface. This interface defines the methods required to set up your shop's contents, title, size, and item lore.

#### Example Implementation

```java
public class MyCustomShop implements ShopAdapter {

    @Override
    public @NotNull Contents getContents() {
        Item item1 = new Item(new ItemStack(Material.DIAMOND_SWORD), 0, 1000);
        Item item2 = new Item(new ItemStack(Material.GOLDEN_APPLE), 1, 500);
        Item item3 = new Item(new ItemStack(Material.ELYTRA), 2, 2000);
        return Contents.of(new Item[]{item1, item2, item3});
    }

    @Override
    public @NotNull String getTitle() {
        return "My Custom Shop";
    }

    @Override
    public @NotNull Integer getSize() {
        return 9; // Inventory size should be one of the allowed values
    }

    @Override
    public Lore getLore() {
        return new Lore().of(Arrays.asList("&aBuy this item for %cost% coins!"));
    }

    @Override
    public @NotNull Lore getDefaultLore() {
        return new Lore().of(Arrays.asList("&bThis item was purchased!"));
    }

    @Override
    public void removeBalance(Player player, int amount) {
        // Implement your balance removal logic
    }

    @Override
    public int getBalance(Player player) {
        // Implement your balance checking logic
        return 1000; // Example balance
    }
}
```

### Using `handleShopGui`

To create and open a shop GUI for a player, use the `handleShopGui` method from `qShopAPI`. This method configures the inventory based on the `ShopAdapter` implementation.

#### Example Command Executor

```java
public class ShopGuiCommandExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return false;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("Please specify the shop name.");
            return false;
        }

        String shopName = args[0];
        ShopAdapter shopAdapter = getShopAdapterByName(shopName);
        if (shopAdapter == null) {
            player.sendMessage("Shop not found.");
            return false;
        }

        Inventory shopInventory = qShopAPI.getInstance().handleShopGui(shopAdapter);
        player.openInventory(shopInventory);

        return true;
    }

    private ShopAdapter getShopAdapterByName(String shopName) {
        // Implement logic to retrieve the ShopAdapter instance by its name
        // This could involve looking up a map or a database of registered shops
        return new MyCustomShop(); // Placeholder for actual implementation
    }
}
```

### Handling Purchases

Use the `handlePurchase` method to process purchases when a player clicks on an item in the shop. You can handle purchases with either a material-based currency or a balance-based currency.

#### Example Handling Purchase with Material

```java
public class ShopEventListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        qShopAPI.getInstance().handlePurchase(
            Arrays.asList("&bPurchased item for %cost% coins!"),
            event,
            Material.DIAMOND
        );
    }
}
```

#### Example Handling Purchase with Balance

```java
public class ShopEventListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        ShopAdapter adapter = getShopAdapterForPlayer(player);
        if (adapter == null) return;

        qShopAPI.getInstance().handlePurchase(
            adapter,
            Arrays.asList("&bPurchased item for %cost% coins!"),
            event
        );
    }

    private ShopAdapter getShopAdapterForPlayer(Player player) {
        // Implement logic to retrieve the ShopAdapter instance for the player
        return null; // Placeholder for actual implementation
    }
}
```

## Contributing

Feel free to contribute to the project by submitting pull requests or opening issues on GitHub.

---

Feel free to adjust the examples based on your specific implementation and usage!
