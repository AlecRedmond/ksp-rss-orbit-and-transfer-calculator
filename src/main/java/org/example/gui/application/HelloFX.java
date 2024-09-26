package org.example.gui.application;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloFX extends Application {

  private Parent createContent() {
    return new StackPane(new Text("Hello World"));
  }

  @Override
  public void start(Stage stage) {
  }

  public static void pseudoMain(String[] args) {
    launch();
  }
}
