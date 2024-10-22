package org.example.stringformatting;

import org.example.equations.application.keplerianelements.OrbitalPeriod;

public class StringUnitParser {
  static int orderOfMagnitudeCounter = 0;
  static int leadingSpace = 3;
  static int decimalPlaces = 0;
  static double myDouble;
  private static boolean commaSplit = false;

  public static String doubleToString(
      double myDouble,
      String unitSI,
      boolean formatOrderOfMagnitude,
      int decimalPlaces,
      String args) {
    String myString;
    String magnitudeSymbol = "";
    String formattingString;
    StringUnitParser.decimalPlaces = decimalPlaces;
    StringUnitParser.myDouble = myDouble;

    if (formatOrderOfMagnitude) {
      StringUnitParser.myDouble = stripOrderOfMagnitude(StringUnitParser.myDouble);
      magnitudeSymbol = getMagnitudeSymbol();
    }

    if (args.equals(new OrbitalPeriod().displayName())) {
      return doubleToTimeString(myDouble);

    } else {
      parseMyArgs(args);
      formattingString = buildString(StringUnitParser.decimalPlaces);

      myString =
          String.format(formattingString, StringUnitParser.myDouble, magnitudeSymbol, unitSI);
    }

    return myString;
  }

  private static String doubleToTimeString(double myDouble) {
    double seconds;
    int minutes;
    int hours;
    int days;

    int myWorkingInt;

    seconds = myDouble % 60;

    myWorkingInt = (int) myDouble;
    myWorkingInt -= (myWorkingInt % 60);
    myWorkingInt = myWorkingInt / 60;
    if(String.format("%04.1f", seconds).equals("60.0")){
      seconds = 0;
      myWorkingInt++;
    }

    minutes = myWorkingInt % 60;
    myWorkingInt -= minutes;
    myWorkingInt = myWorkingInt / 60;

    hours = myWorkingInt % 24;
    myWorkingInt -= hours;
    myWorkingInt = myWorkingInt / 24;

    if (myWorkingInt == 0) {
      return String.format("%02d", hours)
          + ":"
          + String.format("%02d", minutes)
          + ":"
          + String.format("%04.1f", seconds);
    } else {
      days = myWorkingInt;
      return String.format("%02d", days)
          + ":"
          + String.format("%02d", hours)
          + ":"
          + String.format("%02d", minutes)
          + ":"
          + String.format("%04.1f", seconds);
    }
  }

  private static String buildString(int decimalPlaces) {
    String formattingString;
    StringBuilder sb = new StringBuilder();
    sb.append("%");
    if (commaSplit) {
      sb.append(",");
    }
    sb.append(leadingSpace);
    sb.append(".");
    sb.append(decimalPlaces);
    sb.append("f %s%s");
    formattingString = sb.toString();
    return formattingString;
  }

  private static void parseMyArgs(String args) {
    args = args.toLowerCase();
    switch (args) {
      case "eccentricity" -> leadingSpace = 1;
      case "apsis" -> {
        if (orderOfMagnitudeCounter < 2 && orderOfMagnitudeCounter > -2) {
          decimalPlaces = 0;
        } else {
          decimalPlaces = 3;
        }
      }
      case "velocity" -> {
        if (Math.abs(myDouble) < 1000) {
          decimalPlaces = 2;
          if (Math.abs(myDouble) < 100) {
            leadingSpace = 2;
          }
        } else if (Math.abs(myDouble) >= 1000) {
          commaSplit = true;
        }
      }
    }
  }

  private static String getMagnitudeSymbol() {
    String magnitudeSymbol;

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

    return magnitudeSymbol;
  }

  private static double stripOrderOfMagnitude(double myDouble) {
    orderOfMagnitudeCounter = 0;
    while (Math.abs(myDouble) >= 1000) {
      myDouble = myDouble / 1000;
      orderOfMagnitudeCounter++;
    }
    while (Math.abs(myDouble) < 1 && myDouble != 0) {
      myDouble = myDouble * 1000;
      orderOfMagnitudeCounter--;
    }
    return myDouble;
  }

  public static double stringToDouble(String myString) {
    double myDouble = 0;
    String numericalElement;
    String magnitudeElement = "";

    String strippedString = removeWhiteSpace(myString);

    StringBuilder sbNumerical = new StringBuilder(strippedString);
    StringBuilder sbMagnitude = new StringBuilder();

    boolean containsColon = false;
    for (int i = 0; i < sbNumerical.length(); i++) {
      if (String.valueOf(sbNumerical.charAt(i)).matches(":")) {
        containsColon = true;
        break;
      }
    }

    if (containsColon) {
      return timeStringToDouble(myString);
    }

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

  private static double timeStringToDouble(String myString) {
    myDouble = 0;
    int countingIndex = 0;
    String[] splitString = myString.split(":");
    if (splitString.length == 4) {
      myDouble += (Double.parseDouble(splitString[countingIndex])) * (24 * 60 * 60);
      countingIndex++;
    }
    myDouble += (Double.parseDouble(splitString[countingIndex])) * (60 * 60);
    countingIndex++;
    myDouble += (Double.parseDouble(splitString[countingIndex])) * (60);
    countingIndex++;
    myDouble += (Double.parseDouble(splitString[countingIndex]));
    return myDouble;
  }

  public static String removeWhiteSpace(String string) {
    StringBuilder sb;
    String newString;
    sb = new StringBuilder(string);
    for (int i = 0; i < sb.length(); i++) {
      if (String.valueOf(sb.charAt(i)).equals(" ") || String.valueOf(sb.charAt(i)).equals(",")) {
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
