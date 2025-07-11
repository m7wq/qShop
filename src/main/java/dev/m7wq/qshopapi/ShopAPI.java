package dev.m7wq.qshopapi;


import dev.m7wq.qshopapi.events.InventoryClickListener;
import dev.m7wq.qshopapi.extra.Editable;
import dev.m7wq.qshopapi.main.ShopInterface;
import dev.m7wq.qshopapi.reader.ShopReader;
import dev.m7wq.qshopapi.storage.Inputs;
import dev.m7wq.qshopapi.entity.Input;
import dev.m7wq.qshopapi.storage.Edits;
import dev.m7wq.qshopapi.storage.Shops;
import dev.m7wq.qshopapi.utils.ShopUtil;
import dev.m7wq.qshopapi.utils.TextHelper;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

@Getter
public class ShopAPI {

    /**
     * Initialization
     */
    private Shops shops;
    private Inputs inputs;
    private Edits edits;
    protected ShopReader reader;
    protected Plugin plugin;

    public ShopAPI(Plugin plugin){

        shops = new Shops();
        edits = new Edits();
        inputs = new Inputs();
        reader = new ShopReader();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new InventoryClickListener(getShops()),plugin);

    }

    /**
     * @Reader handle the annotations processing
     * Read it then add to shops set
     */
    public void registerShop(Object shop){
        ShopInterface shopInterface = getReader().read(shop.getClass(),inputs,plugin);
        shops.getShops().add(shopInterface);
    }

    /**
     * Register inputs that can be used as values on shop class attributes
     * @Input can be more than a type as (Command, Inventory, more soon)
     */
    public void registerInput(String key,Input<?> input){
        inputs.getInputMap().put(key,input);
    }

    /**
     * @param string if it was found in item's lore or name it applys edit process
     * @param editable Functional Interface return edited string
     */
    public void registerEdit(String string, Editable editable){
        edits.getEditable().put(string, editable);
    }

    public Inventory getShop(String shopName) {
        for (ShopInterface shop : shops.getShops()) {
            if (TextHelper.format(shop.getTitle()).equalsIgnoreCase(TextHelper.format(shopName)))
                return ShopUtil.toInventory(shop);
        }

        return null;
    }

}


