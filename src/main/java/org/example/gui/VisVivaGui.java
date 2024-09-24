package org.example.gui;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.equations.application.Body;
import org.example.equations.method.KeplerianMethod;
import org.example.formatting.StringUnitParser;

import java.util.*;

public class VisVivaGui extends Application {

  GridPane gridPaneLeft = new GridPane();
  GridPane gridPaneRight = new GridPane();
  KeplerianMethod keplerianMethodLeft;
  KeplerianMethod keplerianMethodRight;

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("GridPlane Experiment");

    ArrayList<TextField> periapsisTF = new ArrayList<>();
    periapsisTF.add(new TextField());
    periapsisTF.add(new TextField());
    Label periapsisLb = new Label("Periapsis altitude (m)");
    ArrayList<TextField> apoapsisTF = new ArrayList<>();
    apoapsisTF.add(new TextField());
    apoapsisTF.add(new TextField());
    Label apoapsisLb = new Label("Apoapsis altitude (m)");
    ArrayList<TextField> eccentricityTF = new ArrayList<>();
    eccentricityTF.add(new TextField());
    eccentricityTF.add(new TextField());
    Label eccentricityLb = new Label("Eccentricity");
    ArrayList<TextField> semiMajorAxisTF = new ArrayList<>();
    semiMajorAxisTF.add(new TextField());
    semiMajorAxisTF.add(new TextField());
    Label semiMajorAxisLb = new Label("SemiMajorAxis");

    LinkedHashMap<Node, ArrayList<TextField>> nodeHashMap = new LinkedHashMap<>();
    nodeHashMap.put(periapsisLb, periapsisTF);
    nodeHashMap.put(apoapsisLb, apoapsisTF);
    nodeHashMap.put(eccentricityLb, eccentricityTF);
    nodeHashMap.put(semiMajorAxisLb, semiMajorAxisTF);

    shapeGridPlane(nodeHashMap);
    this.gridPaneLeft.setHgap(10);
    this.gridPaneLeft.setVgap(10);
    this.gridPaneRight.setHgap(10);
    this.gridPaneRight.setVgap(10);

    Button button = new Button("Calculate e and SMA");

    button.setOnAction(
        action -> {
          double[] periapsisDouble = parseToDouble(periapsisTF);
          double[] apoapsisDouble = parseToDouble(apoapsisTF);
          double[] eccentricityDouble = new double[2];
          double[] semiMajorAxisDouble = new double[2];

          this.keplerianMethodLeft =
              new KeplerianMethod(Body.EARTH, periapsisDouble[0], apoapsisDouble[0], true);
          this.keplerianMethodRight =
              new KeplerianMethod(Body.EARTH, periapsisDouble[1], apoapsisDouble[1], true);

          apoapsisDouble[0] = keplerianMethodLeft.getKeplerian().getApoapsis();
          apoapsisDouble[1] = keplerianMethodRight.getKeplerian().getApoapsis();
          periapsisDouble[0] = keplerianMethodLeft.getKeplerian().getPeriapsis();
          periapsisDouble[1] = keplerianMethodRight.getKeplerian().getPeriapsis();
          eccentricityDouble[0] = keplerianMethodLeft.getKeplerian().getEccentricity();
          eccentricityDouble[1] = keplerianMethodRight.getKeplerian().getEccentricity();
          semiMajorAxisDouble[0] = keplerianMethodLeft.getKeplerian().getSemiMajorAxis();
          semiMajorAxisDouble[1] = keplerianMethodRight.getKeplerian().getSemiMajorAxis();

          periapsisTF.get(0).setText(returnUnits(periapsisDouble[0],"m"));
          periapsisTF.get(1).setText(returnUnits(periapsisDouble[1],"m"));
          apoapsisTF.get(0).setText(returnUnits(apoapsisDouble[0],"m"));
          apoapsisTF.get(1).setText(returnUnits(apoapsisDouble[1],"m"));
          eccentricityTF.get(0).setText(returnUnits(eccentricityDouble[0],"eccentricity"));
          eccentricityTF.get(1).setText(returnUnits(eccentricityDouble[1],"eccentricity"));
          semiMajorAxisTF.get(0).setText(returnUnits(semiMajorAxisDouble[0],"m"));
          semiMajorAxisTF.get(1).setText(returnUnits(semiMajorAxisDouble[1],"m"));
        });

    VBox vBox = new VBox(button);
    HBox hBox = new HBox(this.gridPaneLeft, vBox);

    Scene scene = new Scene(hBox, 640, 480);
    stage.setScene(scene);
    stage.show();
  }

  private void shapeGridPlane(LinkedHashMap<Node, ArrayList<TextField>> nodeHashMap) {
    Label leftKey;
    Label rightKey;

    int row = 0;
    for (Map.Entry<Node, ArrayList<TextField>> entry : nodeHashMap.entrySet()) {
      leftKey = (Label) entry.getKey();

      this.gridPaneLeft.add(leftKey, 0, row);
      this.gridPaneLeft.add(entry.getValue().get(0), 1, row);
      this.gridPaneLeft.add(entry.getValue().get(1), 2, row);
      row++;
    }
  }

  private double[] parseToDouble(ArrayList<TextField> textFields) {
    double[] newDouble = new double[2];

    for (int i = 0; i < newDouble.length; i++) {
      try {
        newDouble[i] = StringUnitParser.stringToDouble(textFields.get(i).getText());
      } catch (Exception e) {
        newDouble[i] = 0;
      }
    }
    return newDouble;
  }

  public String returnUnits(double myDouble, String unitSymbol){
      return StringUnitParser.doubleToString(myDouble,unitSymbol);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
