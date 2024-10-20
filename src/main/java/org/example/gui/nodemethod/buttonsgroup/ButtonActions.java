package org.example.gui.nodemethod.buttonsgroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.example.controller.CourierController;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.gui.nodegroups.buttonsgroup.GuiButtons;
import org.example.gui.nodemethod.OrbitalGridPane;

public class ButtonActions {

  public static void allButtons() {
    debugButton();
    calculateButton();
    clearButton();
    toggleButton();
  }

    private static void toggleButton() {

    }

    public static void debugButton() {
    GuiButtons.getDeBugButton()
        .setOnAction(
            actionEvent -> {
              HashMap<Kepler.KeplerEnums, Boolean> currentHolds = OrbitalGridPane.getHolds(true);
              for (Map.Entry<Kepler.KeplerEnums, Boolean> entry : currentHolds.entrySet()) {
                System.out.println(entry);
              }
            });
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
