package dev.m7wq.qshopapi;


import dev.m7wq.qshopapi.data.DataSerializer;
import dev.m7wq.qshopapi.data.DataStorage;
import dev.m7wq.qshopapi.events.InventoryClickListener;
import dev.m7wq.qshopapi.extra.Editable;
import dev.m7wq.qshopapi.listeners.PurchaseListener;
import dev.m7wq.qshopapi.listeners.enums.ItemStatus;
import dev.m7wq.qshopapi.listeners.enums.StatusDisplay;
import dev.m7wq.qshopapi.main.ShopInterface;
import dev.m7wq.qshopapi.reader.ShopReader;
import dev.m7wq.qshopapi.storage.Inputs;
import dev.m7wq.qshopapi.entity.Input;
import dev.m7wq.qshopapi.storage.Edits;
import dev.m7wq.qshopapi.storage.Shops;
import dev.m7wq.qshopapi.utils.ShopUtil;
import dev.m7wq.qshopapi.utils.TextHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.function.Predicate;

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
    @Setter protected DataSerializer serializer;
    protected DataStorage storage;
    @Setter protected PurchaseListener purchaseListener;



    public ShopAPI(Plugin plugin){

        shops = new Shops();
        edits = new Edits();
        inputs = new Inputs();
        reader = new ShopReader();
        storage = new DataStorage();

        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new InventoryClickListener(getShops()),plugin);

        // Built-in edits
        edits.getEditable().put("%status%",(text,item)->{
            ShopInterface shop = shops.getShops().stream().filter(shopInterface -> shopInterface.getItems().contains(item)).findFirst().get();
            ItemStatus status = storage.get(shop.getTitle()).get(item);
            StatusDisplay display = item.getDisplay();

                if (status == ItemStatus.SELECTED)
                    return text.replace("%status%",TextHelper.format(display.selected));
                else if (status == ItemStatus.UN_SELECTED)
                    return text.replace("%status%",TextHelper.format(display.unSelected));
                else
                    return text.replace("%status%", TextHelper.format(display.notPurchased));
        });

        edits.getEditable().put("%price%",(text,item)->text.replace("%price%",String.valueOf(item.getPrice())));
    }

    /**
     * Deserialize Data
     */
    public void enable(){
        getStorage().putAll(getSerializer().deserialize());
    }

    /**
     * Serialize Data
     */
    public void disable(){
        getSerializer().serialize(getStorage());
    }

    /**
     * @Reader handle the annotations processing
     * Read it then add to shops set
     */
    public void registerShop(Object shop){
        ShopInterface shopInterface = getReader().read(shop,shop.getClass(),this,plugin);
        shops.getShops().add(shopInterface);
    }

    /**
     * Register inputs that can be used as values on shop class attributes
     * @Input can be more than a type as (Command, Inventory, more soon)
     */
    public void registerInput(String key,Input input){
        inputs.getInputMap().put(key,input);
    }

    /**
     * @param string if it was found in item's lore or name it applys edit process
     * @param editable Functional Interface return edited string
     */
    public void registerEdit(String string, Editable editable){
        edits.getEditable().put(string, editable);
    }

    public Inventory getShop(Predicate<ShopInterface> predicate) {
        for (ShopInterface shop : shops.getShops()) {
            if (predicate.test(shop))
                return ShopUtil.toInventory(shop,edits);
        }

        return null;
    }

}


