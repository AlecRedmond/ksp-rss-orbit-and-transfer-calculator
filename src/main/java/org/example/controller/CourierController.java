package org.example.controller;

import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.application.OrbitalParameterHolds;

import java.util.HashMap;

@Data
public class CourierController {

    public static void calculateVisViva(){
        InputLogic.parseVisVivaData();
        WorkingLogic.doVisVivaTransfer();
        OutputLogic.writeVisVivaResults();
    }

    public static void debug(){
    }


}
