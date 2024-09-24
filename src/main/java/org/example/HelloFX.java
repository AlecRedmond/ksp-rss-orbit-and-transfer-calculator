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
  }

  public static void pseudoMain(String[] args) {
    launch();
  }
}
