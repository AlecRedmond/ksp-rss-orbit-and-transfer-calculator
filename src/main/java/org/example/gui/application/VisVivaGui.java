package org.example.gui.application;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.equations.application.keplerianelements.*;
import org.example.equations.method.KeplerianMethod;

import static java.util.Arrays.stream;

public class VisVivaGui extends Application {

  Stage stage = new Stage();
  GridPane gridPane = new GridPane();
  KeplerianMethod keplerianMethodLeft = new KeplerianMethod();
  KeplerianMethod keplerianMethodRight = new KeplerianMethod();
  LinkedList<Class> listOfParameterClasses;
  int gridLeftHoldButtonPosition = 0;
  int gridLeftTextFieldPosition = 0;
  int gridRightHoldButtonPosition = 0;
  int gridRightTextFieldPosition = 0;

  ArrayList<ArrayList<Node>> myNodes = new ArrayList<>();
  ArrayList<Node> innerNodes;
  ArrayList<Integer> fieldAndHoldLocations;

  ArrayList<ToggleButton> leftToggleButtonsArray = new ArrayList<>();
  int lastToggledLeftIndex = 0;
  HashMap<Class, Boolean> leftHoldsHashMap = new HashMap<>();
  ArrayList<ToggleButton> rightToggleButtonsArray = new ArrayList<>();
  int lastToggledRightIndex = 0;
  HashMap<Class, Boolean> rightHoldsHashMap = new HashMap<>();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    this.stage = stage;

    // GridPane Initialisation.
    setKeplerElements();
    shapeMyNodesArray();
    setGridFromMyNodes();

    // Hold button checker
    initialiseHoldButtons();

    // Functional Buttons.
    Button calculateEAndSma = new Button("Calculate e and SMA");
    calculateEAndSMAButtonEvent(calculateEAndSma);

    Button clearAllFields = new Button("Clear All Fields");
    clearAllFieldsButtonEvent(clearAllFields);

    Button generateTransfers = new Button("Generate Transfers");

    // SceneBuilding
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

  private void initialiseHoldButtons() {
    for (int i = 0; i < myNodes.size(); i++) {
      this.leftToggleButtonsArray.add(
          (ToggleButton) myNodes.get(i).get(gridLeftHoldButtonPosition));
      this.rightToggleButtonsArray.add(
          (ToggleButton) myNodes.get(i).get(gridRightHoldButtonPosition));
    }

    for (int tb = 0; tb < leftToggleButtonsArray.size(); tb++) {
      int iFinal = tb;
      leftToggleButtonsArray.get(tb).setOnAction(event -> activeHoldCounter(iFinal, true));
    }

    for (int tb = 0; tb < rightToggleButtonsArray.size(); tb++) {
      int iFinal = tb;
      rightToggleButtonsArray.get(tb).setOnAction(event -> activeHoldCounter(iFinal, false));
    }
  }

  private void activeHoldCounter(int tb, boolean wasLeft) {
    Class lastClass = listOfParameterClasses.get(tb);
    List<ToggleButton> actionedList;
    int lastToggled;
    if (wasLeft) {
      actionedList = this.leftToggleButtonsArray;
      lastToggled = this.lastToggledLeftIndex;

    } else {
      actionedList = this.rightToggleButtonsArray;
      lastToggled = this.lastToggledRightIndex;
    }
    boolean clickWasToEnable = actionedList.get(tb).isSelected();

    HashMap<Class, Boolean> toggleState = new HashMap<>();
    for (int i = 0; i < myNodes.size(); i++) {
      toggleState.put(listOfParameterClasses.get(i), actionedList.get(i).isSelected());
    }

    if (lastClass.equals(OrbitalPeriod.class) || lastClass.equals(SemiMajorAxis.class)) {
      Class removeClass = OrbitalPeriod.class;
      if (lastClass.equals(OrbitalPeriod.class)) {
        removeClass = SemiMajorAxis.class;
      }
      if (toggleState.get(removeClass)) {
        int indexToUnset = listOfParameterClasses.indexOf(removeClass);
        actionedList.get(indexToUnset).setSelected(false);
      }
    }

    if (lastClass.equals(Apoapsis.class) || lastClass.equals(Periapsis.class)) {
      Class removeClass = VelocityPeriapsis.class;
      if (lastClass.equals(Periapsis.class)) {
        removeClass = VelocityApoapsis.class;
      }
      if (toggleState.get(removeClass)) {
        int indexToUnset = listOfParameterClasses.indexOf(removeClass);
        actionedList.get(indexToUnset).setSelected(false);
      }
    }

    if (lastClass.equals(Eccentricity.class)) {
      ArrayList<Class> removeClasses = new ArrayList<>();
      removeClasses.add(VelocityPeriapsis.class);
      removeClasses.add(VelocityApoapsis.class);
      int indexToUnset;
      for (Class element : removeClasses) {
        indexToUnset = listOfParameterClasses.indexOf(element);
        actionedList.get(indexToUnset).setSelected(false);
      }
    }

    if (lastClass.equals(VelocityApoapsis.class) || lastClass.equals(VelocityPeriapsis.class)) {
      ArrayList<Class> removeClasses = new ArrayList<>();
      removeClasses.add(Eccentricity.class);
      if (lastClass.equals(VelocityApoapsis.class)) {
        removeClasses.add(Periapsis.class);
        removeClasses.add(VelocityPeriapsis.class);
      } else {
        removeClasses.add(Apoapsis.class);
        removeClasses.add(VelocityApoapsis.class);
      }

      int indexToUnset;
      for (Class element : removeClasses) {
        indexToUnset = listOfParameterClasses.indexOf(element);
        actionedList.get(indexToUnset).setSelected(false);
      }
    }

    long numberHeld = actionedList.stream().filter(ToggleButton::isSelected).count();
    if (numberHeld > 2) {
      IntStream.range(0, actionedList.size())
          .filter(i -> (i != tb && i != lastToggled))
          .mapToObj(actionedList::get)
          .filter(ToggleButton::isSelected)
          .findFirst()
          .get()
          .setSelected(false);
    }

    if (wasLeft) {
      this.lastToggledLeftIndex = tb;
      this.leftHoldsHashMap = toggleState;

    } else {
      this.lastToggledRightIndex = tb;
      this.rightHoldsHashMap = toggleState;
    }
  }

