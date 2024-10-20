package org.example.stringformatting;

import java.util.HashMap;
import java.util.Map;

public class UnitsForElements {
    public static final HashMap<String,String> ELEMENTS = new HashMap<>(Map.of(
            "apoapsis","m",
            "periapsis","m",
            "semimajoraxis","m",
            "velocity","m"
    ));
}
