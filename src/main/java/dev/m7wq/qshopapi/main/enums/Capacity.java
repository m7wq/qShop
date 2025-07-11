package dev.m7wq.qshopapi.main.enums;



import java.util.Arrays;
import java.util.List;

public enum Capacity {
    ONE_ROW(9),
    TWO_ROWS(18),
    THREE_ROWS(27),
    FOUR_ROWS(36),
    FIVE_ROWS(45),
    SIX_ROWS(54);

    private final int size;

    Capacity(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public static Capacity ofSize(int size){
        List<Integer> sizes =  Arrays.asList(9, 18, 27, 36, 45, 54);

        if (!sizes.contains(size))
            throw new IllegalStateException("There's no capacity for size "+size+" ofSize#Capacity");

        for (Capacity capacity : Capacity.values()){
            if (capacity.getSize()==size)
                return capacity;
        }
        return null;
    }
}
