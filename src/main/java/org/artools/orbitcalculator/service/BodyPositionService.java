package org.artools.orbitcalculator.service;

import org.artools.orbitcalculator.application.BodyPosition;

import java.util.List;

public interface BodyPositionService {
    BodyPosition saveSolarSystemState(BodyPosition systemState);

    List<BodyPosition> fetchSolarSystemStates();

    BodyPosition getSolarSystemStateByID(String id);

    BodyPosition updateSolarSystemState(BodyPosition bodyPosition, String id);

    void deleteSolarSystemState(String id);
}
