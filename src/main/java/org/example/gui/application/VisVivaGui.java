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

  @Override
  public void start(Stage stage) throws Exception {
    this.stage = stage;
    buildScene();

    LinkedList<Class> keplerElements =
        new LinkedList<>(
            List.of(Periapsis.class, Apoapsis.class, Eccentricity.class, SemiMajorAxis.class));

    int leftHoldButtonPosition = 0;
    int leftTextFieldPosition = 0;
    int rightHoldButtonPosition = 0;
    int rightTextFieldPosition = 0;

    ArrayList<ArrayList<Node>> myNodes = new ArrayList<>();
    ArrayList<Node> innerNodes;
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

    ArrayList<Integer> fieldLocations =
        new ArrayList<>(
            List.of(
                leftHoldButtonPosition,
                rightHoldButtonPosition,
                leftTextFieldPosition,
                rightTextFieldPosition));

    shapeGrid(myNodes);

    this.gridPane.setHgap(10);
    this.gridPane.setVgap(10);

    Button calculateEAndSma = new Button("Calculate e and SMA");
    Button clearAllFields = new Button("Clear All Fields");

    calculateEAndSMAButtonEvent(fieldLocations, calculateEAndSma, myNodes, keplerElements);

    clearAllFieldsButton(clearAllFields, myNodes);

    VBox vBox = new VBox(calculateEAndSma, clearAllFields);
    vBox.setSpacing(10);
    HBox hBox = new HBox(this.gridPane, vBox);

    Scene scene = new Scene(hBox, 640, 480);
    stage.setScene(scene);
    stage.show();
  }

  private void shapeGrid(ArrayList<ArrayList<Node>> myNodes) {
    int innerNodeSize = myNodes.get(0).size();

    for (int y = 0; y < myNodes.size(); y++) {
      for (int x = 0; x < innerNodeSize; x++) {
        this.gridPane.add(myNodes.get(y).get(x), x, y);
      }
    }
  }

  private void calculateEAndSMAButtonEvent(
      ArrayList<Integer> fieldLocations,
      Button calculateEAndSma,
      ArrayList<ArrayList<Node>> myNodes,
      LinkedList<Class> keplerElements) {
    int finalLeftHoldButtonPosition = fieldLocations.get(0);
    int finalRightHoldButtonPosition = fieldLocations.get(1);
    int finalLeftTextFieldPosition = fieldLocations.get(2);
    int finalRightTextFieldPosition = fieldLocations.get(3);

    calculateEAndSma.setOnAction(
        action -> {
          this.keplerianMethodLeft = new KeplerianMethod();
          this.keplerianMethodRight = new KeplerianMethod();

          // first set the holds
          for (int i = 0; i < myNodes.size(); i++) {
            Class currentClass = keplerElements.get(i);
            boolean leftHoldSelected =
                ((ToggleButton) myNodes.get(i).get(finalLeftHoldButtonPosition)).isSelected();
            this.keplerianMethodLeft.setHold(leftHoldSelected, currentClass);
            boolean rightHoldSelected =
                ((ToggleButton) myNodes.get(i).get(finalRightHoldButtonPosition)).isSelected();
            this.keplerianMethodRight.setHold(rightHoldSelected, currentClass);
            // Parse Data if holds are true
            if (leftHoldSelected) {
              String string =
                  ((TextField) myNodes.get(i).get(finalLeftTextFieldPosition)).getText();
              this.keplerianMethodLeft.setFromString(string, currentClass);
            }
            if (rightHoldSelected) {
              String string =
                  ((TextField) myNodes.get(i).get(finalRightTextFieldPosition)).getText();
              this.keplerianMethodRight.setFromString(string, currentClass);
            }
          }

          this.keplerianMethodLeft.calculateMissing();
          this.keplerianMethodRight.calculateMissing();

          for (int i = 0; i < myNodes.size(); i++) {
            Class currentClass = keplerElements.get(i);
            String leftOutput = this.keplerianMethodLeft.getAsString(currentClass);
            String rightOutput = this.keplerianMethodRight.getAsString(currentClass);
            ((TextField) myNodes.get(i).get(finalLeftTextFieldPosition)).setText(leftOutput);
            ((TextField) myNodes.get(i).get(finalRightTextFieldPosition)).setText(rightOutput);
          }
        });
  }

  private static void clearAllFieldsButton(
      Button clearAllFields, ArrayList<ArrayList<Node>> myNodes) {
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
  }

  public void buildScene() {
    stage.setTitle("GridPlane Experiment");
  }

  public static void main(String[] args) {
    launch(args);
  }
}
