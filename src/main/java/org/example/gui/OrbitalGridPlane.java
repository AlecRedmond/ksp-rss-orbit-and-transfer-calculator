package org.example.gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.LinkedHashMap;

public class OrbitalGridPlane {


    public static LinkedHashMap<Node, Node> getGridPaneHashMap(){
        TextField apsisOneTextField = new TextField();
        Label apsisOneLabel = new Label("Apsis 1 altitude (m)");
        TextField apsisTwoTextField = new TextField();
        Label apsisTwoLabel = new Label("Apsis 2 altitude (m)");
        TextField eccentricityField = new TextField();
        Label eccentricityLabel = new Label("Eccentricity");
        TextField semiMajorAxisField = new TextField();
        Label semiMajorAxisLabel = new Label("SemiMajorAxis");

        LinkedHashMap<Node, Node> nodeHashMap = new LinkedHashMap<>();
        nodeHashMap.put(apsisOneLabel, apsisOneTextField);
        nodeHashMap.put(apsisTwoLabel, apsisTwoTextField);
        nodeHashMap.put(eccentricityLabel, eccentricityField);
        nodeHashMap.put(semiMajorAxisLabel, semiMajorAxisField);

        return nodeHashMap;
    }
}
