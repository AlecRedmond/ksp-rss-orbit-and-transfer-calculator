package org.example.gui.application;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.equations.method.KeplerianMethod;
import org.example.formatting.StringUnitParser;
import org.example.gui.method.VVDataElement;

import java.util.*;

public class VisVivaGui extends Application {

  GridPane gridPane = new GridPane();
  KeplerianMethod keplerianMethodLeft;
  KeplerianMethod keplerianMethodRight;

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("GridPlane Experiment");

    LinkedList<String> keplerElements =
        new LinkedList<>(List.of("Periapsis", "Apoapsis", "Eccentricity", "Semi-Major Axis"));

    int leftHoldButtonPosition = 0;
    int leftTextFieldPosition = 0;
    int rightHoldButtonPosition = 0;
    int rightTextFieldPosition = 0;

    ArrayList<ArrayList<Node>> myNodes = new ArrayList<>();
    ArrayList<Node> innerNodes;
    for (String keplerElement : keplerElements) {
      innerNodes = new ArrayList<>();
      innerNodes.add(new Label(keplerElement));
      leftHoldButtonPosition = innerNodes.size();
      innerNodes.add(new ToggleButton("Hold"));
      leftTextFieldPosition = innerNodes.size();
      innerNodes.add(new TextField(""));
      rightTextFieldPosition = innerNodes.size();
      innerNodes.add(new TextField(""));
      rightHoldButtonPosition = innerNodes.size();
      innerNodes.add(new ToggleButton("Hold"));
      myNodes.add(innerNodes);
    }

    int innerNodeSize = myNodes.get(0).size();

    for (int y = 0; y < myNodes.size(); y++) {
      for (int x = 0; x < innerNodeSize; x++) {
        this.gridPane.add(myNodes.get(y).get(x), x, y);
      }
    }

    this.gridPane.setHgap(10);
    this.gridPane.setVgap(10);

    Button calculateEAndSma = new Button("Calculate e and SMA");
    Button clearAllFields = new Button("Clear All Fields");

    int finalLeftHoldButtonPosition = leftHoldButtonPosition;
    int finalRightHoldButtonPosition = rightHoldButtonPosition;
    int finalLeftTextFieldPosition = leftTextFieldPosition;
    int finalRightTextFieldPosition = rightTextFieldPosition;

    calculateEAndSma.setOnAction(
        action -> {
          ArrayList<VVDataElement> vvDataElementsLeft = new ArrayList<>();
          ArrayList<VVDataElement> vvDataElementsRight = new ArrayList<>();
          for (int i = 0; i < myNodes.size(); i++) {
            String parameterName = String.valueOf(keplerElements.get(i));
            boolean isLeftSelected =
                ((ToggleButton) myNodes.get(i).get(finalLeftHoldButtonPosition)).isSelected();
            boolean isRightSelected =
                ((ToggleButton) myNodes.get(i).get(finalRightHoldButtonPosition)).isSelected();
            TextField leftTextField = (TextField) myNodes.get(i).get(finalLeftTextFieldPosition);
            String leftData = leftTextField.getText();
            TextField rightTextField = (TextField) myNodes.get(i).get(finalRightTextFieldPosition);
            String rightData = rightTextField.getText();
            vvDataElementsLeft.add(new VVDataElement(parameterName, isLeftSelected, leftData));
            vvDataElementsRight.add(new VVDataElement(parameterName, isRightSelected, rightData));
          }

          this.keplerianMethodLeft = new KeplerianMethod();
          for (VVDataElement element : vvDataElementsLeft) {
            element.setData();
            this.keplerianMethodLeft.setFromDataElement(element);
          }

          this.keplerianMethodRight = new KeplerianMethod();
          for (VVDataElement element : vvDataElementsRight) {
            element.setData();
            this.keplerianMethodRight.setFromDataElement(element);
          }

          this.keplerianMethodLeft.calculateMissing();
          this.keplerianMethodRight.calculateMissing();

          for (int i = 0; i < vvDataElementsLeft.size(); i++) {
            String parameterNameLeft = vvDataElementsLeft.get(i).getParameterName().toLowerCase();
            double dataLeft = this.keplerianMethodLeft.getFromParameterName(parameterNameLeft);
            String setNameLeft = parseToString(dataLeft, parameterNameLeft);
            TextField leftTextField = (TextField) myNodes.get(i).get(finalLeftTextFieldPosition);
            leftTextField.setText(setNameLeft);

            String parameterNameRight = vvDataElementsRight.get(i).getParameterName().toLowerCase();
            double dataRight = this.keplerianMethodRight.getFromParameterName(parameterNameRight);
            String setNameRight = parseToString(dataRight, parameterNameRight);
            TextField rightTextField = (TextField) myNodes.get(i).get(finalRightTextFieldPosition);
            rightTextField.setText(setNameRight);
          }
        });

    clearAllFields.setOnAction(
        actionEvent -> {
          for (ArrayList<Node> element : myNodes) {
            for (Node subElement : element) {
              try {
                ((TextField) subElement).setText("");
              } catch (Exception ignored) {
              }
            }
          }
        });

    VBox vBox = new VBox(calculateEAndSma, clearAllFields);
    vBox.setSpacing(10);
    HBox hBox = new HBox(this.gridPane, vBox);

    Scene scene = new Scene(hBox, 640, 480);
    stage.setScene(scene);
    stage.show();
  }

  public String parseToString(double myData, String parameterName) {
    return StringUnitParser.doubleToString(myData, parameterName);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
