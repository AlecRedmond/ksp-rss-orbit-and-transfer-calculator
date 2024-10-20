package org.example.controller;

import lombok.Data;
import org.example.equations.method.OrbitBuilder;

@Data
public class WorkingLogic {
    private static boolean doWork = false;
    private static OrbitBuilder orbitBuilder;

    public static void doVisVivaTransfer() {
    }
}
