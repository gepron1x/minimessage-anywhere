package me.gepron1x.minimessageanywhere;

public interface Processor<T> {
    T handle(T input);
    default T reset(T input) {return input; }
}
