package org.example.formatting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUnitParserTest {
  String string = "PeriApsis";
  String doubleString = "5";

  @Test
  void testFindUnitSymbol() {
    String unitSymbol = StringUnitParser.findUnitSymbol(string);
    assertEquals("m",unitSymbol);
  }

  @Test
  void stringToDoubleTest() {
    double myDouble = StringUnitParser.stringToDouble(doubleString);
    assertEquals(5.0,myDouble);
  }
}
