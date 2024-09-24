package org.example.formatting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StringFormatter {
  public static String doubleToString(double myDouble, String unitSymbol) {
    String myString;
    String magnitudeSymbol;

    int orderOfMagnitudeCounter = 0;
    while (myDouble >= 1000) {
      myDouble = myDouble / 1000;
      orderOfMagnitudeCounter++;
    }
    while (myDouble < 1) {
      myDouble = myDouble * 1000;
      orderOfMagnitudeCounter--;
    }

    switch (orderOfMagnitudeCounter) {
      case 0 -> magnitudeSymbol = "";
      case 1 -> magnitudeSymbol = "k";
      case 2 -> magnitudeSymbol = "M";
      case 3 -> magnitudeSymbol = "G";
      case 4 -> magnitudeSymbol = "T";
      case -1 -> magnitudeSymbol = "m";
      case -2 -> magnitudeSymbol = "μ";
      case -3 -> magnitudeSymbol = "n";
      case -4 -> magnitudeSymbol = "p";
      default -> {
        int scientific = orderOfMagnitudeCounter * 3;
        magnitudeSymbol = String.format("e%02d", scientific);
      }
    }

    myString = String.format("%3.2f %s%s", myDouble, magnitudeSymbol, unitSymbol);

    return myString;
  }

  public static double stringToDouble(String myString) {
    double myDouble = 0;
    boolean makeNegative = false;

    LinkedList<String> splitStringsList = new LinkedList<>(List.of(myString.split(" ")));
    while (splitStringsList.get(0).equals("")) {
      splitStringsList.removeFirst();
    }

    if (splitStringsList.get(0).equals("-")) {
      makeNegative = true;
    }

    for (String element : splitStringsList) {
      try {
        myDouble = Double.parseDouble(element);
        if (makeNegative) {
          myDouble = 0 - myDouble;
        }
        break;
      } catch (Exception e) {
      }
    }

    return myDouble;
  }
}
