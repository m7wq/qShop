package dev.m7wq.qshopapi.object;

import com.avaje.ebean.validation.NotNull;

public class Contents {

    /**
     * Array of items contained within the Contents object.
     * This array should never be null, ensuring that the contents are always defined.
     */
    @NotNull private Item[] items;

    /**
     * Private constructor to initialize the Contents object with an array of items.
     * 
     * @param items the array of Item objects that populate the contents.
     *              This constructor is private to enforce the use of the static factory method for creation.
     */
    private Contents(Item[] items) {
        this.items = items;
    }

    /**
     * Retrieves the array of items contained in this Contents object.
     * 
     * @return a non-null array of Item objects. The array represents the items included in the contents.
     */
    public @NotNull Item[] getItems() {
        return items;
    }

    /**
     * Static factory method to create a new Contents object with the given items.
     * 
     * @param items the array of Item objects to be included in the new Contents instance.
     *              This array should be non-null and contain the items that make up the contents.
     * @return a new instance of Contents initialized with the specified items.
     */
    public static Contents of(Item[] items) {
        return new Contents(items);
    }
}

    


