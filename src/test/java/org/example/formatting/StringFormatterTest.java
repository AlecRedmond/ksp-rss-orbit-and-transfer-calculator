package org.example.formatting;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringFormatterTest {
  private double testDouble = 3e-3;
  private String testString = " 5E2km";
  private String testString2 = " -500Mm";
  private String testString3 = " - 500.00 km";
  private LinkedList<String> testStrings = new LinkedList<>(List.of(testString,testString2,testString3));


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
    assertEquals(myDouble,500000);
    assertEquals(myDouble2,-500e6);
    assertEquals(myDouble3,-500e3);
  }

  @Test
  void removeWhiteSpace() {
    LinkedList<String> newLinkedList = StringFormatter.removeWhiteSpace(testStrings);
    assertEquals(newLinkedList.get(0),"5E2km");
    assertEquals(newLinkedList.get(1),"-500Mm");
    assertEquals(newLinkedList.get(2),"-500.00km");
  }
}
