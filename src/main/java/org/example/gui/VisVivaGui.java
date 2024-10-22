package org.example.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gui.scenebuilder.SceneLayoutBuilder;

public class VisVivaGui extends Application {
  private Stage stage;

  public static void main(String[] args){
    launch(args);
  }
  @Override
  public void start(Stage stage) throws Exception {
    this.stage = stage;

    Scene scene = SceneLayoutBuilder.buildScene();
    //scene.getStylesheets().add(getClass().getResource("controlStyle1.css").toExternalForm());
    stage.setScene(scene);
    stage.show();

  }
}
