package com.hotel.util;

/**
 * Generic Pair class — demonstrates GENERICS
 * Used to temporarily bind a Customer ID to a Room ID before committing to
 * storage.
 *
 * @param <T> first element type (e.g., Integer for Customer ID)
 * @param <U> second element type (e.g., Integer for Room ID)
 */
public class Pair<T, U> {

    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" + first + " -> " + second + "}";
    }
}
