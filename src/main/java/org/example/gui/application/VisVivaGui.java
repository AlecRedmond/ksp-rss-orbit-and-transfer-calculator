package org.example.gui.application;

import java.lang.reflect.Method;
import java.util.*;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.equations.application.keplerianelements.Apoapsis;
import org.example.equations.application.keplerianelements.Eccentricity;
import org.example.equations.application.keplerianelements.Periapsis;
import org.example.equations.application.keplerianelements.SemiMajorAxis;
import org.example.equations.method.KeplerianMethod;

public class VisVivaGui extends Application {

  Stage stage = new Stage();
  GridPane gridPane = new GridPane();
  KeplerianMethod keplerianMethodLeft;
  KeplerianMethod keplerianMethodRight;
  LinkedList<Class> keplerElements;
  int leftHoldButtonPosition = 0;
  int leftTextFieldPosition = 0;
  int rightHoldButtonPosition = 0;
  int rightTextFieldPosition = 0;

  ArrayList<ArrayList<Node>> myNodes = new ArrayList<>();
  ArrayList<Node> innerNodes;
  ArrayList<Integer> fieldAndHoldLocations;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    this.stage = stage;

    setKeplerElements();
    shapeMyNodesArray();
    setGridFromMyNodes();

    Button calculateEAndSma = new Button("Calculate e and SMA");
    calculateEAndSMAButtonEvent(calculateEAndSma);

    Button clearAllFields = new Button("Clear All Fields");
    clearAllFieldsButtonEvent(clearAllFields);

    Button generateTransfers = new Button("Generate Transfers");



    VBox vBox = new VBox(calculateEAndSma, clearAllFields);
    HBox hBox = new HBox(this.gridPane, vBox);
    Scene scene = new Scene(hBox, 640, 480);

    this.stage.setTitle("GridPlane Experiment");
    this.gridPane.setHgap(10);
    this.gridPane.setVgap(10);
    vBox.setSpacing(10);

    stage.setScene(scene);
    stage.show();
  }

  private void setKeplerElements() {
    keplerElements =
        new LinkedList<>(
            List.of(Periapsis.class, Apoapsis.class, Eccentricity.class, SemiMajorAxis.class));
  }

  private void shapeMyNodesArray() {
    try {
      for (Class keplerElement : keplerElements) {
        innerNodes = new ArrayList<>();

        Object instance = keplerElement.getDeclaredConstructor().newInstance();
        Method method = keplerElement.getMethod("displayName");
        String methodString = (String) method.invoke(instance);

        innerNodes.add(new Label(methodString));

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
    } catch (Exception e) {
      System.out.println(e);
      System.out.println("One of the classes does not have a displayName() method");
    }

    fieldAndHoldLocations =
        new ArrayList<>(
            List.of(
                leftHoldButtonPosition,
                rightHoldButtonPosition,
                leftTextFieldPosition,
                rightTextFieldPosition));
  }

  private void setGridFromMyNodes() {
    int innerNodeSize = myNodes.get(0).size();

    for (int y = 0; y < myNodes.size(); y++) {
      for (int x = 0; x < innerNodeSize; x++) {
        this.gridPane.add(myNodes.get(y).get(x), x, y);
      }
    }
  }

  private void calculateEAndSMAButtonEvent(
      Button calculateEAndSma) {
    int finalLeftHoldButtonPosition = this.fieldAndHoldLocations.get(0);
    int finalRightHoldButtonPosition = this.fieldAndHoldLocations.get(1);
    int finalLeftTextFieldPosition = this.fieldAndHoldLocations.get(2);
    int finalRightTextFieldPosition = this.fieldAndHoldLocations.get(3);

    calculateEAndSma.setOnAction(
        action -> {
          this.keplerianMethodLeft = new KeplerianMethod();
          this.keplerianMethodRight = new KeplerianMethod();

          // first set the holds
          for (int i = 0; i < this.myNodes.size(); i++) {
            Class currentClass = this.keplerElements.get(i);
            boolean leftHoldSelected =
                ((ToggleButton) this.myNodes.get(i).get(finalLeftHoldButtonPosition)).isSelected();
            this.keplerianMethodLeft.setHold(leftHoldSelected, currentClass);
            boolean rightHoldSelected =
                ((ToggleButton) this.myNodes.get(i).get(finalRightHoldButtonPosition)).isSelected();
            this.keplerianMethodRight.setHold(rightHoldSelected, currentClass);
            // Parse Data if holds are true
            if (leftHoldSelected) {
              String string =
                  ((TextField) this.myNodes.get(i).get(finalLeftTextFieldPosition)).getText();
              this.keplerianMethodLeft.setFromString(string, currentClass);
            }
            if (rightHoldSelected) {
              String string =
                  ((TextField) this.myNodes.get(i).get(finalRightTextFieldPosition)).getText();
              this.keplerianMethodRight.setFromString(string, currentClass);
            }
          }

          this.keplerianMethodLeft.calculateMissing();
          this.keplerianMethodRight.calculateMissing();

          for (int i = 0; i < this.myNodes.size(); i++) {
            Class currentClass = this.keplerElements.get(i);
            String leftOutput = this.keplerianMethodLeft.getAsString(currentClass);
            String rightOutput = this.keplerianMethodRight.getAsString(currentClass);
            ((TextField) this.myNodes.get(i).get(finalLeftTextFieldPosition)).setText(leftOutput);
            ((TextField) this.myNodes.get(i).get(finalRightTextFieldPosition)).setText(rightOutput);
          }
        });
  }

  private void clearAllFieldsButtonEvent(
      Button clearAllFields) {
    clearAllFields.setOnAction(
        actionEvent -> {
          for (ArrayList<Node> element : this.myNodes) {
            for (Node subElement : element) {
              try {
                ((TextField) subElement).setText("");
              } catch (Exception ignored) {
              }
            }
          }
        });
  }
}
