package org.artools.equations.method.utility;

import org.artools.equations.application.Orbit;
import org.artools.equations.application.keplerianelements.Kepler;

import java.util.Map;

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
