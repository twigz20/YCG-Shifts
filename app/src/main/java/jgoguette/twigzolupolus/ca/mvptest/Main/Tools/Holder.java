package jgoguette.twigzolupolus.ca.mvptest.Main.Tools;

/**
 * Created by jerry on 2016-11-06.
 */

public class Holder<T> {
    private T value;

    public Holder(T value) {
        setValue(value);
    }

    public Holder() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}