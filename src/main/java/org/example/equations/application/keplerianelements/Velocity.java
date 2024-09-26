package org.example.equations.application.keplerianelements;

import org.example.formatting.StringUnitParser;

public class Velocity extends KeplerElement<Double> {
  public double data;

  @Override
  public void set(Double velocity) {
    this.data = velocity;
  }

  @Override
  public Double get() {
    return this.data;
  }

  @Override
  public String getAsString() {
    return StringUnitParser.doubleToString(this.data,unitSI(),false,0,displayName());
  }

  @Override
  public void setFromString(String string) {
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    this.data = dataFromString;
  }

  @Override
  public String displayName() {
    return "Velocity";
  }

  @Override
  public String unitSI() {
    return "m/s";
  }
}
