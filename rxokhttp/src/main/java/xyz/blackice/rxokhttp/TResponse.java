package xyz.blackice.rxokhttp;

/**
 * Created by DELL on 11/10/2017.
 */

public class TResponse <T> {

    private T value;

    public TResponse(T t){
        this.value = t;
    }

}
