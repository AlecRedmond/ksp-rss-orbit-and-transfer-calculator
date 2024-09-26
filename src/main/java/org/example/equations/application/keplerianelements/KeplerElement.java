package org.example.equations.application.keplerianelements;

abstract class KeplerElement<T> {
    public abstract void set(T t);
    public abstract T get();
    public abstract String getAsString();
    public abstract void setFromString(String string);
}
