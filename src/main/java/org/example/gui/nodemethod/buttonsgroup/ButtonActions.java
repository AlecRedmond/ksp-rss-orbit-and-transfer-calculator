package org.example.gui.nodemethod.buttonsgroup;

import java.util.HashMap;
import java.util.Map;

import org.example.controller.CourierController;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.gui.nodegroups.buttonsgroup.GuiButtons;
import org.example.gui.nodemethod.OrbitalGridPane;

public class ButtonActions {

  public static void allButtons() {
    calculateButton();
    clearButton();
    calulateWithInclinationButton();
    toggleButton();
  }

    private static void calulateWithInclinationButton() {

      GuiButtons.getInclinationChangeButton().setOnAction(actionEvent -> {
          String inclinationString = GuiButtons.getInclinationField().getText();
          double inclinationDegs = Double.parseDouble(inclinationString);
          CourierController.calculateInclinationChange(inclinationDegs);
      });
    }

    private static void toggleButton() {

    }


  public static void calculateButton() {
    GuiButtons.getCalculateButton()
        .setOnAction(
            actionEvent -> {
              CourierController.calculateVisViva();
            });
  }

  public static void clearButton(){
      GuiButtons.getClearButton().setOnAction(
              actionEvent -> CourierController.clearAll()
      );
  }
}
