package org.example;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Stack;

public class HelloFX extends Application {

  private Parent createContent() {
    return new StackPane(new Text("Hello World"));
  }

  @Override
  public void start(Stage stage) {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    Label l =
        new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
    Button button = new Button("I am a button");
    StackPane layout = new StackPane();
    layout.getChildren().add(l);
    layout.getChildren().add(button);
    Scene scene = new Scene(layout, 640, 480);
    stage.setScene(scene);
    stage.show();
  }

  public static void pseudoMain(String[] args) {
    launch();
  }
}
