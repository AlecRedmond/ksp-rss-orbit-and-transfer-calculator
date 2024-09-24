package org.example.formatting;

import java.util.Collections;
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
    splitStringsList.removeAll(Collections.singleton(""));

    splitStringsList = removeWhiteSpace(splitStringsList);

    if (splitStringsList.get(0).equals("-")) {
      makeNegative = true;
      splitStringsList.removeFirst();
    }

    if (splitStringsList.get(0).matches("-?[0-9]*\\w+")) {
      StringBuilder sb = new StringBuilder(splitStringsList.get(0));
      StringBuilder sb2 = new StringBuilder();
      while (String.valueOf(sb.charAt(sb.length() - 1)).matches("[^0-9]")) {
        sb2.insert(0, sb.charAt(sb.length() - 1));
        sb.deleteCharAt(sb.length() - 1);
      }
      if (!sb2.isEmpty()) {
        splitStringsList = new LinkedList<>();
        splitStringsList.add(0, sb.toString());
        splitStringsList.add(1, sb2.toString());
      }
    }

    for (String element : splitStringsList) {
      try {
        myDouble = Double.parseDouble(element);
        if (makeNegative) {
          myDouble = 0 - myDouble;
        }
        splitStringsList.remove(element);
        break;
      } catch (Exception e) {
      }
    }

    char mod = '-';

    for (String element : splitStringsList) {
      if (element.matches("[kMGTmμnp]\\w+")) {
        mod = element.charAt(0);
      }
    }

    double order = getOrder(mod);

    return myDouble * order;
  }

  public static LinkedList<String> removeWhiteSpace(LinkedList<String> splitStringsList) {
    StringBuilder sb;
    LinkedList<String> workingList = new LinkedList<>();
    for (String element : splitStringsList) {
      sb = new StringBuilder(element);
      for (int i = 0; i < sb.length(); i++) {
        if (String.valueOf(sb.charAt(i)).equals(" ")) {
          sb.deleteCharAt(i);
        }
      }
      workingList.add(sb.toString());
    }
    return workingList;
  }

  private static double getOrder(char mod) {
    double order;
    switch (mod) {
      case 'k' -> order = 1e3;
      case 'M' -> order = 1e6;
      case 'G' -> order = 1e9;
      case 'T' -> order = 1e12;
      case 'm' -> order = 1e-3;
      case 'μ' -> order = 1e-6;
      case 'n' -> order = 1e-9;
      case 'p' -> order = 1e-12;
      default -> order = 1;
    }
    return order;
  }
}
