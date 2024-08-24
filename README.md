
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
    <version>beta-1.6</version>
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
    implementation 'com.github.m7wq:qShop:beta-1.6'
}
```

## Usage

### Setup in Main Class

Initialize and load the API in your plugin's main class:

```java
public class Plugin extends JavaPlugin{
    @Override
    public void onEnable(){
        // Initialize the instance
        QShopAPI.load();
    }
}
```

### Implementing `ShopAdapter`

Create a custom shop by implementing `ShopAdapter`:

```java
public class MyCustomShop extends ShopAdapter{

    @Size(size = 9)
    @Title(title = "Swords Shop")
    @Lore(lore = {"Im Sword!","&cBUY ME, To slash Your Enemies","Cost: %cost%"})
    @Override
    public Shop getShop() {
   
        return super.getShop(); // or new Shop()
    }

    @Override
    public @NotNull Contents getContents(){
        return Contents.of(
            new Item[]{
                new Item(new ItemStack(Material.COOKIE), 5, 1)
            }
        );
    }
}
```

### Implementing `PaymentAdapter`

Create a custom payment methods to handle purchasing with data or config balance

```java

public class MyPayment implements PaymentAdapter {

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

        ShopAdapter shopAdapter = new MyCustomShop();
  

        Inventory shopInventory = qShopAPI.getInstance().handleShopGui(shopAdapter);
        player.openInventory(shopInventory);

        return true;
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
        qShopAPI.getInstance().handlePurchase(
            new Messager().of("&aYou purchased this item!", "&cNo enough balance!")
            new MyPayment(),
            Arrays.asList("&bPurchased for %cost% coins!"),
            event
        );
    }
}
```

## Contributing

Contribute by submitting pull requests or opening issues on GitHub.
Feel free to adjust as needed for your specific implementation!
