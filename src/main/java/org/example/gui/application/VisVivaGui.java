package org.example.gui.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gui.method.scene.SceneLayoutBuilder;

public class VisVivaGui extends Application {
  private Stage stage;

  public static void main(String[] args){
    launch(args);
  }
  @Override
  public void start(Stage stage) throws Exception {
    this.stage = stage;

    Scene scene = SceneLayoutBuilder.buildScene();
    stage.setScene(scene);
    stage.show();

  }
}
