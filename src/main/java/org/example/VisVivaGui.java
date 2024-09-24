package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
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
import org.example.gui.OrbitalGridPlane;

import java.util.*;

public class VisVivaGui extends Application {

  GridPane gridPane = new GridPane();

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("GridPlane Experiment");

    TextField periapsisTF = new TextField();
    Label periapsisLb = new Label("Periapsis altitude (m)");
    TextField apoapsisTF = new TextField();
    Label apoapsisLb = new Label("Apoapsis altitude (m)");
    TextField eccentricityTF = new TextField();
    Label eccentricityLb = new Label("Eccentricity");
    TextField semiMajorAxisTF = new TextField();
    Label semiMajorAxisLb = new Label("SemiMajorAxis");

    LinkedHashMap<Node, Node> nodeHashMap = new LinkedHashMap<>();
    nodeHashMap.put(periapsisLb, periapsisTF);
    nodeHashMap.put(apoapsisLb, apoapsisTF);
    nodeHashMap.put(eccentricityLb, eccentricityTF);
    nodeHashMap.put(semiMajorAxisLb, semiMajorAxisTF);

    shapeGridPlane(nodeHashMap);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    Button button = new Button("Calculate e and SMA");

    button.setOnAction(
        action -> {
          double apsisOne = Double.parseDouble(periapsisTF.getText());
          double apsisTwo = Double.parseDouble(apoapsisTF.getText());
          KeplerianMethod keplerianMethod =
              new KeplerianMethod(Body.EARTH, apsisOne, apsisTwo, true);
          double apoapsis = keplerianMethod.getKeplerian().getApoapsis();
          double periapsis = keplerianMethod.getKeplerian().getPeriapsis();
          double eccentricity = keplerianMethod.getKeplerian().getEccentricity();
          double semiMajorAxis = keplerianMethod.getKeplerian().getSemiMajorAxis();
          periapsisTF.setText(String.valueOf(periapsis));
          apoapsisTF.setText(String.valueOf(apoapsis));
          eccentricityTF.setText(String.valueOf(eccentricity));
          semiMajorAxisTF.setText(String.valueOf(semiMajorAxis));
        });

    VBox vBox = new VBox(button);
    HBox hBox = new HBox(gridPane, vBox);
    hBox.setAlignment(Pos.BASELINE_CENTER);

    Scene scene = new Scene(hBox, 640, 480);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void shapeGridPlane(LinkedHashMap<Node, Node> nodeHashMap) {

    int row = 0;
    for (Map.Entry<Node, Node> entry : nodeHashMap.entrySet()) {
      gridPane.add(entry.getKey(), 0, row);
      gridPane.add(entry.getValue(), 1, row);
      row++;
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
