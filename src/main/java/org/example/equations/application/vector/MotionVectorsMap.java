package org.example.equations.application.vector;

import lombok.Getter;
import org.example.equations.application.Body;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class MotionVectorsMap {
    private final Map<Body, MotionVectors> map = new EnumMap<>(Body.class);

    public void putData(MotionVectors motionVectors){
        map.put(motionVectors.getCentralBody(), motionVectors);
    }

    public Optional<MotionVectors> getMotionVectors(Body body){
        return Optional.ofNullable(map.get(body));
    }
}
