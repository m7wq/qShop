package dev.m7wq.qshopapi.base;

import org.bukkit.entity.Player;

public interface PaymentAdapter {

    /**
     * Deducts a specified amount from the player's balance.
     *
     * @param player the player from whom the balance will be removed.
     * @param amount the amount to be deducted from the player's balance.
     *
     * This method should handle the removal of balance, which can be currency, points,
     * or other in-game assets. Implementations should ensure proper handling of
     * the player's balance, including checking if the player has sufficient funds.
     */
    void removeBalance(Player player, int amount);

    /**
     * Retrieves the current balance of the player.
     *
     * @param player the player whose balance is being queried.
     * @return an integer representing the player's current balance.
     *
     * This method provides the means to check how much currency or points a player has available.
     * The balance can be used to determine if a player can afford to purchase items from the shop.
     */
    int getBalance(Player player);
}
