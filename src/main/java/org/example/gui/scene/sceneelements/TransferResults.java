package org.example.gui.scene.sceneelements;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.method.HohmannTransfer;
import org.example.equations.method.InclinationBurn;
import org.example.equations.method.utility.TransferStringWriter;
import org.example.gui.scene.SceneConfig;

@Data
public class TransferResults {
    private Text initialBurn;
    private Node intermediateOrbit;
    private Text finalBurn;
    private HBox layout;
    
    public TransferResults(){
        initialBurn = new Text("Initial Burn");
        intermediateOrbit = new Text("Intermediate Orbit");
        finalBurn = new Text("Final Burn");
        layout = new HBox(initialBurn,intermediateOrbit,finalBurn);
        layout.setSpacing((double) SceneConfig.WIDTH /4);
    }

    public void updateText(Orbit orbitA,Orbit orbitB){
        HohmannTransfer hohmannTransfer = new HohmannTransfer(orbitA,orbitB);
        //initialBurn.setText(hohmannTransfer.getFirstBurn().toString());
        intermediateOrbit = (Node) stringsToGridPane(hohmannTransfer);
        //finalBurn.setText(hohmannTransfer.getSecondBurn().toString());
    }

    public GridPane stringsToGridPane(HohmannTransfer hohmannTransfer){
        GridPane gridPane = new GridPane();
        String[][] strings = TransferStringWriter.orbitToString(hohmannTransfer.getTransferOrbit());
        for(int row = 0; row < strings.length; row++){
            for(int col = 0; col < 2; col++){
                gridPane.add(new Text(strings[row][col]),col,row);
            }
        }
        return gridPane;
    }
}
