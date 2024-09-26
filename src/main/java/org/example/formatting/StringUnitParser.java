package org.example.formatting;

public class StringUnitParser {
  public static String doubleToString(double myDouble, String parameterName) {
    String myString;
    String magnitudeSymbol;

    if (parameterName.toLowerCase().equals("eccentricity")) {
      myString = String.format("%.3f", myDouble);
      return myString;
    }

    int orderOfMagnitudeCounter = 0;
    while (Math.abs(myDouble) >= 1000) {
      myDouble = myDouble / 1000;
      orderOfMagnitudeCounter++;
    }
    while (Math.abs(myDouble) < 1 && myDouble != 0) {
      myDouble = myDouble * 1000;
      orderOfMagnitudeCounter--;
    }

    if (!parameterName.isEmpty()) {
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
    } else {
      int scientific = orderOfMagnitudeCounter * 3;
      magnitudeSymbol = String.format("e%02d", scientific);
    }

    String unitSymbol = findUnitSymbol(parameterName.toLowerCase());

    myString = String.format("%3.2f %s%s", myDouble, magnitudeSymbol, unitSymbol);

    return myString;
  }

  public static String findUnitSymbol(String unitType) {
    unitType = unitType.toLowerCase();
    return UnitsForElements.ELEMENTS.getOrDefault(unitType, "");
  }

  public static double stringToDouble(String myString) {
    double myDouble = 0;
    String numericalElement;
    String magnitudeElement = "";

    String strippedString = removeWhiteSpace(myString);

    StringBuilder sbNumerical = new StringBuilder(strippedString);
    StringBuilder sbMagnitude = new StringBuilder();
    while (String.valueOf(sbNumerical.charAt(sbNumerical.length() - 1)).matches("[^0-9]")) {
      sbMagnitude.insert(0, sbNumerical.charAt(sbNumerical.length() - 1));
      sbNumerical.deleteCharAt(sbNumerical.length() - 1);
    }
    numericalElement = sbNumerical.toString();
    if (!sbMagnitude.isEmpty()) {
      magnitudeElement = sbMagnitude.toString();
    }

    try {
      myDouble = Double.parseDouble(numericalElement);

    } catch (Exception ignored) {
    }

    char mod = '-';

    if (magnitudeElement.matches("[kMGTmμnp]\\w+")) {
      mod = magnitudeElement.charAt(0);
    }

    double order = getOrder(mod);

    return myDouble * order;
  }

  public static String removeWhiteSpace(String string) {
    StringBuilder sb;
    String newString;
    sb = new StringBuilder(string);
    for (int i = 0; i < sb.length(); i++) {
      if (String.valueOf(sb.charAt(i)).equals(" ")) {
        sb.deleteCharAt(i);
        i--;
      }
    }
    newString = sb.toString();
    return newString;
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