  private void setKeplerElements() {
    listOfParameterClasses = keplerianMethodLeft.getKeplerian().keplerianClassList();
  }

  private void shapeMyNodesArray() {
    try {
      for (Class keplerElement : listOfParameterClasses) {
        innerNodes = new ArrayList<>();

        Object instance = keplerElement.getDeclaredConstructor().newInstance();
        Method method = keplerElement.getMethod("displayName");
        String methodString = (String) method.invoke(instance);

        innerNodes.add(new Label(methodString));

        gridLeftHoldButtonPosition = innerNodes.size();
        innerNodes.add(new ToggleButton("Hold"));

        gridLeftTextFieldPosition = innerNodes.size();
        innerNodes.add(new TextField(""));

        gridRightTextFieldPosition = innerNodes.size();
        innerNodes.add(new TextField(""));

        gridRightHoldButtonPosition = innerNodes.size();
        innerNodes.add(new ToggleButton("Hold"));

        myNodes.add(innerNodes);
      }
    } catch (Exception e) {
      System.out.println(e);
      System.out.println("One of the classes does not have a displayName() method");
      System.out.println("Have you declared a Lombok @NoArgsConstructor?");
    }

    fieldAndHoldLocations =
        new ArrayList<>(
            List.of(
                gridLeftHoldButtonPosition,
                gridRightHoldButtonPosition,
                gridLeftTextFieldPosition,
                gridRightTextFieldPosition));
  }

  private void setGridFromMyNodes() {
    int innerNodeSize = myNodes.get(0).size();

    for (int y = 0; y < myNodes.size(); y++) {
      for (int x = 0; x < innerNodeSize; x++) {
        this.gridPane.add(myNodes.get(y).get(x), x, y);
      }
    }
  }

  private void calculateEAndSMAButtonEvent(Button calculateEAndSma) {
    int finalLeftTextFieldPosition = this.fieldAndHoldLocations.get(2);
    int finalRightTextFieldPosition = this.fieldAndHoldLocations.get(3);

    calculateEAndSma.setOnAction(
        action -> {
          this.keplerianMethodLeft = new KeplerianMethod();
          this.keplerianMethodRight = new KeplerianMethod();

          // Do Logic with the holds

          HashMap<Class, String> leftDataToParse = new HashMap<>();
          for (int i = 0; i < myNodes.size(); i++) {
            if (leftToggleButtonsArray.get(i).isSelected()) {
              leftDataToParse.put(
                  listOfParameterClasses.get(i),
                  ((TextField) myNodes.get(i).get(finalLeftTextFieldPosition)).getText());
            }
          }

          HashMap<Class, String> rightDataToParse = new HashMap<>();
          for (int i = 0; i < myNodes.size(); i++) {
            if (rightToggleButtonsArray.get(i).isSelected()) {
              rightDataToParse.put(
                  listOfParameterClasses.get(i),
                  ((TextField) myNodes.get(i).get(finalRightTextFieldPosition)).getText());
            }
          }

          this.keplerianMethodLeft.setDataToParse(leftDataToParse);
          this.keplerianMethodRight.setDataToParse(rightDataToParse);

          this.keplerianMethodLeft.calculateMissing();
          this.keplerianMethodRight.calculateMissing();

          for (int i = 0; i < this.myNodes.size(); i++) {
            Class currentClass = this.listOfParameterClasses.get(i);
            String leftOutput = this.keplerianMethodLeft.getAsString(currentClass);
            String rightOutput = this.keplerianMethodRight.getAsString(currentClass);
            ((TextField) this.myNodes.get(i).get(finalLeftTextFieldPosition)).setText(leftOutput);
            ((TextField) this.myNodes.get(i).get(finalRightTextFieldPosition)).setText(rightOutput);
          }
        });
  }

  private void clearAllFieldsButtonEvent(Button clearAllFields) {
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
