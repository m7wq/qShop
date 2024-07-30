package dev.m7wq.qshopapi.messages;

import com.avaje.ebean.validation.NotNull;

public class Messager {

    /**
     * The message to be displayed when a purchase is successful.
     * This string should not be null.
     */
    @NotNull private String purchasedMessage;

    /**
     * The message to be displayed when the user does not have enough balance.
     * This string should not be null.
     */
    @NotNull private String noEnoughBalanceMessage;

    /**
     * Initializes the Messager instance with the specified messages.
     * 
     * @param purchasedMessage the message to set when a purchase is successful.
     * @param notEnoughBalanceMessage the message to set when the user does not have enough balance.
     * @return the current instance of Messager.
     */
    public Messager of(String purchasedMessage, String notEnoughBalanceMessage) {
        this.purchasedMessage = purchasedMessage;
        this.noEnoughBalanceMessage = notEnoughBalanceMessage;
        return this;
    }

    /**
     * Sets the purchased message.
     * 
     * @param purchasedMessage the new message to be displayed upon successful purchase.
     */
    public void setPurchasedMessage(String purchasedMessage) {
        this.purchasedMessage = purchasedMessage;
    }

    /**
     * Sets the message to be displayed when the user does not have enough balance.
     * 
     * @param notEnoughBalanceMessage the new message for insufficient balance.
     */
    public void setNoEnoughBalanceMessage(String notEnoughBalanceMessage) {
        this.noEnoughBalanceMessage = notEnoughBalanceMessage;
    }

    /**
     * Retrieves the message to be displayed when a purchase is successful.
     * 
     * @return the purchased message.
     */
    public String getPurchasedMessage() {
        return purchasedMessage;
    }

    /**
     * Retrieves the message to be displayed when the user does not have enough balance.
     * 
     * @return the message for insufficient balance.
     */
    public String getNoEnoughBalanceMessage() {
        return noEnoughBalanceMessage;
    }
}


    


