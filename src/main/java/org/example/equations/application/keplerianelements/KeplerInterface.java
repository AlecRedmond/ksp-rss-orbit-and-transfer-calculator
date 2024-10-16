package org.example.equations.application.keplerianelements;

public interface KeplerInterface<T> {

  void set(T data);

  T get();

  String getAsString();

  void setFromString(String string);

  String displayName();

  String unitSI();
}
