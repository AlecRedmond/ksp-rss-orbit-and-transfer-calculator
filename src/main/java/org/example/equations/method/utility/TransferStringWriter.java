package org.example.equations.method.utility;

import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;

import java.util.Map;

import static org.example.gui.scene.SceneConfig.ORBITAL_RESULT_LABEL_CHARACTERS;

public class TransferStringWriter {
    private TransferStringWriter(){}

    public static String[][] orbitToString(Orbit orbit){
        String[][] strings = new String[orbit.getKeplarianElements().entrySet().size()][2];
        int row = 0;
        for(Map.Entry<Kepler.KeplerEnums,Kepler> entry : orbit.getKeplarianElements().entrySet()){
            strings[row][0] = orbit.getDisplayName(entry.getKey());
            strings[row][1] = orbit.getAsString(entry.getKey());
            row++;
        }
        return strings;
    }
}
