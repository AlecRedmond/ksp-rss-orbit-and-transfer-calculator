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

import java.util.*;

public class VisVivaGui extends Application {

  GridPane gridPane = new GridPane();

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("GridPlane Experiment");

    TextField apsisOneTextField = new TextField();
    TextField apsisTwoTextField = new TextField();
    Label apsisOneLabel = new Label("Apsis 1 altitude (m)");
    Label apsisTwoLabel = new Label("Apsis 2 altitude (m)");
    TextField eccentricityField = new TextField();
    TextField semiMajorAxisField = new TextField();
    Label eccentricityLabel = new Label("Eccentricity");
    Label semiMajorAxisLabel = new Label("SemiMajorAxis");

    LinkedHashMap<Node, Node> nodeHashMap = new LinkedHashMap<>();
    nodeHashMap.put(apsisOneLabel, apsisOneTextField);
    nodeHashMap.put(apsisTwoLabel, apsisTwoTextField);
    nodeHashMap.put(eccentricityLabel, eccentricityField);
    nodeHashMap.put(semiMajorAxisLabel, semiMajorAxisField);

    shapeGridPlane(nodeHashMap);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    Button button = new Button("Calculate e and SMA");

    button.setOnAction(
        action -> {
          double apsisOne = Double.parseDouble(apsisOneTextField.getText());
          double apsisTwo = Double.parseDouble(apsisTwoTextField.getText());
          KeplerianMethod keplerianMethod =
              new KeplerianMethod(Body.EARTH, apsisOne, apsisTwo, true);
          double eccentricity = keplerianMethod.getKeplerian().getEccentricity();
          double semiMajorAxis = keplerianMethod.getKeplerian().getSemiMajorAxis();
          eccentricityField.setText(String.valueOf(eccentricity));
          semiMajorAxisField.setText(String.valueOf(semiMajorAxis));
        });

    VBox vBox = new VBox(button);
    HBox hBox = new HBox(gridPane,vBox);
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
