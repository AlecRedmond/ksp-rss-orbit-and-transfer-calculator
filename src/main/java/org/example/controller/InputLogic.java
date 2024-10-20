package org.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.example.equations.application.Orbit;
import org.example.equations.application.OrbitalParameterHolds;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;
import org.example.equations.method.OrbitBuilder;
import org.example.gui.nodemethod.OrbitalGridPane;

public class InputLogic {


    public static void parseVisVivaData() {
        HashMap<KeplerEnums,String> inputTextLeft = OrbitalGridPane.getFieldText(true);
        HashMap<KeplerEnums,Boolean> holdsLeft = OrbitalGridPane.getHolds(true);
        HashMap<KeplerEnums,String> inputTextRight = OrbitalGridPane.getFieldText(false);
        HashMap<KeplerEnums,Boolean> holdsRight = OrbitalGridPane.getHolds(false);

        Orbit orbitLeft = parseIntoOrbitFields(inputTextLeft,holdsLeft);
        Orbit orbitRight = parseIntoOrbitFields(inputTextRight,holdsRight);

        Orbit[] orbits = {orbitLeft,orbitRight};

        OrbitBuilder[] myOrbitBuilder = buildOrbits(holdsLeft, holdsRight, orbits);

        WorkingLogic.setOrbitBuilders(myOrbitBuilder);

    }

    private static OrbitBuilder[] buildOrbits(HashMap<KeplerEnums, Boolean> holdsLeft, HashMap<KeplerEnums, Boolean> holdsRight, Orbit[] orbits) {
        OrbitalParameterHolds orbitalParameterHoldsLeft = new OrbitalParameterHolds(holdsLeft);
        OrbitalParameterHolds orbitalParameterHoldsRight = new OrbitalParameterHolds(holdsRight);


        OrbitalParameterHolds[] orbitHolds = {orbitalParameterHoldsLeft,orbitalParameterHoldsRight};

        OrbitBuilder orbitBuilderLeft = new OrbitBuilder(orbits[0], orbitHolds[0]);
        OrbitBuilder orbitBuilderRight = new OrbitBuilder(orbits[1], orbitHolds[1]);
        OrbitBuilder[] myOrbitBuilder = {orbitBuilderLeft,orbitBuilderRight};
        return myOrbitBuilder;
    }

    private static Orbit parseIntoOrbitFields(HashMap<KeplerEnums, String> inputText,HashMap<KeplerEnums,Boolean> holds) {
        Orbit orbit = new Orbit();
        for(Map.Entry<KeplerEnums,String> entry : inputText.entrySet()){
            if(holds.get(entry.getKey())){
            orbit.setFromString(entry.getKey(), entry.getValue());}
        }
        return orbit;
    }
}
