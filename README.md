
# qShop

qShop is a customizable shop plugin for Minecraft servers with a flexible API for various in-game shops.

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
    <version>beta-1.3</version>
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
    implementation 'com.github.m7wq:qShop:beta-1.3'
}
```

## Usage

### Setup in Main Class

Initialize and load the API in your plugin's main class:

```java
public class MyPlugin extends JavaPlugin {

    private qShopAPI shopAPI;

    @Override
    public void onEnable() {
        
         // to initialize the the api instance

        
        QShopAPI.load();
    }
}
```

### Implementing `ShopAdapter`

Create a custom shop by implementing `ShopAdapter`:

```java
public class MyCustomShop implements ShopAdapter {

    @Override
    public @NotNull Contents getContents() {
        return Contents.of(
            new Item(createItem("ItemStack", Arrays.asList("Line1", "Line2"), Material.DIAMOND), 0, 1000),
            new Item(new ItemStack(Material.ELYTRA), 2, 2000)
        );
    }

    @Override
    public @NotNull String getTitle() {
        return "My Custom Shop";
    }

    @Override
    public @NotNull Integer getSize() {
        return 9; // Inventory size
    }

    @Override
    public Lore getLore() {
        return new Lore().of(Arrays.asList("&aBuy this for %cost% coins!"));
    }

    @Override
    public @NotNull Lore getDefaultLore() {
        return new Lore().of(Arrays.asList("&bItem purchased!"));
    }

    @Override
    public void removeBalance(Player player, int amount) {
        // Implement balance removal
    }

    @Override
    public int getBalance(Player player) {
        return 1000; // Example balance
    }
}
```

### Using `handleShopGui`

Create and open a shop GUI with:

```java
public class ShopGuiCommandExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Players only!");
            return false;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("Specify shop name.");
            return false;
        }

        ShopAdapter shopAdapter = getShopAdapterByName(args[0]);
        if (shopAdapter == null) {
            player.sendMessage("Shop not found.");
            return false;
        }

        Inventory shopInventory = qShopAPI.getInstance().handleShopGui(shopAdapter);
        player.openInventory(shopInventory);

        return true;
    }

    private ShopAdapter getShopAdapterByName(String shopName) {
        // Retrieve ShopAdapter by name
        return new MyCustomShop(); // Placeholder
    }
}
```

### Handling Purchases

Process purchases with `handlePurchase`:

#### Material-Based Currency

```java
public class ShopEventListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        qShopAPI.getInstance().handlePurchase(
            new Messager().of("&aPurchase successful!", "&cInsufficient balance!")
            Arrays.asList("&bPurchased for %cost% coins!"),
            event,
            Material.DIAMOND
        );
    }
}
```

#### Balance-Based Currency

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
            new Messager().of("&aYou purchased this item!", &cNo enough balance!")
            adapter,
            Arrays.asList("&bPurchased for %cost% coins!"),
            event
        );
    }

    private ShopAdapter getShopAdapterForPlayer(Player player) {
        // Retrieve ShopAdapter for player
        return null; // Placeholder
    }
}
```

## Contributing

Contribute by submitting pull requests or opening issues on GitHub.
Feel free to adjust as needed for your specific implementation!
