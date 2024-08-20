package dev.m7wq.qshopapi.object;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;




public class Lore {

    /**
     * A list of strings representing the lore lines.
     * Each string in the list can contain placeholders and formatting codes 
     * that will be processed and displayed to the player.
     */
    private List<String> lore;

    private Lore(List<String> lore){
        this.lore = lore;
    }

    /**
     * Initializes the Lore object with a list of lore lines.
     * 
     * @param list a list of strings that represent the lore lines. 
     *             These lines may include placeholders and formatting codes.
     * @return the current instance of the Lore object with the specified lore lines.
     */
    public static Lore of(List<String> list) {
        return new Lore(list);
    }

    /**
     * Processes the lore lines by replacing placeholders with actual values 
     * and applying color codes.
     * 
     * @param item the item whose details are used for replacing placeholders, 
     *             such as %cost% with the item's cost.
     * @return a list of formatted strings where placeholders have been replaced 
     *         and color codes have been applied.
     */
    public List<String> getList(Item item) {
        List<String> loreList = new ArrayList<>();
        
        for (String string : lore) {
            loreList.add(
                ChatColor.translateAlternateColorCodes('&', 
                string.replace("%cost%", String.valueOf(item.getCost()))
                )
            );
        }

        return loreList;
    }
}


