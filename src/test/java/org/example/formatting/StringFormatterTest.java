package org.example.formatting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringFormatterTest {
  private double testDouble = 3e-3;
  private String testString = "500.00 km";
  private String testString2 = "500 km";
  private String testString3 = " - 500.00 km";


  @Test
  void testDoubleToString() {
    String testDoubleString = StringFormatter.doubleToString(testDouble,"m");
    assertEquals(testDoubleString,"3.00 mm");

  }

  @Test
  void testStringToDouble() {
    double myDouble = StringFormatter.stringToDouble(testString);
    double myDouble2 = StringFormatter.stringToDouble(testString2);
    double myDouble3 = StringFormatter.stringToDouble(testString3);
    assertEquals(myDouble,500);
    assertEquals(myDouble2,500);
    assertEquals(myDouble3,-500);
  }
}
